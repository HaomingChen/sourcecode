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
        head.next = tail;
        tail.prev = head;
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

    public int delHead() {
        if (size <= 0) {
            return -1;
        }
        return remove(head.next);
    }

    public int delTail() {
        if (size <= 0) {
            return -1;
        }
        return remove(tail.prev);
    }

    public int remove(Node node) {
        if (size <= 0) {
            return -1;
        }
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = null;
        node.prev = null;
        size--;
        return node.val;
    }

    //弹出头部节点
    public int pop() {
        return delHead();
    }

    //添加节点
    public int append(Node node) {
        addLast(node);
        return node.val;
    }

    //头部添加节点
    public int appendFront(Node node) {
        return append(node);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node pt = head.next;
        while (pt != null) {
            sb.append(pt.val + ", ");
            pt = pt.next;
        }
        return sb.toString().substring(0, sb.length() - 1) + "]";
    }
}

