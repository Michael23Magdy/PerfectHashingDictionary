package org.example;

import java.util.Scanner;

import org.example.StringHashTable.HashTableInterface;
import org.example.StringHashTable.HashTableNsquared;
import org.example.StringHashTable.HashTableN;

public class Menu {
    private DictionaryInterface dictionary;

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int hashTableType;
        do{
            System.out.println("Enter the type of hash table:");
            System.out.println("1. O(N^2) space");
            System.out.println("2. O(N) space");
            System.out.print("Enter Your Choice: ");
            hashTableType = scanner.nextInt();
            if(hashTableType != 1 && hashTableType != 2)
                System.out.println("Invalid choice. Please try again.");
        } while(hashTableType != 1 && hashTableType != 2);
        
        HashTableInterface hashTable = null;
        switch (hashTableType){
            case 1: 
                hashTable = new HashTableNsquared();
                break;
            case 2:
                hashTable = new HashTableN();
                break;
        }
        Dictionary realDictionary = new Dictionary(hashTable);
        dictionary = new DictionaryTimeDecorator(realDictionary);

        while (true) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Insert a string");
            System.out.println("2. Delete a string");
            System.out.println("3. Search for a string");
            System.out.println("4. Batch insert from file");
            System.out.println("5. Batch delete from file");
            System.out.println("6. Exit");
            System.out.print("Enter Your Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter the string to insert: ");
                    String insertString = scanner.nextLine();
                    if (dictionary.insert(insertString)) {
                        System.out.println("String inserted successfully.");
                    } else {
                        System.out.println("Error: String already exists.");
                    }
                    break;

                case 2:
                    System.out.print("Enter the string to delete: ");
                    String deleteString = scanner.nextLine();
                    if (dictionary.delete(deleteString)) {
                        System.out.println("String deleted successfully.");
                    } else {
                        System.out.println("Error: String does not exist.");
                    }
                    break;

                case 3:
                    System.out.print("Enter the string to search: ");
                    String searchString = scanner.nextLine();
                    if (dictionary.search(searchString)) {
                        System.out.println("String exists in the dictionary.");
                    } else {
                        System.out.println("String does not exist in the dictionary.");
                    }
                    break;

                case 4:
                    System.out.print("Enter the file path for batch insert: ");
                    String insertFilePath = scanner.nextLine();
                    int sizeBefore = dictionary.getSize();
                    int numStrings = dictionary.batchInsert(insertFilePath);
                    int sizeAfter = dictionary.getSize();
                    int newStrings = sizeAfter - sizeBefore;
                    System.out.println("Newly added strings: " + newStrings);
                    System.out.println("Already existing strings: " + (numStrings - newStrings));
                    break;

                case 5:
                    System.out.print("Enter the file path for batch delete: ");
                    String deleteFilePath = scanner.nextLine();
                    int PreviousSize = dictionary.getSize();
                    int deleteStrings = dictionary.batchDelete(deleteFilePath);
                    int newSize = dictionary.getSize();
                    int removed = PreviousSize - newSize;
                    System.out.println("Deleted strings: " + removed);
                    System.out.println("Non-existing strings: " + (deleteStrings - removed));
                    break;

                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
