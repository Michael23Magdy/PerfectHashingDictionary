package org.example.StringHashTable;

import java.util.ArrayList;
import java.util.List;

public class PrimeGenerator {
    private static PrimeGenerator primeGenerator;
    List<Integer> primes = new ArrayList<>();
    private PrimeGenerator(){
        int limit = 1000000;
        boolean[] isPrime = new boolean[limit + 1];
        for (int i = 2; i <= limit; i++) {
            isPrime[i] = true;
        }

        for (int i = 2; i * i <= limit; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= limit; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        for (int i = 256; i <= limit; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }
    }

    public int getRandomPrime(){
        return primes.get((int) (Math.random() * primes.size()));
    }

    public static PrimeGenerator getInstance(){
        if(primeGenerator == null){
            primeGenerator = new PrimeGenerator();
        }
        return primeGenerator;
    }
}
