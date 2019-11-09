package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {


    private Path fileToRead;
    private BlockingQueue<String> queue;

    public Producer(Path filePath, BlockingQueue<String> q){
        fileToRead = filePath;
        queue = q;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = Files.newBufferedReader(fileToRead);
            String line;
            while((line = reader.readLine()) != null){
                try {
                    queue.put(line);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
