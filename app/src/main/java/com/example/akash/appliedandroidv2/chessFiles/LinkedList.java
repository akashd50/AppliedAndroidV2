/**
 * COMP 2150 : Assignment 1
 *
 * Name:- Akashdeep Singh
 * Student:- 7802937
 *
 * Class:- LinkedList.java
 *
 * This class provide a LinkedList implementation for storing, accessing and deleting MarketObjects.
 */
package com.example.akash.appliedandroidv2.chessFiles;

public class LinkedList {
    //variables for List
    private Node head;
    private Node last;
    private Node traverse;
    private int length;

    public LinkedList(){
        head = null;
        last = null;
        traverse = null;
    }//constructor

    public boolean insert(GameObject o){

        Node n = new Node(o); //new Node
        if (head==null){
            n.setNext(last);
            last = n;
            head = n;
            length++;
            return true;
        }//if head is null
        else{
            last.setNext(n);
            last = n;
            length++;
            return true;
        }//else
    }//insert()

    public GameObject contains(GameObject m){

        /*This method returns true if an object is found in the List else false.
        * Uses polymorphism to call the methods associated with mentioned Object for comparison.
         */
        Node curr = head;
        boolean found = false;

        while(curr!=null && !found){
            if(curr.getObject().compareTo(m)){
                    //m.getDestX() == curr.getObject().getDestX() && m.getDestY()==curr.getObject().getDestY()){
                found=true;
                return curr.getObject();
            }
            curr=curr.getNext();
        }
        return null;
    }//search()

    public GameObject startTraversal(){
        /*
        * This method starts the traversal for a LinkedList and returns an Object if
        * there's any, else returns null
         */
        traverse = head;
        if(head == null){
            return null;
        }
        else return head.getObject();
    }//startTraversal()

    public GameObject traverse(){
        /*
        * this method traverses the Traverse node to the next Node in the List and returns
        * the result
         */
        traverse = traverse.getNext();
        if(traverse == null){
            return null;
        }
        else return traverse.getObject();
    }//traverse()

    public boolean remove(GameObject o){
        /*
        * this method traverses the List and removes the object from the List.
         */
        Node curr = head;
        Node prev = null;

        while(curr!=null){ //while the list is not empty
            if(o.compareTo(curr.getObject()))
            {//if the compared Objects matches
                if(curr==head)
                {//if it's the first node
                    head = curr.getNext();
                    last = head;
                    return true;
                }//if
                else if(curr.getNext()==null)
                {//else if it's the last Node
                    prev.setNext(curr.getNext());
                    last = prev;
                   return true;
                }//else if
                else
                {//anything in the Middle
                    prev.setNext(curr.getNext());
                    return true;
                }//else
            }//if
            prev = curr;
            curr = curr.getNext();
        }//while
        return false;
    }//remove()*/

    public int getLength() {
        return this.length;
    }

    public String toString(){
        Node curr = head;
        String result="";
        while(curr!=null){
            result+=curr.toString()+"\n";
            curr = curr.getNext();
        }
        return result;
    }//toString()
}
