package org.example.FileParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
    public List<String> readFileContent(String filePath) throws IOException {
      ArrayList<String> strings = new ArrayList<>();
      BufferedReader br = new BufferedReader(new InputStreamReader(
         getClass().getClassLoader().getResourceAsStream(filePath)));
      String line;
      while ((line = br.readLine()) != null) {
         strings.add(line.trim());  // Read each line as a string and trim any extra spaces
      }
      return strings;
    }
}