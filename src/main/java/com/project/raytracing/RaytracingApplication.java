package com.project.raytracing;

import com.project.raytracing.bmpParsing.*;
import com.project.raytracing.geometry.RayTraceService;
import com.project.raytracing.geometry.Trig;
import com.project.raytracing.geometry.Vector3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication
public class RaytracingApplication {



	public static void main(String[] args) {
			// Point from where rays would be cast
			Vector3 cameraPos = new Vector3(0, 0, -5);

			// Look direction
			Vector3 cameraDir = new Vector3(0, 0, 50);

			// Projection plane position is at 1 unit away from camera
			Vector3 planeOrigin = cameraPos.plus(cameraDir.norm());

			// Field of view in degrees
			float fov = 66;

			// Screen size in pixels
			int screenWidth = 50, screenHeight = 50;
			boolean[][] screenBuffer = new boolean[screenWidth][screenHeight];

			/*  b(-2,1,0) *--------* c(1,1,0)
			 *              \     /
			 *                \  /
			 *                  *
			 *             a(0,0,0)
			 */
			Trig triangleToDraw = new Trig(new Vector3(0,0,0), new Vector3(-2, 1, 0), new Vector3(1, 1, 0));

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
				}
			}

			// Output our buffer
			DrawScreenBuffer(screenHeight, screenWidth, screenBuffer);
		}



	private static void DrawScreenBuffer(int screenHeight, int screenWidth, boolean[][]screenBuffer ) {
		// Y first, output by rows
		for (int y = 0; y < screenHeight; y++) {
			for (int x = 0; x < screenWidth; x++) {
				System.out.print((screenBuffer[x][y] ? "X" : "."));
			}
			System.out.println();
		}
	}







}
