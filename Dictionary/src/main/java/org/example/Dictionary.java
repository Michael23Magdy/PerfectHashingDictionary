package org.example;

import java.io.IOException;
import org.example.FileParser.FileParser;
import org.example.StringHashTable.HashTableInterface;

public class Dictionary implements DictionaryInterface{
    private HashTableInterface hashTable;
    private FileParser fileParser;
    
    public Dictionary(HashTableInterface hashTable){
        this.hashTable = hashTable;
    }

    @Override
    public Boolean search(String str) {
        return hashTable.search(str);
    }

    @Override
    public void insert(String str) {
        hashTable.insert(str);
    }

    @Override
    public void delete(String str) {
        hashTable.delete(str);
    }

    @Override
    public void batchInsert(String path) {
        try {
            for(String string : fileParser.readFileContent(path)){
                hashTable.insert(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bashDelete(String path) {
        try {
            for(String string : fileParser.readFileContent(path)){
                hashTable.delete(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printTable() {
        hashTable.printTable();
    }
}