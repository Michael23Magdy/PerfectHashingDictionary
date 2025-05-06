package org.example.StringHashTable;

import java.util.ArrayList;
import java.util.List;


public class HashTableN implements HashTableInterface {
    private final PrimeGenerator primeGenerator;
    private final int firstLevelSize;
    private final int firstLevelKey;

    private final String[] firstLevelBuckets; // first table is an array of string
    // new assumption: if collision happen then first table bucket will be empty.
    private final SecondLevelTable[] secondLevelTables; // array of secondary tables

    private int rebuildCount = 0;
    private int totalItemCount = 0;
    private static final int MAX_REBUILD_ATTEMPTS = 100;

    public HashTableN(int initialSize) {
        this.firstLevelSize = initialSize;
        this.primeGenerator = PrimeGenerator.getInstance();

        this.firstLevelKey = primeGenerator.getRandomPrime();
        this.firstLevelBuckets = new String[initialSize];
        this.secondLevelTables = new SecondLevelTable[initialSize];
    }

    @Override
    public void insert(String str) {
        if (str == null) throw new NullPointerException();

        int index = StringHasher.javaHash(str, firstLevelKey, firstLevelSize);

        if(secondLevelTables[index] == null) {
            if(firstLevelBuckets[index] == null) {
                firstLevelBuckets[index] = str;
            } else if(firstLevelBuckets[index].equals(str)) { // already exists in the firstLevelBucket
                return;
            } else {
                buildSecondLevelTable(index, str);
            }
        } else {
            if(secondLevelTables[index].contains(str)) return; // already exists in the secondLevelBucket
            buildSecondLevelTable(index, str);
        }

        totalItemCount++;
    }

    private void buildSecondLevelTable(int bucketIndex, String newString) {

        List<String> bucket = new ArrayList<>();

        SecondLevelTable oldTable = secondLevelTables[bucketIndex];

        if(oldTable == null) {
            oldTable = new SecondLevelTable(primeGenerator.getRandomPrime(), 0);
            bucket.add(firstLevelBuckets[bucketIndex]);
            firstLevelBuckets[bucketIndex] = null;
        }

        for (String s : oldTable.table) {
            if (s != null) bucket.add(s);
        }

        if (newString != null) bucket.add(newString);

        boolean collisionFound = true;
        int attempts = 0;

        int secondLevelSize = bucket.size() * bucket.size();

        if (secondLevelSize == 1) {
            secondLevelTables[bucketIndex] = null;
            firstLevelBuckets[bucketIndex] = bucket.getFirst();
            return;
        }

        while(collisionFound && attempts < MAX_REBUILD_ATTEMPTS) {
            attempts++;
            collisionFound = false;

            int hashKey = primeGenerator.getRandomPrime();
            SecondLevelTable newTable = new SecondLevelTable(hashKey, secondLevelSize + attempts);
            String[] table = newTable.table;

            for (String item : bucket) {
                int index = StringHasher.javaHash(item, hashKey, secondLevelSize + attempts);

                if (table[index] != null) {
                    collisionFound = true;
                    break;
                } else {
                    table[index] = item;
                }
            }

            if (!collisionFound) {
                secondLevelTables[bucketIndex] = newTable;
            }
        }

        if (attempts > 1) {
            rebuildCount += (attempts - 1);
        }

        if (attempts >= MAX_REBUILD_ATTEMPTS) {
            System.out.println("Warning: Could not build perfect second-level hash table after "
                    + MAX_REBUILD_ATTEMPTS + " attempts. Performance may degrade.");
        }
    }

    @Override
    public Boolean search(String str) {
        if (str == null) return false;

        int index = StringHasher.javaHash(str, firstLevelKey, firstLevelSize);


        if(secondLevelTables[index] == null) {
            if (firstLevelBuckets[index] == null) return false;
            else {
                return firstLevelBuckets[index].equals(str);
            }
        } else {
            return secondLevelTables[index].contains(str);
        }
    }

    @Override
    public void delete(String str) {
        if (str == null) return;

        int index = StringHasher.javaHash(str, firstLevelKey, firstLevelSize);

        if(!search(str)) {
            return;
        }

        if(secondLevelTables[index] == null) {
            firstLevelBuckets[index] = null;
        } else {
            secondLevelTables[index].delete(str);
            buildSecondLevelTable(index, null);
        }
        totalItemCount--;
    }

    public int getRebuildCount() {
        return rebuildCount;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public int calculateTotalSpace() {
        int space = firstLevelSize; // First level space
        
        // Add space for second level tables
        for (SecondLevelTable table : secondLevelTables) {
            if (table != null) {
                space += table.getSize();
            }
        }
        
        return space;
    }

    public boolean verifySpaceComplexity() {
        int n = totalItemCount;
        if (n == 0) return true;
        
        int space = calculateTotalSpace();
        
        // For O(N) space, we expect space to be proportional to N
        // We use a constant factor to allow for some overhead
        return space <= 4 * n;
    }

    private static class SecondLevelTable {
        private final int key;
        private final int size;
        public String[] table;

        public SecondLevelTable(int key, int size) {
            this.key = key;
            this.size = size;
            table = new String[size];
        }

        public boolean contains(String str) {
            int index = StringHasher.javaHash(str, key, size);
            if(table[index] == null) return false;
            return table[index].equals(str);
        }

        public void delete(String str) {
            int index = StringHasher.javaHash(str, key, size);
            table[index] = null;
        }

        public int getSize() {
            return size;
        }
    }

    public void printTables() {
        System.out.println("---- Hash Table Contents ----");

        for (int i = 0; i < firstLevelSize; i++) {
            System.out.println("First-level bucket [" + i + "]: " + firstLevelBuckets[i]);

            SecondLevelTable second = secondLevelTables[i];
            if (second != null) {
                System.out.print("  Second-level table: [");
                for (int j = 0; j < second.getSize(); j++) {
                    System.out.print((second.table[j] != null ? second.table[j] : "null") + (j < second.getSize() - 1 ? ", " : ""));
                }
                System.out.println("]");
            }
        }

        System.out.println("Total items: " + totalItemCount);
        System.out.println("Rebuild attempts (beyond first): " + rebuildCount);
        System.out.println("--------------------------------");
    }

    @Override
    public int getSize() {
        return totalItemCount;
    }

    @Override
    public void printTable() {
        printTables();
    }

}