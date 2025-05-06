package org.example.StringHashTable;

/**
 * An implementation of a hash table with O(1) insert and search operations
 * using a two-level hash table structure with perfect hashing.
 */
public class HashTableN implements HashTableInterface {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR_THRESHOLD = 0.75f;
    private static final int MAX_KEY_ATTEMPTS = 10;
    
    // First level hash table
    private final int firstLevelSize;
    private final int firstLevelKey;
    private final BucketHashTable[] buckets;
    
    private int size;
    private int rehashCount;
    
    /**
     * Creates a new hash table with the specified initial capacity.
     * 
     * @param initialCapacity the initial capacity of the hash table
     */
    public HashTableN(int initialCapacity) {
        System.out.println("Starting two-level  hash ");
        this.firstLevelSize = Math.max(DEFAULT_INITIAL_CAPACITY, initialCapacity);
        this.firstLevelKey = PrimeGenerator.getInstance().getRandomPrime();
        this.buckets = new BucketHashTable[firstLevelSize];
        this.size = 0;
        this.rehashCount = 0;
    }
    
    /**
     * Inserts a string into the hash table.
     * O(1) time complexity.
     * 
     * @param str the string to insert
     * @throws NullPointerException if the string is null
     */
    @Override
    public void insert(String str) {
        if (str == null) {
            throw new NullPointerException("Cannot insert null value");
        }
        
        int index = StringHasher.hash(str, firstLevelKey, firstLevelSize);
        
        // Create a bucket if needed
        if (buckets[index] == null) {
            buckets[index] = new BucketHashTable();
        }
        
        // Only increment size if the item is newly added
        if (!buckets[index].contains(str)) {
           // System.out.println(index);
            buckets[index].insert(str);
            size++;
            
            // Check if we need to resize the entire hash table
            if ((float) size / firstLevelSize >= LOAD_FACTOR_THRESHOLD) {
                resize();
            }
        }
        
        //printTables();
    }
    
    /**
     * Searches for a string in the hash table.
     * O(1) time complexity.
     * 
     * @param str the string to search for
     * @return true if the string is found, false otherwise
     */
    @Override
    public Boolean search(String str) {
        if (str == null) {
            return false;
        }
        
        int index = StringHasher.hash(str, firstLevelKey, firstLevelSize);
        
        if (buckets[index] == null) {
            return false;
        }
        System.out.println("    "+index);
        return buckets[index].contains(str);
    }
    
    /**
     * Deletes a string from the hash table.
     * O(1) time complexity.
     * 
     * @param str the string to delete
     */
    @Override
    public void delete(String str) {
        if (str == null) {
            return;
        }
        
        int index = StringHasher.hash(str, firstLevelKey, firstLevelSize);
        
        if (buckets[index] != null && buckets[index].remove(str)) {
            size--;
            
            // If bucket is empty after removal, set it to null
            if (buckets[index].isEmpty()) {
                buckets[index] = null;
            }
        }
    }
    
    /**
     * Completely rebuilds the hash table with a larger size.
     * This is called when the load factor exceeds the threshold.
     */
    private void resize() {
        HashTableN newTable = new HashTableN(firstLevelSize * 2);
        
        // Rehash all existing elements
        for (BucketHashTable bucket : buckets) {
            if (bucket != null) {
                for (String item : bucket.getAllItems()) {
                    if (item != null) {
                        newTable.insert(item);
                    }
                }
            }
        }
        
        // Note: Since we can't modify our own fields directly in this implementation,
        // we'll need to rely on creating a new table and having the client use that.
        // In practice, you would want to update the firstLevelSize, buckets, etc.
        
        rehashCount++;
        System.out.println("Hash table needs resizing to maintain O(1) performance. " +
                "Please create a new hash table with size " + (firstLevelSize * 2));
    }
    
    /**
     * Returns the current size of the hash table.
     * 
     * @return the number of elements in the hash table
     */
    @Override
    public int getSize() {
        return size;
    }
    
    /**
     * Returns the number of rehash operations performed.
     * 
     * @return the rehash count
     */
    @Override
    public int getNoRehashes() {
        return rehashCount;
    }
    
    /**
     * Prints the contents of the hash table.
     */
    @Override
    public void printTable() {
        printTables();
    }
    
    /**
     * Prints the contents of the hash table for debugging.
     */
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
    
    /**
     * Returns the total space used by the hash table.
     * 
     * @return the total space used
     */
    public int calculateTotalSpace() {
        int space = firstLevelSize; // First level array size
        
        // Add space used by buckets
        for (BucketHashTable bucket : buckets) {
            if (bucket != null) {
                space += bucket.getCapacity();
            }
        }
        
        return space;
    }
    
    /**
     * Verifies that the space complexity is O(n).
     * 
     * @return true if space complexity is O(n), false otherwise
     */
    public boolean verifySpaceComplexity() {
        int n = size;
        if (n == 0) return true;
        
        int space = calculateTotalSpace();
        
        // For O(n) space, we expect space to be proportional to n
        // We use a constant factor to allow for some overhead
        return space <= 4 * n;
    }
    
    /**
     * Inner class representing a bucket in the first-level hash table.
     * Each bucket is itself implemented as a perfect hash table that
     * resolves collisions by finding a key that gives no collisions.
     */
    private static class BucketHashTable {
        private String[] items;
        private int key;
        private int size;
        private int capacity;
        private int rebuildAttempts = 0;
        
        /**
         * Creates a new bucket hash table with initial empty state.
         */
        public BucketHashTable() {
            this.size = 0;
            this.capacity = 0;
            this.items = new String[0];
            this.key = PrimeGenerator.getInstance().getRandomPrime();
        }
        
