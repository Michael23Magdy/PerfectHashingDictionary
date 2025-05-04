package org.example.StringHashTable;

import java.util.ArrayList;
import java.util.List;

public class HashTableNsquared implements HashTableInterface {
    private PrimeGenerator primeGenerator;
    List<String> table = new ArrayList<>();

    public HashTableNsquared(PrimeGenerator primeGenerator){
        this.primeGenerator = primeGenerator;
    }
    @Override
    public Boolean search(String str) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public void insert(String str) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void delete(String str) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
