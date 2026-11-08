
package csc450.module8;

import java.util.concurrent.CountDownLatch;

public class CounterApplication {

    private static final int LIMIT = 20;

    public static void main(String[] args) {

        CountDownLatch countingFinished = new CountDownLatch(1);

        // Thread 1 counts upward and signals completion.
        Thread countUp = new Thread(() -> {
            try {
                System.out.print("Thread 1 (count up): ");

                for (int value = 0; value <= LIMIT; value++) {
                    System.out.print(value + (value < LIMIT ? " " : "\n"));
                }
            } finally {
                // Always release the waiting thread.
                countingFinished.countDown();
            }
        }, "Count-Up");

        // Thread 2 waits until Thread 1 finishes.
        Thread countDown = new Thread(() -> {
            try {
                countingFinished.await();

                System.out.print("Thread 2 (count down): ");

                for (int value = LIMIT; value >= 0; value--) {
                    System.out.print(value + (value > 0 ? " " : "\n"));
                }

            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                System.err.println("Countdown interrupted.");
            }
        }, "Count-Down");

        // Start both worker threads.
        countUp.start();
        countDown.start();

        // Wait for both threads before completing the program.
        try {
            countUp.join();
            countDown.join();

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread interrupted.");
            return;
        }

        System.out.println("Program finished successfully.");
    }
}
