import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
public class MulticoreExample {

   private int base;

   public MulticoreExample() throws ExecutionException, InterruptedException {
      base = 4;

      int cores = Runtime.getRuntime().availableProcessors();
      // int numWorkers = 100;
      System.out.println("Available processor cores is " + cores);

      Instant now = Instant.now();
      ExecutorService threadPool = Executors.newWorkStealingPool();

      LinkedList<Future<Integer>> futures = new LinkedList<Future<Integer>>();

      for (int i = 0; i < cores; i++) {
         futures.add(i, threadPool.submit(new Compute(i)));
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
      int n = 0;
      for (int i = 0; i < cores; i++) {
         n = 0;
         for (int j = 0; j < 10000000; j++) {
            n++;
         }
         n = (int) Math.pow(base, i);
         total += n;
      }
      System.out.println(total);
      d = Duration.between(now, Instant.now());
      System.out.println("Time Taken:  " + d);
   }

   public static void main(String[] args) throws ExecutionException, InterruptedException {
      MulticoreExample me = new MulticoreExample();
   }

   private class Compute implements Callable<Integer> {
      private int id;

      public Compute(int id) {
         this.id = id;
      }

      @Override
      public Integer call() {
         System.out.println("Id: " + id);
         // just waste some time
         int n = 0;
         for (int i = 0; i < 10000000; i++) {
            n++;
         }
         // do the calculation
         n = (int) Math.pow(base, id);
         System.out.println("Id: " + id + " n: " + n);
         return n;
      }
   }
}