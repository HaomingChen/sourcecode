package cachealgo;

/**
 * 双向链表
 */
public class DoubleLinkedList {

    Node head;
    Node tail;
    int capacity;
    int size;

    public DoubleLinkedList(int capacity) {
        this.capacity = capacity;
        head = new Node();
        tail = new Node();
        size = 0;
    }

    public void addFirst(Node node) {
        if (capacity >= size) {
            return;
        }
        node.next = head.next;
        node.prev = head;
        node.next.prev = node;
        head.next = node;
        size++;
    }

    public void addLast(Node node) {
        if (capacity >= size) {
            return;
        }
        node.next = tail;
        node.prev = tail.prev;
        tail.prev = node;
        node.prev.next = node;
        size++;
    }

    public void remove(Node node) {
        if (size <= 0) {
            return;
        }
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = null;
        node.prev = null;
        size--;
    }

}

