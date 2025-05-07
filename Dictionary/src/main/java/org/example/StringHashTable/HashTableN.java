package org.example.StringHashTable;

import java.util.ArrayList;
import java.util.List;


public class HashTableN implements HashTableInterface {
    private static final int DEFAULT_INITIAL_CAPACITY = 1000; 
    
    private static final int MAX_KEY_ATTEMPTS = 20; 
    
    // First level hash table
    private final int firstLevelSize;
    private final int firstLevelKey;
    private final BucketHashTable[] buckets;
    
    private int size;
    private int rehashCount;
    public HashTableN() {
        this.firstLevelSize = DEFAULT_INITIAL_CAPACITY;
        this.firstLevelKey = PrimeGenerator.getInstance().getRandomPrime();
        this.buckets = new BucketHashTable[firstLevelSize];
        this.size = 0; 
    }
    @Override
    public void insert(String str) {
        if (str == null) {
            throw new NullPointerException("Cannot insert null value");
        }
        
        int index = StringHasher.murmurInspiredHash(str, firstLevelKey, firstLevelSize);
    
        if (buckets[index] == null) {
            buckets[index] = new BucketHashTable();
        }
        
        if (!buckets[index].contains(str)) {
            buckets[index].insert(str);
            size++; 
        }
    }
    

    @Override
    public Boolean search(String str) {
        
        int index = StringHasher.murmurInspiredHash(str, firstLevelKey, firstLevelSize);
        
        if (buckets[index] == null) {
            return false;
        }
        return buckets[index].contains(str);
    }
    
  
    @Override
    public void delete(String str) {
        
        int index = StringHasher.murmurInspiredHash(str, firstLevelKey, firstLevelSize);
        if (buckets[index] != null && buckets[index].remove(str)) {
            size--;
            if (buckets[index].isEmpty()) {
                buckets[index] = null;
            }
        }
    }
    
    @Override
    public int getSize() {
        return size;
    }
  
    @Override
    public int getNoRehashes() {
        int sum = 0 ;
        for (BucketHashTable bucket : buckets){
            if (bucket !=null)
                sum+= bucket.rebuildAttempts;
        }
        return sum;
    }
    
    @Override
    public void printTable() {
        printTables();
    }
    
    
    public void printTables() {
        System.out.println("---- Hash Table Contents ----");
        
        for (int i = 0; i < firstLevelSize; i++) {
            if (buckets[i] != null && !buckets[i].isEmpty()) {
                System.out.println("Bucket [" + i + "]:");
                buckets[i].printBucket();
            }
        }
        
        System.out.println("Total items: " + size);
        System.out.println("First-level size: " + firstLevelSize);
        System.out.println("First-level key: " + firstLevelKey);
        System.out.println("Load factor: " + ((float) size / firstLevelSize));
        System.out.println("Rehash count: " + rehashCount);
        System.out.println("--------------------------------");
    }
        
    private static class BucketHashTable {
        private String[] items;
        private ArrayList <String> strings;
        private int key;
        private int size;
        private int capacity;
        private int rebuildAttempts = 0;
        
        
        public BucketHashTable() {
            this.size = 0;
            this.capacity = 1;
            this.items = new String[0];
            this.key = PrimeGenerator.getInstance().getRandomPrime();
            this.strings = new ArrayList<>();
        }
        
        public void insert(String str) {
            if (contains(str))return;
            
            if(size== 0){
                items = new String[1]; 
            }
            int index = StringHasher.murmurInspiredHash(str, key, capacity);
            strings.add(str);
            size++;
            if (items[index] == null) {
                items[index] = str;
                return;
            }
                
           

            String[] newItems = new String[size];
            int i = 0;
                
            for (String s : strings) {
                newItems[i++] = s;
            }
            newItems[size-1] = str;
            
            rebuildPerfectHash(newItems);
        }
        
        private void rebuildPerfectHash(String[] itemsToInsert) {
            int newCapacity = itemsToInsert.length * itemsToInsert.length;
        
            boolean collisionFound;
            int attempts = 0;
            PrimeGenerator primeGen = PrimeGenerator.getInstance();
            
            do {
                attempts++;
                collisionFound = false;
                key = primeGen.getRandomPrime();
                
                
                String[] newTable = new String[newCapacity];

                for (String item : itemsToInsert) {
                    int index = StringHasher.murmurInspiredHash(item, key, newCapacity);
                    if (newTable[index] != null) {
                        collisionFound = true;
                        break;
                    }
                    newTable[index] = item;
                }
               
                if (!collisionFound) {
                    items = newTable;
                    capacity = newCapacity;
                }
                
            } while (collisionFound && attempts < MAX_KEY_ATTEMPTS);
            
            if (collisionFound) {
                System.out.println("Warning: Could not find a perfect hash key after " + 
                        MAX_KEY_ATTEMPTS + " attempts. Bucket performance may degrade.");
            }
            
            if (attempts > 1) {
               // System.out.println("att = "+attempts );
                rebuildAttempts += (attempts);
            }
        }
        public boolean contains(String str) {
            if (str == null || size == 0) {
                return false;
            }
            
            if (capacity <= 0) {
                return false;
            }
        
            int index = StringHasher.murmurInspiredHash(str, key, capacity);
        
            if (items[index] != null && str.equals(items[index])) {
                return true;
            }
            return false;
        }
        
        public boolean remove(String str) {
            if (str == null || size == 0) {
                return false;
            }
            
            int index = StringHasher.murmurInspiredHash(str, key, capacity);
            
            
            if (items[index] != null && str.equals(items[index])) {
                items[index] = null;
                size--;
                
                if (size == 0) {
                    return true;
                }
                
                String[] remainingItems = getAllItems();
                rebuildPerfectHash(remainingItems);
                return true;
            }
            
            
            if (rebuildAttempts >= MAX_KEY_ATTEMPTS) {
                int originalIndex = index;
                do {
                    index = (index + 1) % capacity;
                    if (items[index] != null && str.equals(items[index])) {
                        items[index] = null;
                        size--;
                        
                        
                        String[] remainingItems = getAllItems();
                        rebuildPerfectHash(remainingItems);
                        return true;
                    }
                } while (index != originalIndex && items[index] != null);
            }
            
            return false;
        }
        
        
        public boolean isEmpty() {
            return size == 0;
        }
          
        public String[] getAllItems() {
            String[] result = new String[size];
            int index = 0;
            
            for (String item : items) {
                if (item != null) {
                    result[index++] = item;
                }
            }
            
            return result;
        }
        
       
        public void printBucket() {
            System.out.print("  Contents: [");
            boolean first = true;
            
            for (String item : items) {
                if (item != null) {
                    if (!first) {
                        System.out.print(", ");
                    }
                    System.out.print(item);
                    first = false;
                }
            }
            
            System.out.println("]");
            System.out.println("  Bucket size: " + size + " (capacity: " + capacity + ")");
            System.out.println("  Bucket hash key: " + key);
            System.out.println("  Rebuild attempts: " + rebuildAttempts);
        }
    }
}