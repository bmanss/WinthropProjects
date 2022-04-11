import java.util.*;
import java.io.*;
public class sortit {

    public static void main(String[] args) throws FileNotFoundException{

        // main queues
        queue list0 = new queue();
        queue list1 = new queue();

        // tracker queues 
        queue currentList = list0;
        queue otherList = list1;
        queue temp;

        // sorting variables 
        String prevData = "";
        boolean sorting = true;

        Scanner fileRead;
        File inputFiles;

        // read in files, exit if one fails to load
        for (int i = 0; i < args.length; ++i){
            if (args[i].equals("-")){
                fileRead = new Scanner(System.in);
            }
            else{
                inputFiles = new File(args[i]);
                if (!inputFiles.exists()){
                    System.out.println("File: " + inputFiles + " Not Found");
                    return;
                }
                fileRead = new Scanner(inputFiles);
            }
            while (fileRead.hasNextLine()){
                node nextline = new node(fileRead.nextLine());
                if (nextline.getData().compareTo(prevData) < 0) {
                    temp = currentList;
                    currentList = otherList;
                    otherList = temp;
                }
                currentList.enqueue(nextline);
                prevData = nextline.getData();
            }
        }     
        // set markers
        currentList.setMarker();
        otherList.setMarker();

        while(sorting){

            // if both list are exhausted move the markers 
            if (currentList.getMarker() == 0 && otherList.getMarker() == 0){
                currentList.setMarker();
                otherList.setMarker();
                prevData = "";
                
                // stop sorting if other list is null after checking list when they were just exhausted
                if (otherList.getHead() == null)
                    sorting = false;
            }
            // else if only currentlist is exhausted pull data from other list
            else if (currentList.getMarker() == 0){
                if (otherList.getHead().getData().compareTo(prevData) >= 0){
                    prevData = otherList.getHead().getData();
                    currentList.enqueue(new node(prevData));
                    otherList.deQueue();
                }
                else{
                    // swap lists
                    temp = currentList;
                    currentList = otherList;
                    otherList = temp;

                    prevData = currentList.getHead().getData();
                    currentList.enqueue(new node(prevData));
                    currentList.deQueue();
                }
            }
            // else if only the other list is exhausted pull data from the current list
            else if (otherList.getMarker() == 0){
                if (currentList.getHead().getData().compareTo(prevData) >= 0){
                    prevData = currentList.getHead().getData();
                    currentList.enqueue(new node(prevData));
                    currentList.deQueue();
                }
                else{
                    prevData = currentList.getHead().getData();

                    //swap lists
                    temp = currentList;
                    currentList = otherList;
                    otherList = temp;

                    currentList.enqueue(new node(prevData));
                    otherList.deQueue();
                }
            }
            while (currentList.getMarker() != 0 && otherList.getMarker() != 0){ 

                //if both data pieces are smaller than target
                if (currentList.getHead().getData().compareTo(prevData) < 0 && otherList.getHead().getData().compareTo(prevData) < 0){

                    // swap lists
                    temp = currentList;
                    currentList = otherList;
                    otherList = temp;
    
                    //if current head is smaller than other head
                    if(currentList.getHead().getData().compareTo(otherList.getHead().getData()) < 0){
                        prevData = currentList.getHead().getData();
                        currentList.enqueue(new node(prevData));
                        currentList.deQueue();
                    }
                    else{
                        prevData = otherList.getHead().getData();
                        currentList.enqueue(new node(prevData));
                        otherList.deQueue();
                    }
                }
                // if both data pieces are larger than target take smaller
                else if (currentList.getHead().getData().compareTo(prevData) >= 0 && otherList.getHead().getData().compareTo(prevData) >= 0){
                    if(currentList.getHead().getData().compareTo(otherList.getHead().getData()) < 0){
                        prevData = currentList.getHead().getData();
                        currentList.enqueue(new node(prevData));
                        currentList.deQueue();
                    }
                    else{
                        prevData = otherList.getHead().getData();
                        currentList.enqueue(new node(prevData));
                        otherList.deQueue();
                    }
                }
                // else take larger data and enqueue
                else{
                    if(currentList.getHead().getData().compareTo(otherList.getHead().getData()) >= 0){
                        prevData = currentList.getHead().getData();
                        currentList.enqueue(new node(prevData));
                        currentList.deQueue();
                    }
                    else{
                        prevData = otherList.getHead().getData();
                        currentList.enqueue(new node(prevData));
                        otherList.deQueue();
                    }
                }
            }      
        }
        
        currentList.printQueue();
        System.out.println();
        return;
    }
}