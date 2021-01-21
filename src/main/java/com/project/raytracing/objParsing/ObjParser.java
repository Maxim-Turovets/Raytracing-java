package com.project.raytracing.objParsing;

import com.project.raytracing.geometry.Trig;
import com.project.raytracing.geometry.Vector3;
import lombok.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@NoArgsConstructor
@Data
public class ObjParser {

    //private final  static String FILE_PATH = "Patricio.obj";
    private final  static String FILE_PATH = "f-16.obj";

    private File loadObjFile(){
        return new File(FILE_PATH);
    }

    private Map<Integer,Vector3> points = new HashMap<>();
    private Map<Integer,Vector3> normals = new HashMap<>();

    private List<Trig> trigs = new ArrayList<>();

    private double maxX = -9999999L;
    private double minX = 9999999L;
    private double maxY = -9999999L;
    private double minY = 9999999L;
    private double minZ = 9999999L;



    public  void parse(){

        int countPoints = 1;
        int countNormals = 1;
        File objFile = loadObjFile();

        try {
            Scanner myReader = new Scanner(objFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.equalsIgnoreCase("")) continue;
                if (data.charAt(0) == 'v' && data.charAt(1) == ' '){
                    points.put(countPoints,parsePoint(data));
                    countPoints++;
                }
                if (data.charAt(0) == 'v' && data.charAt(1) == 'n'){
                    normals.put(countNormals,parseNormals(data));
                    countNormals++;
                }
                if (data.charAt(0) == 'f'){
                    if(data.split(" ").length > 4) {
                        trigs.add(parseTriangle(data, false));
                        trigs.add(parseTriangle(data,true));
                    } else {
                        trigs.add(parseTriangle(data, false));
                    }
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

    private Trig parseTriangle(String string,boolean rect){
        String[] splitsData = string.split(" ");
        String splitter = string.contains("//") ? "//" : "/";

        Vector3 vec1 = null;
        Vector3 vec2 = null;
        Vector3 vec3 = null;
        if(!rect) {
             vec1 = points.get(Integer.parseInt(splitsData[1].split(splitter)[0]));
             vec2 = points.get(Integer.parseInt(splitsData[2].split(splitter)[0]));
             vec3 = points.get(Integer.parseInt(splitsData[3].split(splitter)[0]));
        } else {
             vec1 = points.get(Integer.parseInt(splitsData[2].split(splitter)[0]));
             vec2 = points.get(Integer.parseInt(splitsData[3].split(splitter)[0]));
             vec3 = points.get(Integer.parseInt(splitsData[4].split(splitter)[0]));
        }
        return new Trig(vec1, vec2, vec3,rect);
    }

    private Vector3 parseNormals(String string){
        String[] splitsData = string.split(" ");
        double x = Double.parseDouble(splitsData[1]);
        double y = Double.parseDouble(splitsData[2]);
        double z = Double.parseDouble(splitsData[3]);


        return  new Vector3(x,y,z);
    }


}
