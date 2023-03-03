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
        /*
         * The class that handles the computation will be similar to the Multicore class
         * in the example. This class should retrieve the sums from the Future’s and add
         * those together to get the overall sum. You can do most of the computation in
         * the constructor method the way my example does,
         * or you can not have the constructor do much and put the code in a method
         * that you call in main after creating the instance of this class.
         */

        int cores = Runtime.getRuntime().availableProcessors();
        // int numWorkers = 100;
        System.out.println("Available processor cores is " + cores);

        Instant now = Instant.now();
        ExecutorService threadPool = Executors.newWorkStealingPool();

        LinkedList<Future<Integer>> futures = new LinkedList<Future<Integer>>();

        for (int i = 0; i < cores; i++) {
            futures.add(i, threadPool.submit(new Compute(i, cores, array)));
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
        /*
         * Your main method should create the array and randomly generate the values for
         * the array.
         * It should then pass that array to an object that is created to handle the
         * computation.
         */
        int[] array = new int[10000000];

        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(100);
        }

        new MultiCoreArraySum(array);
    }

    private class Compute implements Callable<Integer> {
        /*
         * You will also need to create a class similar to the Compute class that
         * implements the Callable interface. This class should use it’s ID, the number
         * of processors involved, and the size of the array to determine which part of
         * the array it is supposed to sum. The call method of this class should return
         * its sum
         */
        private int id;
        private int cores;
        private int[] array;

        public Compute(int id, int cores, int[] array) {
            this.id = id;
            this.cores = cores;
            this.array = array;
        }

        @Override
        public Integer call() throws Exception {
            int sum = 0;
            int size = array.length / cores;
            int start = id * size;
            int end = start + size;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            System.out.println("Thread " + id + " sum: " + sum);
            return sum;
        }
    }
}

// Responses
/* 
1. Start with a fairly small array – say 1000 int values. This will likely be faster in the single process version. Keep increasing the size of the array – can you determine a point where the multiprocessor approach becomes faster?
    The multicore approach becomes faster when the array size is greater than 100000000.

2. Try generating more tasks than the number of available processors. Is this code slower or faster than when you match the number of tasks to the number of processors?
    The code is slower when the number of tasks is greater than the number of processors, but the difference isn't a lot.

3. If you are able, run your code on a different computer that has a different number of cores. Do your results from the first experiment change at all?
    The results from the first experiment do not change. I attempted this on my laptop with 8 cores, on my desktop with 16 cores.
*/