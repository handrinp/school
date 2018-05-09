import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.IntConsumer;

public class ThreadPool {
  private int size;
  private Thread[] workers;
  private LinkedBlockingQueue<BooleanTask> tasks;

  public ThreadPool(int size) {
    this.size = size;
    workers = new Thread[size];
    tasks = new LinkedBlockingQueue<>();

    for (int i = 0; i < workers.length; ++i) {
      final int threadNum = i;

      workers[i] = new Thread(() -> {
        boolean alive = true;

        while (alive) {
          try {
            alive = tasks.take().run(threadNum);
          } catch (InterruptedException e) { }
        }
      });

      workers[i].start();
    }
  }

  public void schedule(IntConsumer r) {
    boolean inserted = false;

    while (!inserted) {
      try {
        tasks.put(n -> {
          r.accept(n);
          return true;
        });

        inserted = true;
      } catch (InterruptedException e) { }
    }
  }

  public void stop() {
    for (int i = 0; i < size; ++i) {
      final int threadNum = i;
      boolean inserted = false;

      while (!inserted) {
        try {
          tasks.put(n -> {
            System.out.println("Thread " + threadNum + " stopped.");
            return false;
          });

          inserted = true;
        } catch (InterruptedException e) { }
      }
    }

    for (int i = 0; i < size; ++i) {
      boolean joined = false;

      while (!joined) {
        try {
          workers[i].join();
          joined = true;
        } catch (InterruptedException e) { }
      }
    }
  }
}

