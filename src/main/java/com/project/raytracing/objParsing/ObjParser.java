package com.project.raytracing.objParsing;

import com.project.raytracing.bmpParsing.AbstractRgb888Image;
import com.project.raytracing.bmpParsing.BmpImage;
import com.project.raytracing.bmpParsing.BmpWriter;
import com.project.raytracing.bmpParsing.Rgb888Image;
import com.project.raytracing.geometry.Trig;
import com.project.raytracing.geometry.Vector3;
import lombok.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@NoArgsConstructor
@Data
public class ObjParser {

    private final  static String FILE_PATH = "Demo.obj";
   // private final  static String FILE_PATH = "porsche.obj";

    private File loadObjFile(){
        return new File(FILE_PATH);
    }

    private Map<Integer,Vector3> points = new HashMap<>();
    private List<Trig> trigs = new ArrayList<>();

    private double maxX = -9999999l;
    private double minX = 9999999l;
    private double maxY = -9999999l;
    private double minY = 9999999l;
    private double minZ = 9999999l;



    public  void parse(){

        int count = 1;
        File objFile = loadObjFile();

        try {
            Scanner myReader = new Scanner(objFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.equalsIgnoreCase("")) continue;
                if (data.charAt(0) == 'v' && data.charAt(1) == ' '){
                    points.put(count,parsePoint(data));
                    count++;
                }
                if (data.charAt(0) == 'f'){
                   trigs.add(parseTriangle(data));
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Load (.obj) file exception");
        }


    }


    private Vector3 parsePoint(String string){
        String[] splitsData = string.split(" ");
        double x = Double.parseDouble(splitsData[1]);
        double y = Double.parseDouble(splitsData[2]);
        double z = Double.parseDouble(splitsData[3]);

        if(x > this.maxX)
            this.maxX = x;
        if(y > this.maxY)
            this.maxY = y;
        if(z < this.minZ)
            this.minZ = z;
        if(y < this.minY)
            this.minY = y;
        if(x < this.minX)
            this.minX = x;

        return  new Vector3(x,y,z);
    }

    private Trig parseTriangle(String string){
        String[] splitsData = string.split(" ");
        String splitter = string.contains("//") ? "//" : "/";
        Vector3 vec1 = points.get(Integer.parseInt(splitsData[1].split(splitter)[0]));
        Vector3 vec2 = points.get(Integer.parseInt(splitsData[2].split(splitter)[0]));
        Vector3 vec3 = points.get(Integer.parseInt(splitsData[3].split(splitter)[0]));

        return  new Trig(vec1,vec2,vec3);
    }

}
