package com.project.raytracing;


import com.project.raytracing.rendering.RenderImage;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RaytracingApplication {

	public static void main(String[] args){
		RenderImage renderImage = new RenderImage(500);
		renderImage.renderTree();
	}





}
