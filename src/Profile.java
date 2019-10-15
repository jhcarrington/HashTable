/**
 * Filename:   Profile.java
 * Project:    p3
 * Authors:    Jason Carrington, Cristian Espinoza. Lecture 001
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   10/29/2018
 * Version:    1.0
 * 
 * Credits:    None
 * 
 * Bugs:       None
 */
import java.util.ArrayList;
import java.util.TreeMap;

public class Profile<K extends Comparable<K>, V> {

    HashTableADT<K, V> hashtable;
    TreeMap<K, V> treemap;
    
    /**
     * Constructor that instantiates the hashTable and treemap
     */
    public Profile() {
        hashtable = new HashTable<K, V>();
        treemap = new TreeMap<K, V>();
    }
    
    /**
     * Inserts the value of value at key into both the hashTable and treemap
     * 
     * @param key the key of the value that we are inserting
     * @param value the data that is associated with the key we are inserting
     */
    public void insert(K key, V value) {
        hashtable.put(key, value);
        treemap.put(key, value);
    }
    
    /**
     * Removes the value at key from both the hashTable and treemap
     * 
     * @param key the key that we are removing
     */
    public void retrieve(K key) {
        hashtable.get(key);
        treemap.get(key);
    }
    
    /**
     * Main method that inserts and removes values based on args
     * 
     * @param args <num_elements> the number of elements that we want inserted and removed
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Expected 1 argument: <num_elements>");
            System.exit(1);
        }
        int numElements = Integer.parseInt(args[0]);
        Profile<Integer, Integer> profile = new Profile<Integer, Integer>();
        for(int i = 0; i < numElements; i++) {
            profile.insert(i, i);
            profile.retrieve(i);
        }
        
        String msg = String.format("Successfully inserted and retreived %d elements into the hash table and treemap", numElements);
        System.out.println(msg);
    }
}