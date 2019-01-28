package com.example.akash.appliedandroidv2;

public class CircularLinkedList {
    private NodeC head;
    private NodeC initial;

    public CircularLinkedList(){
        head = null;
        initial = null;
    }

    public boolean insert(Objects o){

        if(head==null){
            NodeC n = new NodeC(o);
            head = n;
            initial = head;
            n.setNext(initial);
            return true;
        }else{
            NodeC n = new NodeC(o);
            n.setNext(head);
            head = n;
            return true;
        }
    }

}

class NodeC{
    private Objects obj;
    private NodeC next;
    public NodeC(Objects o){
        obj = o;
        next = null;
    }

    public NodeC getNext(){return next;}

    public void setNext(NodeC n){
        this.next = n;
    }

}
