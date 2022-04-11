public class queue {
    
    private node curr;
    private node last;
    private node head;
    private int currentMarker = 0;
    private int nextMarker = 0;

    // contructor to initialize queue with empty head node
    queue(){
        head = new node();
        last = head;
    }

    // add node to queue
    public void enqueue(node newCurr){
        nextMarker++;
        last.setNext(newCurr);
        last = newCurr;
    }

    // remove first node and decrement nodecount to track when the end of a run is reached
    public void deQueue(){
        --currentMarker;
        head = head.getNext();
    }

    // set to start of list at the head
    public node getHead(){
        return head.getNext();
    }

    // return current marker
    public int getMarker(){
        return currentMarker;
    }

    // set current marker = to next marker
    public void setMarker(){
        currentMarker = nextMarker;
        nextMarker = 0;
    }

    // print full list in queue
    public void printQueue(){
        curr = head.getNext();
        while (curr != null)
        {
            System.out.println(curr.getData() + " ");
            curr = curr.getNext();
        }
    }
}
