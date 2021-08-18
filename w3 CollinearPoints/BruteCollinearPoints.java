import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Arrays;


public class BruteCollinearPoints {


    private Point[] points;
    private LineSegment[] segments;
    private int segCount;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        if (points == null || hasNull(points) || isRepeated(points)) {
            throw new IllegalArgumentException("null pointer exception");
        }

        //don't change the input
        this.points = points.clone();
        this.segments = new LineSegment[2];
        this.segCount = 0;

        //sort by natural order, to get the first and last point correct
       Arrays.sort(this.points);

        for (int i = 0; i < this.points.length - 3; i++) {
            for (int j = i + 1; j < this.points.length - 2; j++) {
                for (int k = j + 1; k < this.points.length - 1; k++) {
                    for (int h = k + 1; h < this.points.length; h++) {
                        double slope1 = this.points[i].slopeTo(this.points[j]);
                        double slope2 = this.points[j].slopeTo(this.points[k]);
                        double slope3 = this.points[k].slopeTo(this.points[h]);

                        if (slope1 == slope2 && slope2 == slope3) {
//                           System.out.println(this.points[i].toString()+this.points[j].toString()+this.points[k].toString()+this.points[h].toString()+" is collinear!");
                            if (this.segments.length == this.segCount) {
                                resize(this.segments.length*2);
                            }

                            this.segments[this.segCount++] = new LineSegment(this.points[i], this.points[h]);
                        }
                    }
                }
            }
        }

    }

    private void resize(int capacity) {
        LineSegment[] tmp = new LineSegment[capacity];
        for (int i = 0; i < this.segments.length; i++) {
            tmp[i] = this.segments[i];
        }
        this.segments = tmp;
    }


    private boolean hasNull(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                return true;
            }
        }
        return false;
    }

    private boolean isRepeated(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) {
                    return true;
                }
            }
        }
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.segCount;
    }

    // the line segments
    public LineSegment[] segments() {
        //return a copy for not destroying encapsulation
        return Arrays.copyOf(this.segments,this.segCount);
    }

    public static void main(String[] args){

        //read n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for(int i=0;i<n;i++){
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x,y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();


        BruteCollinearPoints bcp = new BruteCollinearPoints(points);

    }


}