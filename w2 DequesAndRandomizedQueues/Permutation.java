import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Iterator;

public class Permutation {

    public static void main(String[] args) {

        //take command line argument
       int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String value = StdIn.readString();
            rq.enqueue(value);
        }

        for(int i=0;i<k;i++){
            StdOut.println(rq.dequeue());
        }


    }


}
