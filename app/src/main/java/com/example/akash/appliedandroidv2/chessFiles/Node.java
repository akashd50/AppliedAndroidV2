/**
 * Created by Akash on 1/18/2018.
 */
package com.example.akash.appliedandroidv2.chessFiles;

public class Node {
    private GameObject object;
    private Node next;

    public Node(GameObject o){
        object = o;
        this.next = null;
    }

    public Node getNext(){
        return this.next;
    }

    public void setNext(Node n){
        this.next = n;
    }

    public void setObject(GameObject o){
        this.object = o;
    }

    public GameObject getObject(){
        return this.object;
    }

    public String toString(){
        return this.object.toString();
    }
}
