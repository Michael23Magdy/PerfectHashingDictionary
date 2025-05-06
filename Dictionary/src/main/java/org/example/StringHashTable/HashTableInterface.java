package org.example.StringHashTable;

public interface HashTableInterface {
    Boolean search(String str);
    void insert(String str);
    void delete(String str);
    int getSize();
    void printTable();
    int getNoRehashes();
}