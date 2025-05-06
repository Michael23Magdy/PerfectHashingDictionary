package org.example;

import java.io.IOException;
import java.util.List;

import org.example.FileParser.FileParser;
import org.example.StringHashTable.HashTableInterface;

public class DictionaryTimeDecorator implements DictionaryInterface{
    private DictionaryInterface dictionary;
    
    public DictionaryTimeDecorator(DictionaryInterface dictionary){
        this.dictionary = dictionary;
    }

    @Override
    public Boolean search(String str) {
        long startTime = System.nanoTime();
        Boolean result = dictionary.search(str);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        System.out.println("Time taken to execute search operation: " + executionTime + " nanoseconds.");
        return result;
    }

    @Override
    public Boolean insert(String str) {
        long startTime = System.nanoTime();
        Boolean result = dictionary.insert(str);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        System.out.println("Time taken to execute insert operation: " + executionTime + " nanoseconds.");
        return result;
    }

    @Override
    public Boolean delete(String str) {
        long startTime = System.nanoTime();
        Boolean result = dictionary.delete(str);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        System.out.println("Time taken to execute delete operation: " + executionTime + " nanoseconds.");
        return result;
    }

    @Override
    public int batchInsert(String path) {
        long startTime = System.nanoTime();
        int result = dictionary.batchInsert(path);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        System.out.println("Time taken to execute batch insert operation: " + executionTime + " nanoseconds.");
        return result;
    }

    @Override
    public int batchDelete(String path) {
        long startTime = System.nanoTime();
        int result = dictionary.batchDelete(path);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        System.out.println("Time taken to execute batch delete operation: " + executionTime + " nanoseconds.");
        return result;
    }

    public int getSize(){
        return dictionary.getSize();
    }

    @Override
    public void printTable() {
        dictionary.printTable();
    }
}