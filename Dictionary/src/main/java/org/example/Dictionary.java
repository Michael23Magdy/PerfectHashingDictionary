package org.example;

import java.io.IOException;
import java.util.List;

import org.example.FileParser.FileParser;
import org.example.StringHashTable.HashTableInterface;

public class Dictionary implements DictionaryInterface{
    private HashTableInterface hashTable;
    private FileParser fileParser;
    
    public Dictionary(HashTableInterface hashTable){
        this.hashTable = hashTable;
        this.fileParser = new FileParser();
    }

    @Override
    public Boolean search(String str) {
        return hashTable.search(str);
    }

    @Override
    public Boolean insert(String str) {
        if(hashTable.search(str)) return false;
        hashTable.insert(str);
        return true;
    }

    @Override
    public Boolean delete(String str) {
        if(!hashTable.search(str)) return false;
        hashTable.delete(str);
        return true;
    }

    @Override
    public int batchInsert(String path) {
        try {
            List<String> strings = fileParser.readFileContent(path);
            for(String string : strings){
                hashTable.insert(string);
            }
            return strings.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int batchDelete(String path) {
        try {
            List<String> strings = fileParser.readFileContent(path);
            for(String string : strings){
                hashTable.delete(string);
            }
            return strings.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSize(){
        return hashTable.getSize();
    }
}