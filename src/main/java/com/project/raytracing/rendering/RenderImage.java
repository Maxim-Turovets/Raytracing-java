package com.project.raytracing.rendering;

import com.project.raytracing.bmpParsing.AbstractRgb888Image;
import com.project.raytracing.bmpParsing.BmpImage;
import com.project.raytracing.bmpParsing.BmpWriter;
import com.project.raytracing.bmpParsing.Rgb888Image;
import com.project.raytracing.geometry.RayTraceService;
import com.project.raytracing.geometry.Trig;
import com.project.raytracing.geometry.Vector3;
import com.project.raytracing.objParsing.ObjParser;
import com.project.raytracing.octree.Octree;
import lombok.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
@AllArgsConstructor
public class RenderImage {
    public  int SIZE;

    @SneakyThrows
    public void renderTree(){
        Octree a = new Octree(3d, new Vector3(0d, 0d, 0d));

        final String FILE_PATH = "f-16.obj";
        File file = new File(FILE_PATH);
        Scanner sc = new Scanner(file);

        try {
            while (sc.hasNextLine()) {
                double x, y, z;
                x = Double.parseDouble(sc.next());
                y = Double.parseDouble(sc.next());
                z = Double.parseDouble(sc.next());
                a.add_nodes(x, y, z);
            }
        }catch (Exception e){}
        finally {
            sc.close();
        }
        render();
    }

    public  void render() {
        ObjParser objParser = new ObjParser();
        objParser.parse();

        List<Trig> trigs = objParser.getTrigs();

        double X = (objParser.getMaxX() - objParser.getMinX() * -1 )/2 ;
        double Y = (objParser.getMaxY() - objParser.getMinY() * -1 )/2 ;
        double Z = -((objParser.getMaxX() + objParser.getMinX() * -1 )+(objParser.getMaxY() + objParser.getMinY() * -0.5 )) ;



        long s = System.currentTimeMillis();
        // Point from where rays would be cast
        Vector3 cameraPos = new Vector3(X, Y, Z);

        // Look direction
        Vector3 cameraDir = new Vector3(0, 0, 7);

        Vector3 planeOrigin = cameraPos.plus(cameraDir.norm());

        float fov = 63;

        int screenWidth = SIZE, screenHeight = SIZE;
        double[][] screenBuffer = new double[screenWidth][screenHeight];



        int treadCount = SIZE/10;

        List<Thread> threads = new ArrayList<>();

        for (int k = 0; k < treadCount; k++) {
            final int i2 = k;
            threads.add(new Thread(()->{
                for (int x = screenWidth/treadCount*i2; x < screenWidth/treadCount*(i2+1); x++) {
                    for (int y = 0; y < screenHeight; y++) {

                        var xNorm = (x - screenWidth/2) / (float)screenWidth;
                        var yNorm = -(y - screenHeight/2) / (float)screenHeight;

                        double distanceToPlaneFromCamera = (cameraPos.minus(planeOrigin)).length();
                        var fovInRad = fov / 180f * Math.PI;

                        float realPlaneHeight = (float) (distanceToPlaneFromCamera * Math.tan(fovInRad));


                        Vector3 positionOnPlane =
                                planeOrigin.plus( new Vector3(xNorm * realPlaneHeight / 2, yNorm * realPlaneHeight / 2, 0));

                        Vector3 rayDirection = positionOnPlane.minus(cameraPos);


                        for (int i = 0; i < trigs.size() ; i++) {
                            Trig trig = trigs.get(i);
                            if (RayTraceService.thereIsIntersectionBetweenRayAndTriangle(cameraPos, rayDirection, trig) > 0) {
                                screenBuffer[x][y] = RayTraceService.thereIsIntersectionBetweenRayAndTriangle(cameraPos, rayDirection, trig);
                            }
                        }

                    }
                }
                Thread.currentThread().interrupt();
            }));
        }

        threads.forEach(Thread::start);

        while (threads.stream().anyMatch(Thread::isAlive)){}
        drawScreenBuffer(screenBuffer);

        System.out.println(Thread.currentThread().getName());
        System.out.println(System.currentTimeMillis() - s);

    }

    @SneakyThrows
    private void drawScreenBuffer(double[][]screenBuffer )  {
        File file = new File("RenderImage.bmp");


        Rgb888Image image = new AbstractRgb888Image(SIZE, SIZE) {
            public int getRgb888Pixel(int x, int y) {
                if(screenBuffer[x][y] == 2){
                    return getColor(90);
                } if(screenBuffer[x][y] == 1){
                    return getColor(15);
                }else {
                    return getColor(0);
                }
            }
        };

        BmpImage bmp = new BmpImage();
        bmp.image = image;
        try (FileOutputStream out = new FileOutputStream(file)) {
            BmpWriter.write(out, bmp);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private int getColor(int percent){
        percent = 100 - percent;
        int rgb = 255 * percent / 100;
        Color color = new Color(rgb, rgb, rgb);
        return  color.getRGB();
    }
}
