import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiCoreArraySum2 {

    private int[] arr;
    private int numCores;
    private ExecutorService threadPool;
    private Future<Integer>[] futures;

    public MultiCoreArraySum2(int[] arr) {
        this.arr = arr;
        this.numCores = Runtime.getRuntime().availableProcessors();
        this.threadPool = Executors.newWorkStealingPool();
        this.futures = new Future[numCores];
    }

    public int sum() {
        int chunkSize = arr.length / numCores;
        int totalSum = 0;
        for (int i = 0; i < numCores; i++) {
            int start = i * chunkSize;
            int end = (i == numCores - 1) ? arr.length : (i + 1) * chunkSize;
            futures[i] = threadPool.submit(new Compute(start, end));
        }
        for (int i = 0; i < numCores; i++) {
            try {
                totalSum += futures[i].get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
        return totalSum;
    }

    private class Compute implements Callable<Integer> {
        private int start, end;

        public Compute(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Integer call() throws Exception {
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += arr[i];
            }
            return sum;
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[1000];
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(100);
        }

        MultiCoreArraySum2 sum = new MultiCoreArraySum2(arr);

        long start = System.currentTimeMillis();
        int totalSum = sum.sum();
        long end = System.currentTimeMillis();

        System.out.println("Total sum: " + totalSum);
        System.out.println("Time taken: " + (end - start) + " ms");
    }
}
