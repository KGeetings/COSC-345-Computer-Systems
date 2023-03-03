import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Multicore Processor Example in Java to use all available processor cores
 * to average lists of random numbers.
 * Uses the Java 8's Executor service for a Work Stealing Pool to divvy the
 * work onto the available processor cores.
 * Example was run on a 8 Core 2.6 GHz Intel i7 with 16GB of 2133 LPDDR3
 * which takes around 14 seconds;
 */
public class MultiCoreArraySum {

    public MultiCoreArraySum(int[] array) throws ExecutionException, InterruptedException {

        int cores = Runtime.getRuntime().availableProcessors();
        // int numWorkers = 100;
        System.out.println("Available processor cores is " + cores);

        Instant now = Instant.now();
        ExecutorService threadPool = Executors.newWorkStealingPool();

        LinkedList<Future<Integer>> futures = new LinkedList<Future<Integer>>();

        for (int i = 0; i < cores; i++) {
            futures.add(i, threadPool.submit(new Compute(i, array)));
        }

        int total = 0;
        for (int i = 0; i < cores; i++) {
            total += futures.get(i).get();
        }

        System.out.println("Total: " + total);

        Duration d = Duration.between(now, Instant.now());
        System.out.println("Time Taken multi-core:  " + d); // Total time taken

        // Same computation without cores
        now = Instant.now();
        System.out.println("No multicore");
        total = 0;
        for (int i = 0; i < array.length; i++) {
            total += array[i];
        }
        System.out.println(total);
        d = Duration.between(now, Instant.now());
        System.out.println("Time Taken:  " + d);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /* Your main method should create the array and randomly generate the values for the array. 
        It should then pass that array to an object that is created to handle the computation. */
        int[] array = new int[10000000];

        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(100);
        }

        new MultiCoreArraySum(array);
    }

    private class Compute implements Callable<Integer> {
        private int id;

        public Compute(int id, int[] array) {
            this.id = id;
        }

        @Override
        public Integer call() {
            System.out.println("Id: " + id);
            int sum = 0;
            for (int i = 0; i < array.length; i++) {
                sum += array[i];
            }
            return sum;
        }
    }
}