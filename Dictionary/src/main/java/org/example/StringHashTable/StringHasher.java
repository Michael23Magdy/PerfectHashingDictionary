package org.example.StringHashTable;

import java.nio.charset.StandardCharsets;

import static java.lang.Math.abs;

public class StringHasher {
    public static int hash(String str, int key, int mod) {
        long hash = 0, power = 1;
        for (char c : str.toCharArray()) {
            hash = (hash + c * power) % mod;
            power = (power * key) % mod;
            System.out.println("+========================" + hash + " " + power +"============");
        }
        return (int) hash;
    }

     public static int javaHash(String str, int seed, int mod) {
         return  abs(str.hashCode() % mod);
     }

}
