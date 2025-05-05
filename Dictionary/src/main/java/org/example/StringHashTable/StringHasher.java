package org.example.StringHashTable;

public class StringHasher {
    public static int hash(String str, int key, int mod) {
        long hash = 0, power = 1;
        for (char c : str.toCharArray()) {
            hash = (hash + c * power) % mod;
            power = (power * key) % mod;
        }
        return (int) hash;
    }

}
