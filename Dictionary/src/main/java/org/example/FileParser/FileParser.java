package org.example.FileParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FileParser {
    public List<String> readFileContent(String filePath) throws IOException {
        String content = Files.readString(Path.of(filePath));
        return Arrays.stream(content.split("\\W+"))
                     .filter(s -> !s.isBlank())
                     .toList();
    }
}