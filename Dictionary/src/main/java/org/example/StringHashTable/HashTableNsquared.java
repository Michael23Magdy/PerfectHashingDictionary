package org.example.StringHashTable;

import java.util.Arrays;

public class HashTableNsquared implements HashTableInterface {
    final int intialSize = 100;
    private int currentPrime;
    private PrimeGenerator primeGenerator;
    private String[] table; 
    private int size;

    public HashTableNsquared(){
        this.primeGenerator = PrimeGenerator.getInstance();
        this.table = new String[intialSize];
        this.size = 0;
        this.currentPrime = primeGenerator.getRandomPrime();
    }

    private String[] hashtest(int key, int newSize){
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
        int tries = 0;
        while(table[hashIndex] != null){
            if(tries > 10){
                throw new RuntimeException("Rehashing failed after multiple attempts. Unable to insert: " + str);
            }
            rehash(table.length);
            hashIndex = StringHasher.hash(str, currentPrime, table.length);
            tries++;
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
}
