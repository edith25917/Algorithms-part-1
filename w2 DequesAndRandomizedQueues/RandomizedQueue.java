import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items = (Item[]) new Object[2];
    private int size = 0;


    //construct an empty randomized queue
    public RandomizedQueue() {
    }

    //is the randomized queue empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    //return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    //add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("illegal args enqueue");
        }

        if (this.size == this.items.length) {
            resize(this.items.length * 2);
        }

        int index = StdRandom.uniform(this.size+1);
        if(index != this.size){
            this.items[this.size] = this.items[index];
        }
        this.items[index] = item;
        this.size++;

    }

    //remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("dequeue no elem");
        }



        Item i = this.items[this.size - 1];
        this.items[this.size - 1] = null;
        this.size--;

        if (this.size > 0 && this.size == this.items.length / 4) {
            resize(this.items.length / 2);
        }

        return i;
    }

    //return a random item(but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("sample no elem");
        }
        int r = StdRandom.uniform(this.size);

        return this.items[r];
    }

    //return an independent iterator over items in random order
    public Iterator<Item> iterator() {

        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {

        private int count = 0;
        private Item[] itemArr = (Item[]) new Object[size()];

        public ArrayIterator() {
            for(int i=0;i<size();i++){
                itemArr[i] = items[i];
            }
            StdRandom.shuffle(itemArr);
        }

        @Override
        public boolean hasNext() {
            return count < size();
        }

        @Override
        public Item next() {
            if (hasNext()) {
                return itemArr[count++];

            }
            throw new NoSuchElementException("no next item");
        }
    }

    private void resize(int capacity) {
        Item[] tmp = (Item[]) new Object[capacity];
        for (int i = 0; i < size(); i++) {
            tmp[i] = this.items[i];
        }

        this.items = tmp;
    }

    //unit testing
    public static void main(String[] args) {

        RandomizedQueue<String> rq = new RandomizedQueue<>();
    }


}
