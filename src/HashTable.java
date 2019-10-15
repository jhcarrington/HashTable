/**
 * Filename: HashTable.java Project: p3 Authors: Jason Carrington 001, Cristian Espinoza 001
 *
 * Semester: Fall 2018 Course: CS400
 * 
 * Due Date: Pre-Tests: 10/19/2018
 * 
 * Credits: None
 * 
 * Bugs: None
 */


import java.util.NoSuchElementException;

/**
 * We used a bucket scheme to handle collisions. These buckets are part of a singly linked list. We
 * took the hashCode of the key and then took the mod of it to the table size that way when the
 * table size changes the values arn't still at the beginning of the hashTable and they are equally
 * spread out
 * 
 * @author Jason, Cristian
 *
 * @param <K> the key object
 * @param <V> the value object
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {
    /**
     * Each element of the hashTable is a Bucket item
     * 
     * @author Jason, Cristian
     *
     * @param <K>
     * @param <V>
     */
    class Bucket<K, V> {

        private K key; // stores the key for each value
        private V value;// stores the data associated with each key
        private Bucket<K, V> next; // stores a reference to the next node of the linked list

        public Bucket(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public void setNext(Bucket<K, V> next) {
            this.next = next;
        }

        public Bucket<K, V> getNext() {
            return next;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public boolean hasNext() {
            if (next != null) {
                return true;
            }
            return false;
        }


    }

    private Bucket<K, V>[] hashTable;// stores the hash table
    private double loadFactor;// stores the load factor
    private int size;// stores the size of the table

    /**
     * Main constructor that has default values to start the hashTable with
     */
    public HashTable() {
        this.loadFactor = .75;
        hashTable = (Bucket<K, V>[]) new Bucket[11];
        size = 0;
    }

    /**
     * Overloaded constructor that takes in specific values to create a new hashTable
     * 
     * @param initialCapacity, the initial size of the hash table
     * @param loadFactor, the size it can go to before resizing.
     */
    public HashTable(int initialCapacity, double loadFactor) {
        this.loadFactor = loadFactor;
        hashTable = (Bucket<K, V>[]) new Bucket[initialCapacity];
        size = 0;
    }

    private int hashCode(K key) {
        return Math.abs(key.hashCode() % hashTable.length);
    }

    /**
     * Inserts the new object into the correct location or updates the value if the key is the same
     * 
     * @param key the specific key that you'r trying to compute
     * @param value the value that is trying to be added
     * @throws IllegalArgumentException thrown if the key is null
     */
    @Override
    public void put(K key, V value) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException();
        }

        if (value != null) {
            size++;
        }
        Bucket<K, V> next = hashTable[hashCode(key)];
        if (next != null) {
            int foundSame = 0;// 0 if it did not find the same key; 1 if it did
            while (next.hasNext()) {// iterates through the bucket
                if (next.getKey() == key) {
                    next.setValue(value);
                    foundSame = 1;// if we found the same key and updated it then we are done with
                                  // the put
                    break;
                }
                next = next.getNext();

            }
            if (foundSame == 0) {// if the same key was not found set the next bucket value to the
                                 // new one
                next.setNext(new Bucket<K, V>(key, value));
            }
        } else {
            hashTable[hashCode(key)] = new Bucket<K, V>(key, value);
        }
        if ((double) size / hashTable.length >= loadFactor) {
            resize();
        }
    }

    /**
     * resizes the hashTable
     */
    private void resize() {
        Bucket<K, V>[] oldHashTable = hashTable;
        hashTable = (Bucket<K, V>[]) new Bucket[hashTable.length * 2];// doubles the size
        rehash(oldHashTable);
    }

    /**
     * puts all the values from the old hash table into their new respective spot in the resized
     * table
     * 
     * @param oldHashTable the old hash table
     */
    private void rehash(Bucket<K, V>[] oldHashTable) {
        for (int i = 0; i < oldHashTable.length; i++) {
            if (oldHashTable[i] != null) {
                Bucket<K, V> next = oldHashTable[i];
                while (next.getNext() != null) {// iterates through the old hash table and updates
                                                // the new
                    this.put(next.getKey(), next.getValue());
                    next = next.getNext();
                }
                this.put(next.getKey(), next.getValue());
            }
        }
    }

    /**
     * gets the value at key or throws an exception
     * 
     * @param key the key that we are looking for
     * @throws IllegalArgumentException if the key value is null
     * @throws NoSuchElementException if the value at key was not found
     */
    @Override
    public V get(K key) throws IllegalArgumentException, NoSuchElementException {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = hashCode(key);// stores the generated hash code
        if (hashTable[hashCode] != null) {
            Bucket<K, V> next = hashTable[hashCode];
            while (next.getNext() != null) {// iterates to the end of the bucket
                if (next.getKey().equals(key)) {// looks for the value with the same key
                    return next.getValue();
                }
                next = next.getNext();
            }
            if (next.getKey().equals(key)) {// checks the last value for the same key
                return next.getValue();
            }
        }

        throw new NoSuchElementException();// no matching keys were found
    }

    /**
     * removes the specific key from the hash table
     * 
     * @param key, the key that you want removed
     * @throws IllegalArgumentException if the key is null
     * @throws NoSuchElementException if the key does not correspond to a value in the hash table
     */
    @Override
    public void remove(K key) throws IllegalArgumentException, NoSuchElementException {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = hashCode(key);// stores the hash code
        if (hashTable[hashCode] != null) {
            Bucket<K, V> next = hashTable[hashCode];// stores the current bucket we are looking at
            Bucket<K, V> previous = null;// stores the previous bucket before the one we are on
            while (next.getNext() != null) {// loops through the buckets until the end
                if (next.getKey().equals(key)) {
                    if (previous == null) {// if the bucket is the first one, then assign the root
                                           // to the next bucket
                        hashTable[hashCode] = next.getNext();
                    } else {
                        previous.setNext(next.getNext());// if bucket is not the first, assign the
                                                         // previous' next pointer to the next
                                                         // bucket after the removed one
                    }
                    size--;
                    return;
                }
                previous = next;
                next = next.getNext();
            }
            if (next.getKey().equals(key)) {
                if (previous == null) {
                    hashTable[hashCode] = next.getNext();
                } else {
                    previous.setNext(next.getNext());
                }
                size--;
                return;
            }
        }
        throw new NoSuchElementException();// thrown if it did not find a matching key in the above
                                           // code

    }

    /**
     * getter method for size
     * 
     * @return int, the size of the hashTable
     */
    @Override
    public int size() {
        return size;
    }

}