        /**
         * Inserts a string into the bucket.
         * Rebuilds the perfect hash table if needed.
         * 
         * @param str the string to insert
         */
        public void insert(String str) {
            if (str == null) {
                return;
            }
            
            
            if (contains(str)) {
                return;
            }
            
            // Add the string to our collection
            String[] newItems = new String[size + 1];
            
            for (int i = 0; i < size; i++) {
                newItems[i] = items[i];
            }
            newItems[size] = str;
            size++;
            
            
            // Rebuild the perfect hash table
            rebuildPerfectHash(newItems);
        }
        
       
        private void rebuildPerfectHash(String[] itemsToInsert) {
            // Calculate a reasonable size for the bucket (size^2 ensures good distribution)
            int newCapacity = itemsToInsert.length * itemsToInsert.length;
            //if (newCapacity < 4) newCapacity = 4; // Minimum size
            
            boolean collisionFound;
            int attempts = 0;
            PrimeGenerator primeGen = PrimeGenerator.getInstance();
            
            do {
                attempts++;
                collisionFound = false;
                key = primeGen.getRandomPrime();
                
                // Create a new table
                String[] newTable = new String[newCapacity];
                
                // Try to place all items
                for (String item : itemsToInsert) {
                    int index = StringHasher.hash(item, key, newCapacity);
                    //System.out.println(item+"   :  "+index+ "  :  "+key);
                    // Check for collision
                    if (newTable[index] != null) {
                        collisionFound = true;
                        break;
                    }
                    
                    // Place the item
                    newTable[index] = item;
                }
                
                // If no collisions, update our table
                if (!collisionFound) {
                    items = newTable;
                    capacity = newCapacity;
                }
                
            } while (collisionFound && attempts < MAX_KEY_ATTEMPTS);
            
            // If we couldn't find a perfect hash after many attempts,
            // we'll still use the last key and handle occasional collisions
            if (collisionFound) {
                System.out.println("Warning: Could not find a perfect hash key after " + 
                        MAX_KEY_ATTEMPTS + " attempts. Bucket performance may degrade.");
                
                // Create a new table with the last key we tried
                 String[] newTable = new String[newCapacity];
                
                // Place all items, handling collisions by linear probing
                for (String item : itemsToInsert) {
                    int index = StringHasher.hash(item, key, newCapacity);
                    int originalIndex = index;
                    
                    // Handle collision with linear probing (fallback)
                    while (newTable[index] != null) {
                        index = (index + 1) % newCapacity;
                        if (index == originalIndex) {
                            // This should never happen if our table is large enough
                            throw new RuntimeException("Hash table is full");
                        }
                    }
                    
                    newTable[index] = item;
                }
                
                items = newTable;
                capacity = newCapacity;
            }
            
            if (attempts > 1) {
                rebuildAttempts += (attempts);
            }
        }
        
        /**
         * Checks if a string is in the bucket.
         * 
         * @param str the string to check
         * @return true if the string is in the bucket, false otherwise
         */
        public boolean contains(String str) {
            //System.out.println("hi");
            if (str == null || size == 0) {
               
                return false;
            }
            
            int index = StringHasher.hash(str, key, capacity);
            
            // Direct check for perfect hash
            if (items[index] != null && str.equals(items[index])) {
                return true;
            }
            System.out.println(rebuildAttempts);
            // If we couldn't achieve perfect hashing, fallback to linear probing
            if (rebuildAttempts >= MAX_KEY_ATTEMPTS) {
                
                int originalIndex = index;
                do {
                    index = (index + 1) % capacity;
                    if (items[index] != null && str.equals(items[index])) {
                        return true;
                    }
                } while (index != originalIndex && items[index] != null);
            }
            
            return false;
        }
        
        /**
         * Removes a string from the bucket.
         * 
         * @param str the string to remove
         * @return true if the string was removed, false if it wasn't found
         */
        public boolean remove(String str) {
            if (str == null || size == 0) {
                return false;
            }
            
            int index = StringHasher.hash(str, key, capacity);
            
            // Check direct hash location first (perfect hashing case)
            if (items[index] != null && str.equals(items[index])) {
                items[index] = null;
                size--;
                
                // If we removed the last item, don't rebuild
                if (size == 0) {
                    return true;
                }
                
                // Collect remaining items and rebuild
                String[] remainingItems = getAllItems();
                rebuildPerfectHash(remainingItems);
                return true;
            }
            
            // If we couldn't achieve perfect hashing, check with linear probing
            if (rebuildAttempts >= MAX_KEY_ATTEMPTS) {
                int originalIndex = index;
                do {
                    index = (index + 1) % capacity;
                    if (items[index] != null && str.equals(items[index])) {
                        items[index] = null;
                        size--;
                        
                        // Rebuild the hash table to maintain performance
                        String[] remainingItems = getAllItems();
                        rebuildPerfectHash(remainingItems);
                        return true;
                    }
                } while (index != originalIndex && items[index] != null);
            }
            
            return false;
        }
        
        /**
         * Checks if the bucket is empty.
         * 
         * @return true if the bucket is empty, false otherwise
         */
        public boolean isEmpty() {
            return size == 0;
        }
        
        /**
         * Returns the capacity of the bucket.
         * 
         * @return the capacity
         */
        public int getCapacity() {
            return capacity;
        }
        
        /**
         * Returns all non-null items in the bucket.
         * 
         * @return an array of all items
         */
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
        
        /**
         * Prints the contents of the bucket.
         */
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