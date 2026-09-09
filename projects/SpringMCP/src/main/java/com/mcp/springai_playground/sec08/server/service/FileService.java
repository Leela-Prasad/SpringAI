package com.mcp.springai_playground.sec08.server.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FileService {

    private final Path basePath;

    public FileService() throws IOException {
        basePath = Paths.get(System.getProperty("java.io.tmpdir"), "elicit-demo-files");
        initialize();
    }

    public List<String> listFiles() throws IOException {
        try(var paths = Files.list(basePath)) {
            return paths.map(filepath -> filepath.getFileName().toString())
                    .toList();
        }
    }

    public void deleteFile(String fileName) throws IOException {
        var file = basePath.resolve(fileName);
        Files.deleteIfExists(file);
    }

    private void initialize() throws IOException {
        if(Files.notExists(basePath))
            Files.createDirectory(basePath);

        for(int i=1; i<=5; ++i) {
            var file = basePath.resolve("file%d.txt".formatted(i));
            if(Files.notExists(file)) {
                Files.writeString(file, "Sample Content for " + file.getFileName());
           }
        }
    }
}
