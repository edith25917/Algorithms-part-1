import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    /*task
     what is the probability that the system percolates?
     When p equals 0, the system does not percolate;
     when p equals 1, the system percolates.
    When n is sufficiently large, there is a threshold value p* such that when p < p* a random n-by-n grid almost never percolates,
    and when p > p*, a random n-by-n grid almost always percolates.
    No mathematical solution for determining the percolation threshold p* has yet been derived.
    Your task is to write a computer program to estimate p*.
    */
    // creates n-by-n grid, with all sites initially blocked

    //0:blocked, 1: open, 2: top connect, 3: bottom connect, 4: both connect
    private final byte[][] sites;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF singleuf;
    private int top = 0;
    private int bottom;
    private int len;
    private int opensite;


    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must larger than 0");
        }
        this.sites = new byte[n][n];
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.singleuf = new WeightedQuickUnionUF(n * n + 1);
        this.bottom = n * n + 1;
        this.len = n;

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                this.sites[i][k] = 0;
            }
        }
    }


    private int getIndex(int row, int col) {
        return this.len * (row - 1) + col;
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        //System.out.println("row:"+(row)+" col:"+(col));
        if (row <= 0 || col <= 0 || row > this.sites.length || col > this.sites.length) {
            throw new IllegalArgumentException("args not included");
        }

        if (this.sites[row - 1][col - 1] > 0) {
            return;
        }


        this.sites[row - 1][col - 1] = 1;
        this.opensite++;

        if (row == 1) {
            this.uf.union(getIndex(row, col), top);
            this.singleuf.union(getIndex(row,col),top);
        }

        if (row == this.len) {
            this.uf.union(getIndex(row, col), bottom);
        }


        //union possible neighbor
        for (int i = getNeighbor(row - 1, -1); i <= getNeighbor(row - 1, 1); i++) {
            if (isOpen(i + 1, col)) {
                this.uf.union(getIndex(row, col), getIndex(i + 1, col));
                this.singleuf.union(getIndex(row, col), getIndex(i + 1, col));
            }
        }

        for (int k = getNeighbor(col - 1, -1); k <= getNeighbor(col - 1, 1); k++) {
            if (isOpen(row, k + 1)) {
                this.uf.union(getIndex(row, col), getIndex(row, k + 1));
                this.singleuf.union(getIndex(row, col), getIndex(row, k + 1));

            }
        }


    }

    private int getNeighbor(int current, int var) {
        if (current + var >= 0 && current + var < this.sites.length) {
            return current + var;
        }
        return current;

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > this.sites.length || col > this.sites.length) {
            throw new IllegalArgumentException("args not included");
        }

        return this.sites[row - 1][col - 1] > 0;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row > this.sites.length || col > this.sites.length) {
            throw new IllegalArgumentException("args not included");
        }

//        for(int i=this.len-1;i<this.len;i++){
//            for(int k =0;k<this.len;k++){
//                System.out.println("("+i+","+k+") "+ (this.singleuf.find(getIndex(i+1,k+1)) == this.singleuf.find(this.top)));;
//            }
//        }

        return this.singleuf.find(getIndex(row, col)) == this.singleuf.find(this.top);
    }


    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.opensite;
    }

    // does the system percolate?
    public boolean percolates() {

//        print();
        return this.uf.find(this.top) == this.uf.find(this.bottom);

    }

//    private void print() {
//        System.out.println(this.uf.find(this.top) + " ");
//
//        for (int i = 1; i <= this.len; i++) {
//            for (int k = 1; k <= this.len; k++) {
//                if (this.uf.find(getIndex(i, k)) < 10) {
//                    System.out.print(this.uf.find(getIndex(i, k)) + " " + this.sites[i - 1][k - 1] + "\t\t");
//
//                } else {
//                    System.out.print(this.uf.find(getIndex(i, k)) + " " + this.sites[i - 1][k - 1] + "\t");
//
//                }
//
//            }
//            System.out.println();
//        }
//
////        System.out.println(this.uf.find(this.bottom));
//
//    }

//    private void print() {
//        for (int i = 0; i < this.sites.length; i++) {
//            for (int k = 0; k < this.sites.length; k++) {
//                if (this.sites[i][k].id < 10 || (this.sites[i][k].id >= 10 && this.sites[i][k].id < 100 && this.sites[i][k].isOpen)) {
//                    System.out.print(this.sites[i][k].id + " " + (this.sites[i][k].isOpen) + "\t\t");
//                } else {
//                    System.out.print(this.sites[i][k].id + " " + (this.sites[i][k].isOpen) + "\t");
//                }
//            }
//            System.out.println();
//        }
//    }


}