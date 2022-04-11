public class node {

    private String data;
    private node next;

    // Constructors
    node(){
        data = "";
        next = null;
    }

    // constructor to initialize new node with data
    node(String newData){
        data = newData;
        next = null;
    }

    // method to return node contents
    public String getData(){
        return data;
    }

    // method to return next node
    public node getNext(){
        return next;
    }

    // method to set next node
    public void setNext(node newNext){
        next = newNext;
    }
}
