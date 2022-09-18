/*
* AUTHOR: Victoria Santos
* FILE: MaxPQ.java
* ASSIGNMENT: PA10 WikiRacer
* COURSE: CSc 210 001; Spring 2022
* PURPOSE: This program is meant to simulate the online game
* WikiRacer, which challenges users to make it from one
* Wikipedia page to another solely through the current
* page's hrefs. I operates in conjunction with two
* other files, MaxPQ and WikiScraper. Altogether, the
* program works by fetching and scraping HTMLs, managing 
* a max priority queue of "ladders", and making informed
* choices about which link might lead to the end page.
* This file is responsible for creating and operating
* the max priority queue.
*/


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class MaxPQ {


    private Ladder[] ladderQueue;
    private static final int DEFAULT_CAPACITY = 100;
    private int size;
    private int currOpenIndex;


    /**
     * Constructor for ladderQueue objects. Initializes queue
     * to the default capacity of 10, starts the size at 0 (as
     * there are 0 ladders in the starting queue), and the
     * currOpenIndex at 1 (next open index for objects).
     * 
     * @param none
     * 
     * @return none
     */
    public MaxPQ() {
    	ladderQueue = new Ladder[DEFAULT_CAPACITY];
        size = 0;
        currOpenIndex = 1;
    }

    
    /**
     * Responsible for enqueuing objects to the ladderQueue,
     * given a ladder and priority. This creates the ladder
     * object for the user and passes it to the main
     * enqueue function.
     * 
     * @param name,
     *            a given ladder as a List<String>
     * @param priority,
     *            the associated priority as an int
     * 
     * @return none
     */
    public void enqueue(List<String> ladder, int priority) {
        Ladder newLadder = new Ladder(ladder, priority);
        enqueue(newLadder);
    }


    /**
     * Responsible for enqueuing Ladder objects to the
     * ladderQueue. It adds the ladder to the end of
     * the queue, then calls the bubbleUp helper function
     * to maintain heap ordering. If the queue is too
     * small before enqueuing, it is extended with the
     * helper function.
     * 
     * @param ladder,
     *            a Ladder object
     * 
     * @return none
     */
    public void enqueue(Ladder ladder) {
        if (currOpenIndex == ladderQueue.length) {
            extendArray();
        }

        // check
        ladderQueue[currOpenIndex] = ladder;
        int currIndex = currOpenIndex;

        if (currOpenIndex > 1) {
            bubbleUp(ladder, currIndex);
        }

        size++;
        currOpenIndex++;
    }

    
    /**
     * Responsible for extending the ladderQueue
     * array. The extended queue will be twice the
     * queue's previous capacity and will still
     * contain all of the existing elements in
     * proper order.
     * 
     * @param none
     * 
     * @return none
     */
    private void extendArray() {
        Ladder[] extendedQueue = new Ladder[ladderQueue.length * 2];

        for (int i = 0; i < ladderQueue.length; i++) {
            extendedQueue[i] = ladderQueue[i];
        }

        ladderQueue = extendedQueue;
    }


    /**
     * Responsible for maintaining heap ordering once
     * a ladder is added to the queue. If the ladder's
     * priority is more urgent than its parent
     * priorities, it is switched. If two patients have the
     * same priority, the one with the lexicographically
     * smaller name should become the parent.
     * 
     * @param patient,
     *            a Ladder object
     * @param currIndex,
     *            the current index of the enqueued object
     * 
     * @return none
     */
    private void bubbleUp(Ladder ladder, int currIndex) {
    	Ladder parent = ladderQueue[currIndex / 2];

        while (ladder.priority >= parent.priority) {

            if (ladder.priority == parent.priority) {
                if (ladder.ladder.equals(parent.ladder)) {
                    break;
                }
            }

            Ladder temp = parent;
            ladderQueue[currIndex / 2] = ladder;
            ladderQueue[currIndex] = temp;
            currIndex = currIndex / 2;
            parent = ladderQueue[currIndex / 2];

            if (currIndex <= 1) {
                break;
            }
        }
    }


    /**
     * Responsible for dequeuing Ladder objects from the
     * ladderQueue. It removes the front ladder from
     * the queue, then calls the bubbleDown helper function
     * to maintain heap ordering.
     * 
     * @param none
     * 
     * @return ladder of dequeued object as a List<String>
     */
    public List<String> dequeue() {
        Ladder temp = ladderQueue[1];
        ladderQueue[1] = ladderQueue[size];
        ladderQueue[size] = null;
        int currIndex = 1;
        
        if (size > 1) { 
        	bubbleDown(currIndex);
        }

        size--;
        currOpenIndex--;
        return temp.ladder;
    }


    /**
     * Responsible for maintaining heap ordering once
     * a ladder is removed from the queue. If the new front
     * ladder's priority is less urgent than either of its child
     * priorities, it is switched with the more urgent one.
     * 
     * @param currIndex,
     *            the current index of the new front object
     * 
     * @return none
     */
    private void bubbleDown(int currIndex) {
    	Ladder parent = ladderQueue[currIndex];
        Ladder child1 = ladderQueue[currIndex * 2];
        Ladder child2 = ladderQueue[currIndex * 2 + 1];
        
        while (parent.priority <= child1.priority
                || parent.priority <= child2.priority) {
        	
            if (parent.priority < child1.priority) {
            	ladderQueue[currIndex] = child1;
            	ladderQueue[currIndex * 2] = parent;
                currIndex = currIndex * 2;
            } else {
            	ladderQueue[currIndex] = child2;
            	ladderQueue[currIndex * 2 + 1] = parent;
                currIndex = currIndex * 2 + 1;
            }

            if (currIndex * 2 + 1 >= size) {
                break;
            }
            parent = ladderQueue[currIndex];
            child1 = ladderQueue[currIndex * 2];
            child2 = ladderQueue[currIndex * 2 + 1];
        }
    }


    /**
     * Returns true if the queue has no ladders,
     * and false otherwise.
     * 
     * @param none
     * 
     * @return boolean true or false
     * 
     */
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * Returns the queue as a String, with each ladder
     *  and priority listed in proper heap order.
     * 
     * @param none
     * 
     * @return queue as a String
     * 
     */
    @Override
    public String toString() {
        String queueString = "";

        if (size != 0) {
            queueString += "{";

            for (int i = 1; i < size; i++) {
                queueString += ladderQueue[i].toString();
                queueString += ", ";
            }

            queueString += ladderQueue[currOpenIndex - 1];
            queueString += "}";
        }

        return queueString;
    }
    

    /*
    * An inner class for creating and printing Ladder objects.
    */
	private class Ladder {
	
		
	    private List<String> ladder;
	    private int priority;
	
	    
	    /**
	     * Constructor for Ladder objects
	     * 
	     * @param givenLadder, a List<String> ladder
	     * 
	     * @param givenPriority, the ladder priority as an int
	     * 
	     * @return ladder object as a String
	     */
	    private Ladder(List<String> givenLadder, int givenPriority) {
	        ladder = givenLadder;
	        priority = givenPriority;
	    }
	
	    
	    /**
	     * Overriding toString method for printing Ladder objects
	     * 
	     * @param none
	     * 
	     * @return ladder object as a String
	     */
	    @Override
		public String toString() {
			return ladder.toString() + " (" + priority + ")";
		}
	
	    
	}
	
}
