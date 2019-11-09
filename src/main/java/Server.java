package main.java;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Server {

    private static final int NUMBER_OF_CONSUMERS = 1;
    private static final int NUMBER_OF_PRODUCERS = 2;
    private static final int QUEUE_SIZE = 2;
    private static BlockingQueue<String> queue;
    private static Collection<Thread> producerThreadCollection, allThreadCollection;


    public static void main(String[] args) {
        producerThreadCollection = new ArrayList<>();
        allThreadCollection = new ArrayList<>();
        queue = new LinkedBlockingDeque<>(QUEUE_SIZE);

        createAndStartProducers();
        createAndStartConsumers();

        for(Thread t: allThreadCollection){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createAndStartProducers(){
        for(int i = 1; i <= NUMBER_OF_PRODUCERS; i++){
            Producer producer = new Producer(Paths.get("File" + i + ".txt"), queue);
            Thread producerThread = new Thread(producer,"producer-"+i);
            producerThreadCollection.add(producerThread);
            producerThread.start();
        }
        allThreadCollection.addAll(producerThreadCollection);
    }

    private static void createAndStartConsumers(){
        for(int i = 0; i < NUMBER_OF_CONSUMERS; i++){
            Thread consumerThread = new Thread(new Consumer(queue), "consumer-"+i);
            allThreadCollection.add(consumerThread);
            consumerThread.start();
        }
    }

    public static boolean isProducerAlive(){
        for(Thread t: producerThreadCollection){
            if(t.isAlive())
                return true;
        }
        return false;
    }
}
