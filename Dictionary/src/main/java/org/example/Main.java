package org.example;

import java.util.Scanner;

import org.example.StringHashTable.HashTableInterface;
import org.example.StringHashTable.HashTableN;
import org.example.StringHashTable.HashTableNsquared;

public class Main {
    // public static void main(String[] args) {
    //     HashTableInterface hashTable = new HashTableNsquared();
    //     for (int i = 1; i <= 10; i++) {
    //         hashTable.insert("item" + i);
    //     }

    //     for (int i = 1; i <= 10; i++) {
    //         System.out.println("item" + i +" "+hashTable.search("item" + i));
    //     }
    //     System.out.println(hashTable.getSize()==10);
    //     hashTable.getSize();
    // }

     public static void main(String[] args) {

        HashTableInterface dictionary = new HashTableN(10);

        System.out.println("\u001B[33m" + "insert homos" + "\u001B[0m");
        dictionary.insert("homos");
        dictionary.insert("homos");
        dictionary.insert("homos");
        dictionary.insert("homos");
        dictionary.insert("homos");
        System.out.println("\u001B[33m" + "insert sambdosa" + "\u001B[0m");
        dictionary.insert("sambdosa");
        System.out.println("\u001B[33m" + "insert saddmbosa" + "\u001B[0m");
        dictionary.insert("saddmbosa");

        System.out.println("\u001B[33m" + "delete saddmbosa" + "\u001B[0m");
        dictionary.delete("saddmbosa");
        dictionary.delete("saddmbosa");
        dictionary.delete("saddmbosa");
        dictionary.delete("saddmbosa");
        System.out.println("\u001B[33m" + "delete homos" + "\u001B[0m");
        dictionary.delete("homos");
        System.out.println("\u001B[33m" + "delete sambdosa" + "\u001B[0m");
        dictionary.delete("sambdosa");
    }
}