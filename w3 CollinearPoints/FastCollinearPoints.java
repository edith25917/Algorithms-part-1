import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Quick3way;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {

    private Point[] points;
    private LineSegment[] segments;
    private int segCount;
    private int pointCount;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null || hasNull(points) || isRepeated(points)) {
            throw new IllegalArgumentException("null pointer exception");
        }
        this.segCount = 0;
        this.segments = new LineSegment[2];
        this.points = points.clone();

        //need to sort by natural order first, to determine the first and last point correctly
        Arrays.sort(this.points);

        //if use foreach, no need to create new this.points, otherwise create new arr to sort
        for (int i = 0; i < this.points.length - 1; i++) {

            Point[] newPointsArr = new Point[this.points.length];
            for (int c = 0; c < this.points.length; c++) {
                newPointsArr[c] = this.points[c];
            }

            //sort again to make the equal slope gather together
            Arrays.sort(newPointsArr, this.points[i].slopeOrder());


            //find if three or more slope is existed as line segment,，if yes, find first and last
            int count = 0;
            double currentSlope = 0.0;
            double prevSlope = this.points[i].slopeTo(newPointsArr[0]);
            boolean isFirst = true;
            int lastRoll = this.points.length - 1;

            for (int a = 1; a < this.points.length; a++) {
                if (this.points[i].compareTo(newPointsArr[a]) == 0) {
                    continue;
                }

                currentSlope = this.points[i].slopeTo(newPointsArr[a]);

                //check isfirst
                if (prevSlope == currentSlope && this.points[i].compareTo(newPointsArr[a - 1]) > 0) {
                    isFirst = false;
                }

                //check line segment
                if (a == lastRoll || prevSlope != currentSlope) {
                    if (a == lastRoll && prevSlope == currentSlope) {
                        count++;
                    }
                    // 有 line seg
                    if (count >= 3 && isFirst) {
                        Point first = this.points[i];
                        Point last = newPointsArr[a - 1];
                        if (a == lastRoll && prevSlope == currentSlope) {
                            last = newPointsArr[a];
                        }

                        if (this.segments.length == this.segCount) {
                            resize(this.segments.length * 2);
                        }
//                        System.out.println(first.toString() + " " + last.toString() + " is collinear!");
                        this.segments[this.segCount++] = new LineSegment(first, last);
                    }
                    count = 0;
                    isFirst = true;
                }

                count++;
                prevSlope = currentSlope;

            }
        }
    }




    private void firstTry(Point[] points) {
        if (points == null || hasNull(points) || isRepeated(points)) {
            throw new IllegalArgumentException("null pointer exception");
        }
        this.segCount = 0;
        this.segments = new LineSegment[2];
        this.points = points.clone();
        this.pointCount = 0;

        Arrays.sort(this.points);
        double[] slopes = new double[2];

        for (int i = 0; i < this.points.length - 1; i++) {
            this.pointCount = 0;

            //排序該點 到其它所有點之間的斜率
            for (int k = 0; k < this.points.length; k++) {
                if (i == k) {
                    continue;
                }
                if (slopes.length == this.pointCount) {
                    slopes = resize(slopes, slopes.length * 2);
                }
                slopes[this.pointCount++] = this.points[i].slopeTo(this.points[k]);
            }

            Arrays.sort(slopes, 0, this.pointCount);

            //找出是否有三個以上一樣的斜率，若有的話 取first (point[i]),和last
            int start = 0;
            int count = 0;
            double s = slopes[start];

            while (start < this.pointCount - 1) {
                while (start + 1 < this.pointCount && slopes[++start] == s) {
                    count++;
                }
                if (count >= 2) {
                    Point first = this.points[i];

                    //is first element, 解決sub segment問題
                    boolean isFirst = true;
                    for (int h = i - 1; h >= 0; h--) {
                        if (this.points[h].slopeTo(this.points[i]) == s) {
                            isFirst = false;
                        }
                    }
                    if (isFirst) {
                        //find last
                        for (int j = this.points.length - 1; j > 0; j--) {
                            if (this.points[i].slopeTo(this.points[j]) == s) {
                                Point last = this.points[j];
                                if (this.segments.length == this.segCount) {
                                    resize(this.segments.length * 2);
                                }
                                System.out.println(first.toString() + " " + last.toString() + " is collinear!");
                                this.segments[this.segCount++] = new LineSegment(first, last);
                                break;
                            }
                        }
                    }
                }
                count = 0;
                s = slopes[start];
            }
        }
    }


    // the number of line segments
    public int numberOfSegments() {
        return this.segCount;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(this.segments, this.segCount);
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
                if (points[i].compareTo(points[j]) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void resize(int capacity) {
        LineSegment[] tmp = new LineSegment[capacity];
        for (int i = 0; i < this.segments.length; i++) {
            tmp[i] = this.segments[i];
        }
        this.segments = tmp;
    }

    private double[] resize(double[] a, int capacity) {
        double[] tmp = new double[capacity];
        for (int i = 0; i < a.length; i++) {
            tmp[i] = a[i];
        }
        return tmp;

    }

    public static void main(String[] args) {

        //read n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        FastCollinearPoints fcp = new FastCollinearPoints(points);

    }

}