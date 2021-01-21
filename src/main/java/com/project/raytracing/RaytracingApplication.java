package com.project.raytracing;

import com.project.raytracing.bmpParsing.*;
import com.project.raytracing.geometry.RayTraceService;
import com.project.raytracing.geometry.Trig;
import com.project.raytracing.geometry.Vector3;
import com.project.raytracing.objParsing.ObjParser;
import com.project.raytracing.rendering.RenderImage;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class RaytracingApplication {

	public static void main(String[] args){
		RenderImage renderImage = new RenderImage(2500);
		renderImage.render();
	}





}
