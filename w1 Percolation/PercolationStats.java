import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int trials;
    private final double[] means;
    private final double confidenceVar = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N <= 0 or T <= 0");
        }
        this.trials = trials;
        this.means = new double[this.trials];


        int count = 0;
        while (count < this.trials) {
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                int a = StdRandom.uniform(1, n+1);
                int b = StdRandom.uniform(1, n+1);

                p.open(a, b);
            }


            double threshold = (double) p.numberOfOpenSites() / (n * n);
            this.means[count] = threshold;
            count++;

        }

//        System.out.println("me mean\t\t\t\t=" + mean());
//        System.out.println("me stddev\t\t\t\t=" + stddev());
//        System.out.println("me 95 confidence interval\t\t\t\t=" + confidenceLo() + ", " + confidenceHi());


    }

    // sample mean of percolation threshold
    public double mean() {
       return StdStats.mean(this.means);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.means);

    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - this.confidenceVar * stddev() / Math.sqrt(this.trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + this.confidenceVar * stddev() / Math.sqrt(this.trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats pstats = new PercolationStats(n, T);

        System.out.println("mean\t\t\t\t=" + pstats.mean());
        System.out.println("stddev\t\t\t\t=" + pstats.stddev());
        System.out.println("95 confidence interval\t\t\t\t=[" + pstats.confidenceLo() + ", " + pstats.confidenceHi()+"]");

    }
}
