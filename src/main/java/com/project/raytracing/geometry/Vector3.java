package com.project.raytracing.geometry;

import com.project.raytracing.RaytracingApplication;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vector3 {
    private double x;
    private double y;
    private double z;


    public Vector3 plus (Vector3 right) {
        return new Vector3(this.getX() + right.getX(), this.getY() + right.getY(),this.getZ() + right.getZ());
    }
    public Vector3 minus(Vector3 right) {
        return new Vector3(this.getX() - right.getX(), this.getY() - right.getY(),this.getZ() - right.getZ());
    }

    public double length() {
        return Math.sqrt(
                this.getX() * this.getX() +
                this.getY() * this.getY() +
                this.getZ() * this.getZ());
    }

    public Vector3 norm() {
        var length = length();
        return new Vector3(this.getX()/length, this.getY()/length, this.getZ()/length);
    }

    public Vector3 crossProduct(Vector3 edge2) {
        Vector3 u = this;
        return new Vector3(
                u.getY() * edge2.getZ() - u.getZ() * edge2.getY(),
                u.getZ() * edge2.getX() - u.getX() * edge2.getZ(),
                u.getX() * edge2.getY() - u.getY() * edge2.getX());
    }

    public double dotProduct(Vector3 other) {
        return this.getX() * other.getX() + this.getY() * other.getY() + this.getZ() * other.getZ();
    }

}
