package org.example;

import java.util.Scanner;

import org.example.StringHashTable.HashTableInterface;
import org.example.StringHashTable.HashTableN;

public class Main {

     public static void main(String[] args) {

        HashTableInterface dictionary = new HashTableN(10);

        System.out.println("\u001B[33m" + "insert homos" + "\u001B[0m");
        dictionary.insert("homos");
        dictionary.insert("homos");
        dictionary.insert("homos");
        dictionary.insert("homos");
        dictionary.insert("homos");
        dictionary.printTable();

        System.out.println("\u001B[33m" + "insert sambdosa" + "\u001B[0m");
        dictionary.insert("sambdosa");
        dictionary.printTable();

        System.out.println("\u001B[33m" + "insert saddmbosa" + "\u001B[0m");
        dictionary.insert("saddmbosa");
        dictionary.printTable();

        System.out.println("\u001B[33m" + "delete saddmbosa" + "\u001B[0m");
        dictionary.delete("saddmbosa");
        dictionary.printTable();

        System.out.println("\u001B[33m" + "delete homos" + "\u001B[0m");
        dictionary.delete("homos");
        dictionary.printTable();

        System.out.println("\u001B[33m" + "delete sambdosa" + "\u001B[0m");
        dictionary.delete("sambdosa");
        dictionary.printTable();
    }
}