package org.example;

public interface DictionaryInterface {
    Boolean search(String str);
    Boolean insert(String str);
    Boolean delete(String str);
    int batchInsert(String path);
    int batchDelete(String path);
    int getSize();
}
