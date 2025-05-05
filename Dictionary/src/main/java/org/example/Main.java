package org.example;

import org.example.StringHashTable.HashTableInterface;
import org.example.StringHashTable.HashTableNsquared;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        HashTableInterface hashTable = new HashTableNsquared();
        hashTable.insert("mina1");
        hashTable.insert("mina2");
        hashTable.insert("mina3");
        hashTable.insert("mina4");
        hashTable.insert("mina5");
        hashTable.insert("mina6");
        hashTable.insert("mina7");
        hashTable.insert("mina8");
        hashTable.insert("mina9");
        hashTable.insert("mina10");
        hashTable.insert("mina11");

        System.out.println(hashTable.search("mina11"));
    }
}