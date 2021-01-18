package com.project.raytracing.geometry;

public class RayTraceService {

    final static  double EPSILON = 1e-5f;

    public static boolean thereIsIntersectionBetweenRayAndTriangle(Vector3 rayOrigin, Vector3 rayVector, Trig inTriangle) {
        final Vector3 vertex0 = inTriangle.getA();
        final Vector3 vertex1 = inTriangle.getB();
        final Vector3 vertex2 = inTriangle.getC();
        final Vector3 edge1 = vertex1.minus(vertex0);
        final Vector3 edge2 = vertex2.minus(vertex0);
        final Vector3 h = rayVector.crossProduct(edge2);
        double a = edge1.dotProduct(h);
        
        if (a > EPSILON * -1 && a < EPSILON) {
            return false;
        }
        double f = 1 / a;
        final Vector3 s = rayOrigin.minus(vertex0);
        double u = f * s.dotProduct(h);
        if (u < 0.0 || u > 1.0) {
            return false;
        }
        final Vector3 q = s.crossProduct(edge1);
        double v = f * rayVector.dotProduct(q);
        if (v < 0.0 || u + v > 1.0) {
            return false;
        }

        // At this stage we can compute t to find out where the intersection point is on the line.
        double t = f * edge2.dotProduct(q);
        return t > EPSILON;
    }
}
