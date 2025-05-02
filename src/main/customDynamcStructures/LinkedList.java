package main.customDynamcStructures;

public class LinkedList<T> {
    private Node<T> head;
    private int size;

    private static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public void add(T element) {
        Node<T> newNode = new Node<>(element);

        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }

        size++;
    }


    public void add(T element, int id) {
        add(element);
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new Error("indexut e izwyn goleminata");
        }

        T removedElement;

        if (index == 0) {
            removedElement = head.data;
            head = head.next;
        } else {
            Node<T> current = head;

            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }

            removedElement = current.next.data;
            current.next = current.next.next;
        }

        size--;
        return removedElement;
    }

    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new Error("indexut e izwyn goleminata");
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        current.data = element;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new Error("indexut e izwyn goleminata");
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    @Override
    public String toString() {
        String res="[";
        Node<T> curr=head;

        while(curr!=null){
            res = res+curr.data;

            if(curr.next!=null) {
                res=res+", "; }
            curr= curr.next; }
        res=res+"]";
        return res;
    }
}