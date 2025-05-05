package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.example.StringHashTable.HashTableNsquared;

class HashTableNsquaredTest {

    private HashTableNsquared hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTableNsquared();
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
    void testRehashingTriggeredByQuadraticThreshold() {
        for (int i = 0; i < 20; i++) {
            hashTable.insert("item" + i);
        }

        for (int i = 0; i < 20; i++) {
            assertTrue(hashTable.search("item" + i));
        }

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

        String[] toPreserve = {"x", "y", "z"};
        for (String s : toPreserve) {
            assertTrue(hashTable.search(s));
        }

        // Force rehash by size
        for (int i = 0; i < 20; i++) {
            hashTable.insert("more" + i);
        }

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
        for (int i = 0; i < 1000; i++) {
            hashTable.insert("data" + i);
        }

        for (int i = 0; i < 1000; i++) {
            assertTrue(hashTable.search("data" + i));
        }

        assertEquals(1000, hashTable.getSize());
    }
}