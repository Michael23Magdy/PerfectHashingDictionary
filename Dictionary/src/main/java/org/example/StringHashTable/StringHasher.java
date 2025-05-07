package org.example.StringHashTable;

import java.nio.charset.StandardCharsets;

import static java.lang.Math.abs;

public class StringHasher {
    public static int hash(String str, int key, int mod) {
        long hash = 0, power = 1;
        for (char c : str.toCharArray()) {
            hash = (hash + c * power) % mod;
            power = (power * key) % mod;
        }
        return (int) hash;
    }

     public static int javaHash(String str, int seed, int mod) {
         return  abs(str.hashCode() % mod);
     }
    public static int murmurInspiredHash(String str, int seed, int mod) {
        if (str == null || mod <= 0) {
            return 0;
        }
        
        byte[] data = str.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        int h = seed;
        
        // Process 4 bytes at a time
        final int c1 = 0xcc9e2d51;
        final int c2 = 0x1b873593;
        
        int len = data.length;
        int i = 0;
        
        // Body
        while (i + 4 <= len) {
            int k = (data[i] & 0xFF) |
                    ((data[i + 1] & 0xFF) << 8) |
                    ((data[i + 2] & 0xFF) << 16) |
                    ((data[i + 3] & 0xFF) << 24);
            
            k *= c1;
            k = Integer.rotateLeft(k, 15);
            k *= c2;
            
            h ^= k;
            h = Integer.rotateLeft(h, 13);
            h = h * 5 + 0xe6546b64;
            
            i += 4;
        }
        
        // Tail
        int k = 0;
        switch (len - i) {
            case 3:
                k ^= (data[i + 2] & 0xFF) << 16;
            case 2:
                k ^= (data[i + 1] & 0xFF) << 8;
            case 1:
                k ^= (data[i] & 0xFF);
                k *= c1;
                k = Integer.rotateLeft(k, 15);
                k *= c2;
                h ^= k;
        }
        
        // Finalization
        h ^= len;
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;
        
        // Apply modulus to ensure it depends on table size
        return Math.abs(h % mod);
    }

}
