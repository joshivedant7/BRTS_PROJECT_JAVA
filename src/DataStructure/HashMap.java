package DataStructure;

public class HashMap<K,V>{
    LinkedList<K> key;
    LinkedList<V> value;

    public HashMap(){
        key = new LinkedList<>();
        value = new LinkedList<>();
    }

    public void clear(){
        key.Head = null;
        value.Head = null;
    }
    public LinkedList<V> values(){
        return value;
    }
    public boolean containsKey(K key){
        LinkedList<K>.Node temp = this.key.Head;
        while(temp != null){
            if (temp.data == key){
                return true;
            }
            temp = temp.next;
        }
        return false;
    }
    public int size(){
        return key.size();
    }
    public boolean containsValue(V value){
        LinkedList<V>.Node temp = this.value.Head;
        while(temp != null){
            if (temp.data == value){
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public V get(K key){
        LinkedList<K>.Node temp1 = this.key.Head;
        LinkedList<V>.Node temp2 = this.value.Head;
        while(temp1 != null){
            if (temp1.data == key){
                return temp2.data;
            }
            temp1 = temp1.next;
            temp2 = temp2.next;
        }
        return null;
    }

    public boolean isEmpty(){
        return key.Head == null;
    }

    public void put(K key,V value){
        if (containsKey(key)){
            LinkedList<K>.Node temp1 = this.key.Head;
            LinkedList<V>.Node temp2 = this.value.Head;
            while(temp1 != null){
                if (temp1.data == key){
                    temp2.data = value;
                    break;
                }
                temp1 = temp1.next;
                temp2 = temp2.next;
            }
        }else {
            this.key.addLast(key);
            this.value.addLast(value);
        }
    }
    public V remove(K key){
        if (containsKey(key)){
            V r = null;
            LinkedList<K>.Node temp1 = this.key.Head;
            LinkedList<V>.Node temp2 = this.value.Head;
            while(temp1 != null){
                if (temp1.data == key){
                    r = temp2.data;
                    this.key.deleteParticular(temp1.data);
                    this.value.deleteParticular(temp2.data);
                    break;
                }
                temp1 = temp1.next;
                temp2 = temp2.next;
            }
            return r;
        }else return null;
    }
    void display(){
        LinkedList<K>.Node temp1 = this.key.Head;
        LinkedList<V>.Node temp2 = this.value.Head;
        while(temp1 != null){
            System.out.println("{Key : "+temp1.data+", data : "+temp2.data+"}");
            temp1=temp1.next;
            temp2=temp2.next;
        }
    }

    public V replace(K key,V value){
        if (containsKey(key)){
            LinkedList<K>.Node temp1 = this.key.Head;
            LinkedList<V>.Node temp2 = this.value.Head;
            while(temp1 != null){
                if (temp1.data == key){
                    temp2.data = value;
                    break;
                }
                temp1 = temp1.next;
                temp2 = temp2.next;
            }
            return temp2.data;
        }else return null;
    }
}
