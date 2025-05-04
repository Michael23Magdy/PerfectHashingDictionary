package org.example.StringHashTable;

public class StringHasher {
    final int mod = (int) 1e9 + 7;
    public int hash(String str, int key){
        int hashValue = 0;
        int power = 1;
        for(char c : str.toCharArray()){
            hashValue = (hashValue + c * power) % mod;
            power = (power * key) % mod;
        }
        return hashValue;
    }
}
