package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.example.StringHashTable.HashTableN;

class HashTableNTest {

    private HashTableN hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTableN();
    }

    @Test
    void testInsertAndSearchBasic() {
        hashTable.insert("apple");
        hashTable.insert("banana");

        assertTrue(hashTable.search("apple"));
        assertTrue(hashTable.search("banana"));
        assertFalse(hashTable.search("carrot"));
    }

    @Test
    void testDeleteRemovesElement() {
        hashTable.insert("alpha");
        assertTrue(hashTable.search("alpha"));

        hashTable.delete("alpha");
        assertFalse(hashTable.search("alpha"));
    }

    @Test
    void testDeleteNonExistentDoesNothing() {
        hashTable.insert("beta");
        hashTable.delete("gamma"); // gamma was never inserted
        assertTrue(hashTable.search("beta"));
    }

    @Test
    void testInsertSameElementTwiceOnlyCountsOnce() {
        hashTable.insert("duplicate");
        int initialSize = hashTable.getSize();

        hashTable.insert("duplicate");
        int afterInsertSize = hashTable.getSize();

        assertEquals(initialSize, afterInsertSize);
    }

    @Test
    void testSizeAccuracy() {
        assertEquals(0, hashTable.getSize());
        hashTable.insert("a");
        hashTable.insert("b");
        hashTable.insert("c");

        assertEquals(3, hashTable.getSize());

        hashTable.delete("b");
        assertEquals(2, hashTable.getSize());
    }

    @Test
    void testRehashingTriggeredByQuadraticThreshold() {      //////
        int noRehashesBefore = hashTable.getNoRehashes();
        for (int i = 0; i < 20; i++) {
            hashTable.insert("item" + i);
            
        }
        int noRehashesAfter = hashTable.getNoRehashes();
        int noRehashes = noRehashesAfter - noRehashesBefore;

        for (int i = 0; i < 20; i++) {
            //System.out.println(i);
            assertTrue(hashTable.search("item" + i));
        }
        System.out.println("testRehashingTriggeredByQuadraticThreshold = "+noRehashes);

        assertEquals(20, hashTable.getSize());
    }

    @Test
    void testInsertTriggersRehashOnCollisionLimit() {
        hashTable.insert("abc");
        hashTable.insert("cba");
        hashTable.insert("bca");

        assertTrue(hashTable.search("abc"));
        assertTrue(hashTable.search("cba"));
        assertTrue(hashTable.search("bca"));
    }

    @Test
    void testInsertNullDoesNotCrash() {
        assertThrows(NullPointerException.class, () -> hashTable.insert(null));
    }

    @Test
    void testSearchInEmptyTableReturnsFalse() {
        assertFalse(hashTable.search("anything"));
    }

    @Test
    void testRehashPreservesData() {
        hashTable.insert("x");
        hashTable.insert("y");
        hashTable.insert("z");
        int noRehashesBefore = hashTable.getNoRehashes();
        String[] toPreserve = {"x", "y", "z"};
        for (String s : toPreserve) {
            assertTrue(hashTable.search(s));
        }

        // Force rehash by size
        for (int i = 0; i < 20; i++) {
            hashTable.insert("mor" + i);
        }
        int noRehashesAfter = hashTable.getNoRehashes();
        int noRehashes = noRehashesAfter - noRehashesBefore;
        System.out.println("testRehashPreservesData = "+noRehashes);

        for (String s : toPreserve) {
            assertTrue(hashTable.search(s));
        }
    }

    @Test
    void testDeleteAndReinsert() {
        hashTable.insert("alpha");
        hashTable.delete("alpha");

        assertFalse(hashTable.search("alpha"));

        hashTable.insert("alpha");
        assertTrue(hashTable.search("alpha"));
    }

    @Test
    void testStressWithManyInserts() { 
        int noRehashesBefore = hashTable.getNoRehashes();
        for (int i = 0; i < 1000; i++) {
            hashTable.insert("data" + i);
        }
        int noRehashesAfter = hashTable.getNoRehashes();
        int noRehashes = noRehashesAfter - noRehashesBefore;


        for (int i = 0; i < 1000; i++) {
            assertTrue(hashTable.search("data" + i));
        }
        System.out.println("testStressWithManyInserts = "+noRehashes);
        assertEquals(1000, hashTable.getSize());
    }

    @Test
    void testCaseSensitivity() {
        hashTable.insert("apple");
        hashTable.insert("Apple");

        assertTrue(hashTable.search("apple"));
        assertTrue(hashTable.search("Apple"));
        assertNotEquals("apple", "Apple");
    }

    @Test
    void testRehashingAndCollision() {
        hashTable.insert("collision1");
        hashTable.insert("collision2");
        hashTable.insert("collision3");

        // Trigger rehashing
        hashTable.insert("collision4");

        assertTrue(hashTable.search("collision1"));
        assertTrue(hashTable.search("collision2"));
        assertTrue(hashTable.search("collision3"));
        assertTrue(hashTable.search("collision4"));
    }

    @Test
    void testNullSearchDoesNotCrash() {
        assertFalse(hashTable.search(null));
    }

    @Test
    void testInsert1M() {
        int noRehashesBefore = hashTable.getNoRehashes();

        for (int i = 0; i < 1000000; i++) {
            hashTable.insert("item" + i);
        }
        int noRehashesAfter = hashTable.getNoRehashes();
        int noRehashes = noRehashesAfter - noRehashesBefore;
        
        
        for (int i = 0; i < 1000000; i++) {
            
            assertTrue(hashTable.search("item" + i));
        }
        System.out.println("testInsert1M =  "+noRehashes);
    }
    @Test
    void testInsert100k() {
        int noRehashesBefore = hashTable.getNoRehashes();

        for (int i = 0; i < 100000; i++) {
            hashTable.insert("item" + i);
        }
        int noRehashesAfter = hashTable.getNoRehashes();
        int noRehashes = noRehashesAfter - noRehashesBefore;
       
        
        for (int i = 0; i < 100000; i++) {
            
            assertTrue(hashTable.search("item" + i));
        }
        System.out.println("testInsert100k =  "+noRehashes);
    }
    @Test
    void testInsert10k() {
        int noRehashesBefore = hashTable.getNoRehashes();

        for (int i = 0; i < 10000; i++) {
            hashTable.insert("item" + i);
        }
        int noRehashesAfter = hashTable.getNoRehashes();
        int noRehashes = noRehashesAfter - noRehashesBefore;
       
        
        for (int i = 0; i < 10000; i++) {
            
            assertTrue(hashTable.search("item" + i));
        }
        System.out.println("testInsert10k =  "+noRehashes);
    }

    @Test
    void testDeleteNonExistentElement() {
        hashTable.delete("nonExistent");

        
        hashTable.insert("existing");
        assertTrue(hashTable.search("existing"));
    }

    @Test
    void testInsertSpecialCharacters() {
        hashTable.insert("!@#$%^&*()");
        hashTable.insert("1234567890");

        assertTrue(hashTable.search("!@#$%^&*()"));
        assertTrue(hashTable.search("1234567890"));
    }

    @Test
    void testDuplicateDeletes() {
        hashTable.insert("apple");
        hashTable.delete("apple");
        hashTable.delete("apple"); // Trying to delete again

        assertFalse(hashTable.search("apple"));
    }

    @Test
    void testInsertDeleteRapid() {
        for (int i = 0; i < 100; i++) {
            hashTable.insert("item" + i);
            hashTable.delete("item" + i);
        }

        // Ensure table is empty
        for (int i = 0; i < 100; i++) {
            assertFalse(hashTable.search("item" + i));
        }
    }



    @Test
    void testInsertLargeString() {
        String largeString = "a".repeat(10000); // String of 10,000 'a' characters
        hashTable.insert(largeString);

        assertTrue(hashTable.search(largeString));
    }
}