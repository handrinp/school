import java.util.Random;

public class Main {
  public static void main(String[] args) {
    ThreadPool pool = new ThreadPool(Runtime.getRuntime().availableProcessors());

    for (int task = 0; task < 15; ++task) {
      pool.schedule(n -> {
        int N = new Random().nextInt(5_000_000);
        System.out.println("Started running on thread " + n);

        for (int i = 0; i < N; ++i) {
          double x = Math.cos(i * Math.PI / 3);
        }

        System.out.println("Finished running on thread " + n);
      });
    }

    pool.stop();
    System.out.println("I am done!");
  }
}

