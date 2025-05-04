package org.example.StringHashTable;

public class StringHasher {
    public static int hash(String str, int key, int mod){
        int hashValue = 0;
        int power = 1;
        for(char c : str.toCharArray()){
            hashValue = (hashValue + c * power) % mod;
            power = (power * key) % mod;
        }
        return hashValue;
    }
}
