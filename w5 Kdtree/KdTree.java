package com.myself;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

    private class KdNode {
        private Point2D point;
        private boolean isVertical;
        private KdNode leftChild;
        private KdNode rightChild;

        public KdNode(Point2D point, boolean isVertical){
            this.point = point;
            this.isVertical = isVertical;
        }
    }

    private KdNode root;
    private int size;
    private final RectHV CONTAINER_RECT = new RectHV(0,0,1,1);
    private ArrayList<Point2D> lists = new ArrayList<Point2D>();

    // construct an empty set of points
    public KdTree(){

    }

    public boolean isEmpty(){  return this.root == null; }

    // number of points in the set
    public int size(){
        return this.size;
    }


    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        if(contains(p)) return;

        if(isEmpty()) {
            this.root = new KdNode(p,true);
            this.size++;
            return;
        }

        KdNode current = this.root;
        while(true){
            if(current.isVertical && p.x() > current.point.x() || !current.isVertical && p.y() > current.point.y()){
                if(current.rightChild==null){
                    current.rightChild = new KdNode(p,!current.isVertical);
                    break;
                }else{
                    current = current.rightChild;
                }
            }else {
                if(current.leftChild==null){
                    current.leftChild = new KdNode(p,!current.isVertical);
                    break;
                }else{
                    current = current.leftChild;
                }
            }
        }

        lists.add(p);
        this.size++;
    }

    // does the set contain point p?
    public boolean contains(Point2D p){
        if(p == null) throw new IllegalArgumentException();
        boolean isExist = false;
        KdNode current = this.root;
        while(current != null){
            if(p.equals(current.point)){
                isExist = true;
                break;
            }
            if(current.isVertical && p.x() > current.point.x() ||
                    !current.isVertical && p.y() > current.point.y()){
                current = current.rightChild;
            }else {
                current = current.leftChild;
            }
        }
        return isExist;
    }

    public void draw() {
        StdDraw.setScale(0, 1);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        CONTAINER_RECT.draw();

        draw(this.root, CONTAINER_RECT);
    }

    // draw all points to standard draw
    private void draw(final KdNode node, final RectHV rect) {
//        for(Point2D point: this.lists){
//            point.draw();
//        }
        if (node == null) {
            return;
        }

        // draw the point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        new Point2D(node.point.x(), node.point.y()).draw();

        // get the min and max points of division line
        Point2D min, max;
        if (node.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            min = new Point2D(node.point.x(), rect.ymin());
            max = new Point2D(node.point.x(), rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            min = new Point2D(rect.xmin(), node.point.y());
            max = new Point2D(rect.xmax(), node.point.y());
        }

        // draw that division line
        StdDraw.setPenRadius();
        min.drawTo(max);

        // recursively draw children
        draw(node.leftChild, leftRect(rect, node));
        draw(node.rightChild, rightRect(rect, node));
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)  {
        if(rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        rangeSearch(this.root,rect, CONTAINER_RECT,points);
//        search(this.root,points,rect);

        return points;
    }

    private void rangeSearch(KdNode rootNode, RectHV queryRect, RectHV currentRect, ArrayList<Point2D> points){
        if(rootNode == null) return;

        // if queryRect does not intersect with current node's rect, then no need to search for the node and its child
        if(!queryRect.intersects(currentRect)){
            return;
        }

        if(queryRect.contains(rootNode.point)){
            points.add(rootNode.point);
        };

        rangeSearch(rootNode.leftChild, queryRect, leftRect(currentRect,rootNode),points);
        rangeSearch(rootNode.rightChild, queryRect, rightRect(currentRect,rootNode),points);
    }

    // node's left child rect
    private RectHV leftRect(RectHV cRect, KdNode node){
        if(node == null) return null;

        if(node.isVertical){
            return new RectHV(cRect.xmin(),cRect.ymin(),node.point.x(),cRect.ymax());
        }else{
            return new RectHV(cRect.xmin(),cRect.ymin(),cRect.xmax(),node.point.y());
        }
    }

    // node's right child rect
    private RectHV rightRect(RectHV cRect, KdNode node){
        if(node == null) return null;
        if(node.isVertical){
            return new RectHV(node.point.x(),cRect.ymin(),cRect.xmax(),cRect.ymax());
        }else{
            return new RectHV(cRect.xmin(),node.point.y(),cRect.xmax(),cRect.ymax());
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if(p == null) throw new IllegalArgumentException();
        if(isEmpty()) return null;
        if(contains(p)) return p;

        Point2D nearest = this.root.point;
        nearest = nearestSearch(p,nearest,this.root,CONTAINER_RECT);

        return nearest;
    }

    private Point2D nearestSearch(Point2D queryPoint, Point2D nearest, KdNode node, RectHV rect){
        if(node == null)  return nearest;

        // compare querypoint to nearest point so far and current node
        if(queryPoint.distanceSquaredTo(nearest) > queryPoint.distanceSquaredTo(node.point)){
            nearest = node.point;
        }

        // if current nearst point < current node's rect (node.leftchild+node.rightchild)
        // then no need to search the node and its child
        if(queryPoint.distanceSquaredTo(nearest) > rect.distanceSquaredTo(queryPoint)) {
            // if the query point is on the left side of root, then search from left to right
            if (node.isVertical && queryPoint.x() < node.point.x() ||
                    !node.isVertical && queryPoint.y() < node.point.y()) {
                nearest = nearestSearch(queryPoint, nearest, node.leftChild, leftRect(rect, node));
                nearest = nearestSearch(queryPoint, nearest, node.rightChild, rightRect(rect, node));
            } else {
                nearest = nearestSearch(queryPoint, nearest, node.rightChild, rightRect(rect, node));
                nearest = nearestSearch(queryPoint, nearest, node.leftChild, leftRect(rect, node));
            }
        }

        return nearest;
    }



}
