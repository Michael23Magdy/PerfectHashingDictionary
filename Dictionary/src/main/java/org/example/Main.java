package org.example;

import org.example.StringHashTable.HashTableInterface;
import org.example.StringHashTable.HashTableNsquared;

public class Main {
    public static void main(String[] args) {
        HashTableInterface hashTable = new HashTableNsquared();
        for (int i = 1; i <= 10; i++) {
            hashTable.insert("item" + i);
        }

        for (int i = 1; i <= 10; i++) {
            System.out.println("item" + i +" "+hashTable.search("item" + i));
        }
        System.out.println(hashTable.getSize()==10);
        hashTable.getSize();
    }
}