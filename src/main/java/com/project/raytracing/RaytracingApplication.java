package com.project.raytracing;

import com.project.raytracing.bmpParsing.*;
import com.project.raytracing.geometry.RayTraceService;
import com.project.raytracing.geometry.Trig;
import com.project.raytracing.geometry.Vector3;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication
public class RaytracingApplication {

    public static int SIZE = 500;

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
			// Point from where rays would be cast
			Vector3 cameraPos = new Vector3(0, 0, -10);

			// Look direction
			Vector3 cameraDir = new Vector3(0, 0, 1);

			// Projection plane position is at 1 unit away from camera
			Vector3 planeOrigin = cameraPos.plus(cameraDir.norm());

			// Field of view in degrees
			float fov = 60;

			// Screen size in pixels
			int screenWidth = SIZE, screenHeight = SIZE;
			boolean[][] screenBuffer = new boolean[screenWidth][screenHeight];

			/*  b(-2,1,0) *--------* c(2,1,0)
			 *              \     /
			 *                \  /
			 *                  *
			 *             a(0,0,0)
			 */

			Trig triangleToDraw = new Trig(new Vector3(0,0,0), new Vector3(-1, 1, 0), new Vector3(1, 1, 0));
			Trig triangleToDraw2 = new Trig(new Vector3(0,0,0), new Vector3(-2, -1, 0), new Vector3(1, -1, 0));
			Trig triangleToDraw3 = new Trig(new Vector3(0,0,0), new Vector3(3, -1, 0), new Vector3(2, 1, 0));
			Trig triangleToDraw4 = new Trig(new Vector3(0,0,0), new Vector3(-2, 1, 0), new Vector3(-3, -1, 0));
			Trig triangleToDraw5 = new Trig(new Vector3(0,0,0), new Vector3(-2, 3, 0), new Vector3(-3, -3, 0));

			for (int x = 0; x < screenWidth; x++) {
				for (int y = 0; y < screenHeight; y++) {
					// Normalized coordinates of pixel to [-1;1] interval;
					// y is inverted because in console output it goes from 0 to screenHeight
					// and in world coordinates y goes from realPlaneHeight/2 to -realPlaneHeight/2
					var xNorm = (x - screenWidth/2) / (float)screenWidth;
					var yNorm = -(y - screenHeight/2) / (float)screenHeight;

					double distanceToPlaneFromCamera = (cameraPos.minus(planeOrigin)).length();
					var fovInRad = fov / 180f * Math.PI;

					// Height of plane in front of camera in our world units. Tangent function needs radians
					float realPlaneHeight = (float) (distanceToPlaneFromCamera * Math.tan(fovInRad));

					// Here we suppose that camera is looking always from point (0,0,-Z), so the math here is little simpler to understand
					// We need to find point on plane that is xNorm * realPlaneHeight / 2 units away from center in x direction,
					// and similarly in y-dir. If the camera is moved and/or plane is rotated, math is little harder here
					// Also, we assume height == width
					Vector3 positionOnPlane =
							planeOrigin.plus( new Vector3(xNorm * realPlaneHeight / 2, yNorm * realPlaneHeight / 2, 0));

					// Throw a ray from camera to the point we found and beyond
					Vector3 rayDirection = positionOnPlane.minus(cameraPos);

					// If we find an intersection of the ray with our triangle, we draw "pixel"
					if (RayTraceService.thereIsIntersectionBetweenRayAndTriangle(cameraPos, rayDirection, triangleToDraw)) {
						screenBuffer[x][y] = true;
					}
					if (RayTraceService.thereIsIntersectionBetweenRayAndTriangle(cameraPos, rayDirection, triangleToDraw2)) {
						screenBuffer[x][y] = true;
					}
					if (RayTraceService.thereIsIntersectionBetweenRayAndTriangle(cameraPos, rayDirection, triangleToDraw3)) {
						screenBuffer[x][y] = true;
					}
					if (RayTraceService.thereIsIntersectionBetweenRayAndTriangle(cameraPos, rayDirection, triangleToDraw4)) {
						screenBuffer[x][y] = true;
					}
					if (RayTraceService.thereIsIntersectionBetweenRayAndTriangle(cameraPos, rayDirection, triangleToDraw5)) {
						screenBuffer[x][y] = true;
					}
				}
			}

			// Output our buffer
			DrawScreenBuffer(screenHeight, screenWidth, screenBuffer);

		System.out.println(System.currentTimeMillis() - s);
		}



	@SneakyThrows
	private static void DrawScreenBuffer(int screenHeight, int screenWidth, boolean[][]screenBuffer )  {
		File file = new File("Demo.bmp");

		Rgb888Image image = new AbstractRgb888Image(SIZE, SIZE) {
			public int getRgb888Pixel(int x, int y) {
				if(screenBuffer[x][y]){
					return 0xFF0000;
				} else {
					return  0xFFFFFF;
				}
			}
		};

		BmpImage bmp = new BmpImage();
		bmp.image = image;
		FileOutputStream out = new FileOutputStream(file);
		try {
			BmpWriter.write(out, bmp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}

//		// Y first, output by rows
//		for (int y = 0; y < screenHeight; y++) {
//			for (int x = 0; x < screenWidth; x++) {
//				System.out.print((screenBuffer[x][y] ? "X" : "."));
//			}
//			System.out.println();
//		}
	}







}
