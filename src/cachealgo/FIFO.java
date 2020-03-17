package cachealgo;

import java.util.HashMap;

public class FIFO {

    class Node {

        int key;
        int value;
        Node next;
        Node prev;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }

        public Node() {

        }

    }

    HashMap<Integer, Node> dic;
    int size;
    Node head;
    Node tail;
    int capacity;

    public FIFO(int capacity) {
        dic = new HashMap<>(capacity);
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
        this.capacity = capacity;
        size = 0;
    }

    public void put(int key, int value) {
        if (capacity >= size) {
            remove(head.next.key);
        }
        insert(key, value);
    }

    public int get(int key) {
        if (!dic.containsKey(key)) {
            return -1;
        }
        return dic.get(key).value;
    }

    private void remove(int key) {
        if (size <= 0 || !dic.containsKey(key)) {
            return;
        }
        Node node = dic.get(key);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    private void insert(int key, int value) {
        if (dic.containsKey(key)) {
            Node node = dic.get(key);
            node.value = value;
            return;
        }
        Node node = new Node(key, value);
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
        size++;
    }

}
