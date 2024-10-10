package DataStructure;
import java.util.Iterator;

public class LinkedList<E> implements Iterable<E> {

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node current = Head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E data = current.data;
                current = current.next;
                return data;
            }
        };
    } // For each loop purpose

    class Node {
        E data;
        Node next;
        Node prev;

        public Node(E data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    Node Head = null;

    void addFirst(E data){
        Node n = new Node(data);
        if (Head == null){
            Head = n;
        }else{
            n.next = Head;
            Head.prev = n;
            Head = n;
        }
    }

    void addLast(E data){
        Node n = new Node(data);
        if(Head == null){
            Head = n;
        }else {
            Node temp=Head;
            while(temp.next != null){
                temp=temp.next;
            }
            temp.next=n;
            n.prev=temp;
        }
    }

    void display(){
        Node temp = Head;
        while(temp != null){
            System.out.print(temp.data+" -> ");
            temp=temp.next;
        }
        System.out.println("null");
    }

    void displayRev(){
        Node temp = Head;
        while(temp.next != null){
            temp=temp.next;
        }
        System.out.print("null");
        while (temp != null){
            System.out.print(" <- "+temp.data);
            temp=temp.prev;
        }
        System.out.println();
    }

    void deleteFirst(){
        if(Head ==  null){
            System.out.println("DLL's empty!");
        }else{
            if (Head.next == null){
                Head = null;
            }else{
                Head=Head.next;
                Head.prev=null;
            }
        }
    }

    boolean isExist(E val){
        Node temp=Head;
        while(temp != null){
            if (temp.data == val)
                return true;
            temp=temp.next;
        }
        return false;
    }

    void deleteParticular(E val) {
        if (Head == null) return;
        if (Head.data.equals(val)) {
            Head = Head.next;
            if (Head != null) {
                Head.prev = null;
            }
            return;
        }
        Node temp = Head.next;
        while (temp != null) {
            if (temp.data.equals(val)) {
                if (temp.next != null) {
                    temp.next.prev = temp.prev;
                }
                temp.prev.next = temp.next;
                return;
            }
            temp = temp.next;
        }
    }

    void deleteLast(){
        Node temp = Head;
        while(temp.next.next != null){
            temp=temp.next;
        }
        temp.next.prev=null;
        temp.next=null;
    }

    int size(){
        int count = 0;
        Node temp = Head;
        while (temp != null){
            count++;
            temp = temp.next;
        }
        return count;
    }

    void addAfter(E data,E val){
        if (!isExist(val)){
            System.out.println("Value not exist");
        }else{
            Node temp = Head;
            Node n=new Node(data);
            while (temp.data != val) {
                temp = temp.next;
            }
            if (temp.next == null){
                temp.next=n;
                n.prev=temp;
            }else{
                Node x = temp.next;
                temp.next = n;
                n.next = x;
                x.prev=n;
                n.prev=temp;
            }
        }
    }

    void addBefore(E data,E val){
        if (!isExist(val)){
            System.out.println("Value not exist");
        }else{
            Node temp = Head;
            Node n=new Node(data);
            while (true){
                if (temp.data == val){
                    break;
                }
                temp=temp.next;
            }
            if (temp.prev == null){
                temp.prev=n;
                n.next=temp;
                Head=n;
            }else{
                Node x = temp.prev;
                x.next = n;
                n.next = temp;
                temp.prev=n;
                n.prev=x;
            }
        }
    }

    void addBetween(E data,E val1,E val2){
        if (Head == null || Head.next == null || Head.next.next == null){
            System.out.println("Can't added");
            return;
        }
        boolean isExist = false;
        Node temp = Head;
        while (temp.next.next != null){
            if ((temp.data == val1 && temp.next.data==val2) || (temp.data == val2 && temp.next.data == val1)){
                isExist=true;
                break;
            }
            temp=temp.next;
        }
        if (isExist){
            Node n=new Node(data);
            Node x = temp.next;
            temp.next = n;
            n.next = x;
            x.prev=n;
            n.prev =temp;
        }else {
            System.out.println("Not found");
        }
    }
}
