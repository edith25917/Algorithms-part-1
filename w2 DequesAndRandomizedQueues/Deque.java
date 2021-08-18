import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int count = 0;
    private Node first = null;
    private Node tail = null;

    private class Node {
        Item item;
        Node next;
        Node prev;

        private Node(Item i) {
            this.item = i;
        }
    }

    //construct an empty deque
    public Deque() {
    }

    //is the deque empty
    public boolean isEmpty() {
        return this.count == 0;
    }

    //return the number of items on the deque
    public int size() {
        return this.count;
    }

    //add the item to the front
    public void addFirst(Item item) {
        validateItem(item);

        Node oldFirst = this.first;
        this.first = new Node(item);
        this.first.next = oldFirst;
        this.first.prev = null;

        if (isEmpty()) {
            this.tail = this.first;
        } else {
            oldFirst.prev = this.first;
        }

        count++;
    }

    //add the item to the back
    public void addLast(Item item) {
        validateItem(item);

        Node oldLast = this.tail;
        this.tail = new Node(item);
        this.tail.next = null;

        if(isEmpty()){
            this.first = this.tail;
        }else{
            this.tail.prev = oldLast;
            oldLast.next = this.tail;
        }
        count++;
    }

    //remove and return the item from the front
    public Item removeFirst() {
        validateList();
        Item currentFirst = this.first.item;
        count--;

        if (isEmpty()) {
            this.first = null;
            this.tail = null;
        } else {
            this.first = this.first.next;
            first.prev = null;
        }
        return currentFirst;
    }

    //remove and return the item from the back
    public Item removeLast() {
        validateList();
        Item currentLast = this.tail.item;
        count--;

        //the last one
        if (isEmpty()) {
            this.tail = null;
            this.first = null;
        } else {
            this.tail = this.tail.prev;
            this.tail.next = null;
        }

        return currentLast;
    }

    private void validateList(){
        if (isEmpty()) {
            throw new NoSuchElementException("deque is empty");
        }
    }

    private void validateItem(Item item){
        if (item == null) {
            throw new IllegalArgumentException("addlast args is null");
        }
    }


    //return an iterator over items in order from front to back
    public Iterator<Item> iterator() {

        return new Iterator<Item>() {
            Node current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                if (hasNext()) {
                    Node n = current;
                    current = current.next;
                    return n.item;
                }
                throw new NoSuchElementException("no next item");
            }
        };

    }

    // unit testing
    public static void main(String[] args) {

        Deque<String> dq = new Deque<>();

    }

}
