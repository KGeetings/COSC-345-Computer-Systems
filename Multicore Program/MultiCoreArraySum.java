import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* 
 * Using the example program that was presented, write a Java program that will use multiple
    cores to add the elements of an array of int values.
 * Your main method should create the array and randomly generate the values for the array.
    It should then pass that array to an object that is created to handle the computation.
 * Classes you will create: 
    • The class that handles the computation will be similar to the Multicore class in the 
    example.  This class should retrieve the sums from the Future’s and add those together 
    to get the overall sum.  You can do most of the computation in the constructor method 
    the way my example does, or you can not have the constructor do much and put the 
    code in a method that you call in main after creating the instance of this class. 
    • You will also need to create a class similar to the Compute class that implements the 
    Callable interface.  This class should use it’s ID, the number of processors involved, and 
    the size of the array to determine which part of the array it is supposed to sum.   The 
    call method of this class should return its sum 
*/

public class MultiCoreArraySum {

    private int base;

    public MultiCoreArraySum() throws ExecutionException, InterruptedException {
        base = 4;

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Available processor cores is " + cores);

        Instant now = Instant.now();
        ExecutorService threadPool = Executors.newWorkStealingPool();
        
        List<Future<Integer>> futures = new ArrayList<>(cores);

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

        now = Instant.now();
        total = 0;
        for (int i = 0; i < cores * base; i++) {
            total += i;
        }
        System.out.println("Total: " + total);
        d = Duration.between(now, Instant.now());
        System.out.println("Time Taken single-core: " + d); // Total time taken
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new MultiCoreArraySum();
    }

    private class Compute implements Callable<Integer> {
        private int id;

        public Compute(int id) {
            this.id = id;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println("Id: " + id);
            int sum = 0;
            for (int i = id * base; i < (id + 1) * base; i++) {
                sum += i;
            }
            System.out.println("Id: " + id + " n: " + sum);
            return sum;
        }
    }
}