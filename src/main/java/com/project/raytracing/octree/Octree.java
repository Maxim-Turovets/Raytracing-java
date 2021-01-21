package com.project.raytracing.octree;

import com.project.raytracing.geometry.Vector3;


public class Octree {

    public Vector3 root;
    public Octree childNodes[];
    public boolean parent;
    public int depth;

    public double dimensions;

    public Octree(double x, Vector3 p) {
        this.root = p;
        this.dimensions = x;
    }


    public Octree() {
        root = null;
    }

    public int i = 0;


    public boolean add_nodes(double x, double y, double z) {

        boolean success = false;

        if (this.i < 8) {
            if (compare(x, y, z)) {
                if (root.getChildPoints() == null) {
                    System.out.println("Root is null" + x + " " + y + " " + z);
                }

                this.root.getChildPoints()[i] = new Vector3(x, y, z);
                this.i++;
                success = true;
            }
        }

        else if (this.childNodes == null) {

            this.create_nodes_of_nodes(x, y, z);

            for (int j = 0; j < 8; j++) {
                double tempx = this.root.getX() - root.getChildPoints()[j].getX();
                double tempy = this.root.getY() - root.getChildPoints()[j].getY();
                double tempz = this.root.getZ() - root.getChildPoints()[j].getZ();

                int checker = compareValues(tempx, tempy, tempz);

                this.childNodes[checker].add_nodes(root.getChildPoints()[j].getX(),
                        root.getChildPoints()[j].getY(), root.getChildPoints()[j].getZ());

            }
            root.setChildPoints(null);

            double tempx = this.root.getX() - x;
            double tempy = this.root.getY() - y;
            double tempz = this.root.getZ() - z;
            int checker = compareValues(tempx, tempy, tempz);
            this.childNodes[checker].add_nodes(x, y, z);
            // this.i=0;
        }

        else {

            if (childNodes != null) {
                int checker = compareValues(x, y, z);
                childNodes[checker].add_nodes(x, y, z);

            }
        }

        return success;
    }


    public int compareValues(double x, double y, double z) {

        if (x > 0 && y > 0 && z > 0)
            return 0;
        else if (x > 0 && y > 0 && z < 0)
            return 1;
        else if (x > 0 && y < 0 && z > 0)
            return 2;
        else if (x > 0 && y < 0 && z < 0)
            return 3;
        else if (x < 0 && y > 0 && z > 0)
            return 4;
        else if (x < 0 && y > 0 && z < 0)
            return 5;
        else if (x < 0 && y < 0 && z > 0)
            return 6;
        else
            return 7;

    }


    public boolean compare(double x, double y, double z) {
        if (x <= 2.0 && x >= -2.0) {
            if (y <= 2.0 && y >= -2.0) {
                if (z <= 2.0 && z >= -2.0) {
                    return true;
                }
            }
        }
        return true;
    }



    public double makeDimensions(double dimensions) {
        return (this.dimensions) / (2 ^ depth);
    }


    public boolean create_nodes_of_nodes(double x, double y, double z) {

        this.childNodes = new Octree[8];
        boolean success = false;

        this.childNodes[0] = new Octree(this.dimensions / 2, new Vector3(
                this.root.getX() + dimensions / 2, this.root.getY() + dimensions / 2,
                this.root.getZ() + dimensions / 2));

        this.childNodes[1] = new Octree(this.dimensions / 2, new Vector3(
                this.root.getX() + dimensions / 2, this.root.getY() + dimensions / 2,
                this.root.getZ() - dimensions / 2));

        this.childNodes[2] = new Octree(this.dimensions / 2, new Vector3(
                this.root.getX() + dimensions / 2, this.root.getY() - dimensions / 2,
                this.root.getZ() + dimensions / 2));

        this.childNodes[3] = new Octree(this.dimensions / 2, new Vector3(
                this.root.getX() + dimensions / 2, this.root.getY() - dimensions / 2,
                this.root.getZ() - dimensions / 2));

        this.childNodes[4] = new Octree(this.dimensions / 2, new Vector3(
                this.root.getX() - dimensions / 2, this.root.getY() + dimensions / 2,
                this.root.getZ() + dimensions / 2));

        this.childNodes[5] = new Octree(this.dimensions / 2, new Vector3(
                this.root.getX() - dimensions / 2, this.root.getY() + dimensions / 2,
                this.root.getZ() - dimensions / 2));

        this.childNodes[6] = new Octree(this.dimensions / 2, new Vector3(
                this.root.getX() - dimensions / 2, this.root.getY() - dimensions / 2,
                this.root.getZ() + dimensions / 2));

        this.childNodes[7] = new Octree(this.dimensions / 2, new Vector3(
                this.root.getX() - dimensions / 2, this.root.getY() - dimensions / 2,
                this.root.getZ() - dimensions / 2));

        return success;

    }


    public void print_tree() {

        int depth = (int) (Math.log(2 / dimensions) / Math.log(2));

        for (int k = 0; k < depth; k++) {
            System.out.print("   ");
        }

        System.out.println("(" + root.getX() + ", " + root.getY() + ", " + root.getZ() + ")"
                + "  -- " + dimensions);

        if (this.childNodes == null) {
            for (int j = 0; j < i; j++) {
                for (int k = 0; k < depth; k++) {
                    System.out.print("   ");
                }
                System.out.println("  *  (" + this.root.getChildPoints()[j].getY() + ", "
                        + this.root.getChildPoints()[j].getY() + ", "
                        + this.root.getChildPoints()[j].getZ() + ")");
            }
        } else {
            for (int j = 0; j < i; j++) {
                this.childNodes[j].print_tree();
            }
        }
    }

}
