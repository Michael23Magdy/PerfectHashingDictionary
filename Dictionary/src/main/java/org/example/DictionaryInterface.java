package org.example;

public interface DictionaryInterface {
    Boolean search(String str);
    void insert(String str);
    void delete(String str);
    void batchInsert(String path);
    void bashDelete(String path);
}
