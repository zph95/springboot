package com.zph.programmer.springboot.utils;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 */
public class ExecutorUtil {

  private static final ExecutorService threadExec = Executors.newFixedThreadPool(4);

  public static void execute(Runnable runnable) {
    threadExec.execute(runnable);
  }

  public static <T> Future<T> submit(Callable<T> task) {
    return threadExec.submit(task);
  }

  public static void shutdown() {
    threadExec.shutdown();
  }

  public static void shutdownNow() {
    threadExec.shutdownNow();
  }
}
