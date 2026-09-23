package Synchronization_OS;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class producerConsumer {

    int capacity = 5;

    Queue<Integer> queue = new LinkedList<>();

    // Semaphores
    Semaphore empty = new Semaphore(capacity);
    Semaphore full = new Semaphore(0);
    Semaphore mutex = new Semaphore(1);


    // Producer
    public void produce(int value) throws InterruptedException {

        // wait(empty)
        empty.acquire();

        // wait(mutex)
        mutex.acquire();

        queue.add(value);

        System.out.println("Produced: " + value);

        // signal(mutex)
        mutex.release();

        // signal(full)
        full.release();
    }


    // Consumer
    public void consume() throws InterruptedException {

        // wait(full)
        full.acquire();

        // wait(mutex)
        mutex.acquire();

        int value = queue.remove();

        System.out.println("Consumed: " + value);

        // signal(mutex)
        mutex.release();

        // signal(empty)
        empty.release();
    }


    // Producer Thread
    static class Producer extends Thread {

        producerConsumer pc;

        Producer(producerConsumer pc) {
            this.pc = pc;
        }

        public void run() {

            try {

                for (int i = 0; i < 10; i++) {

                    pc.produce(i);

                    Thread.sleep(100);
                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }
    }


    // Consumer Thread
    static class Consumer extends Thread {

        producerConsumer pc;

        Consumer(producerConsumer pc) {
            this.pc = pc;
        }

        public void run() {

            try {

                for (int i = 0; i < 10; i++) {

                    pc.consume();

                    Thread.sleep(150);
                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }
    }


    // Main
    public static void main(String[] args) {

        producerConsumer pc = new producerConsumer();

        Producer producer = new Producer(pc);

        Consumer consumer = new Consumer(pc);

        producer.start();
        consumer.start();
    }
}
