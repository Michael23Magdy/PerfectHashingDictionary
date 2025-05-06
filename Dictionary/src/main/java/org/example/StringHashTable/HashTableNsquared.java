package org.example.StringHashTable;

import java.util.Arrays;

public class HashTableNsquared implements HashTableInterface {
    final int initialSize = 100;
    private int currentPrime;
    private PrimeGenerator primeGenerator;
    private String[] table; 
    private int size;
    private int noRehashes;

    public void printTable() {
        for (int i = 0; i < table.length; i++) {
            if(table[i] !=  null){
                System.out.println(table[i]+" -> "+i);
            }
        }
        System.out.println("------------------------");
    }

    public HashTableNsquared(){
        System.out.println("Starting hash table n^2 space...");
        this.primeGenerator = PrimeGenerator.getInstance();
        this.table = new String[initialSize];
        this.size = 0;
        this.noRehashes = 0;
        this.currentPrime = primeGenerator.getRandomPrime();
    }

    private String[] hashtest(int key, int newSize){
        this.noRehashes++;
        String[] newTable = new String[newSize];
        for (String string : table) {
            if(string != null){
                int newHashIndex = StringHasher.hash(string, key, newSize);
                if(newTable[newHashIndex] != null) 
                    return null;
                newTable[newHashIndex] = string;
            }
        }
        return newTable;
    }

    private void rehash(int newSize){
        String[] newTable;
        int newPrime;
        do{
            newPrime = primeGenerator.getRandomPrime();
            newTable = hashtest(newPrime, newSize);
        } while(newTable == null);
        table = Arrays.copyOf(newTable, newTable.length);
        currentPrime = newPrime;
    }

    @Override
    public Boolean search(String str) {
        if(str == null) 
            return false;
        int hashIndex = StringHasher.hash(str, currentPrime, table.length);
        return str.equals(table[hashIndex]);
    }

    @Override
    public void insert(String str) {
        int hashIndex = StringHasher.hash(str, currentPrime, table.length);
        if(str.equals(table[hashIndex])) 
            return;
        
        if(size*size >= table.length){
            rehash(table.length * 4);
            hashIndex = StringHasher.hash(str, currentPrime, table.length);
        }
        int noRehashesBefore = this.noRehashes;
        while(table[hashIndex] != null){
            if(this.noRehashes - noRehashesBefore > 10){
                throw new RuntimeException("Rehashing failed after multiple attempts. Unable to insert: " + str);
            }
            rehash(table.length);
            hashIndex = StringHasher.hash(str, currentPrime, table.length);
        }
        table[hashIndex] = str;
        size++;
    }

    @Override
    public void delete(String str) {
        if(str == null)
            return;
        int deletionIndex = StringHasher.hash(str, currentPrime, table.length);
        if(table[deletionIndex] == null) return;
        table[deletionIndex] = null;
        size--;
    }

    @Override
    public int getSize(){
        return size;
    }
    public int getNoRehashes(){ return noRehashes; }
}
