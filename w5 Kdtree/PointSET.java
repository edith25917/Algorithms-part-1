package com.myself;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> set;

    // construct an empty set of points
    public PointSET(){
        this.set = new TreeSet<Point2D>();
    }

    public boolean isEmpty(){
        return this.set.isEmpty();
    }

    // number of points in the set
    public int size(){
        return this.set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p){
        if(p==null) throw new IllegalArgumentException();
        if(!this.set.contains(p)) this.set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p){
        if(p == null) throw new IllegalArgumentException();

        return this.set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for(Point2D point: this.set){
            System.out.println(point.toString());
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)  {
        if(rect == null) throw new IllegalArgumentException();

        ArrayList<Point2D> lists = new ArrayList<Point2D>();
        for(Point2D point: this.set){
            if(rect.contains(point)){
                lists.add(point);
            }
        }
        return lists;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if(p == null) throw new IllegalArgumentException();
        if (this.set.isEmpty()) return null;

        Point2D near = this.set.first();
        for(Point2D point: this.set){
            if(p.distanceTo(point) < p.distanceTo(near)){
                near = point;
            }
        }
        return near;
    }

}
