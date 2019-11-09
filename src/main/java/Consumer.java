package main.java;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class Consumer implements Runnable {


    private BlockingQueue<String> queue;
    private static volatile SortedSet<String> s = Collections.synchronizedSortedSet(new TreeSet<>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return extractInt(o1) - extractInt(o2);
        }

        int extractInt(String s) {
            String num = s.substring(0, s.indexOf(","));
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        }
    }));

    public Consumer(BlockingQueue<String> q){
        queue = q;
    }

    public void run(){
        while(true){
            String line = queue.poll();

            if(line == null && !Server.isProducerAlive())
                return;

            if(line != null){
                s.add(line);
            }
            writeToFile();
        }
    }

    private void writeToFile() {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("Output.txt"));
            String output = s.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("\n", "", ""));
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
