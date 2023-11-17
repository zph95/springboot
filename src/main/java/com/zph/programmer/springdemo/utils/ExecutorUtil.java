package com.zph.programmer.springdemo.utils;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import com.zph.programmer.springdemo.dto.BaseResponseDto;

/**
 *
 */
@Slf4j
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

  public static <T> T executeFacade(Supplier<T> execute, String logInfo) {
    try {
      log.info("executeFacade begin: [{}] ", logInfo);
      T t = execute.get();
      log.info("executeFacade end: [{}] {}", logInfo, t);
      return t;
    } catch (Exception e) {
      log.error("executeFacade error!", e);
    }
    return null;

  }

  public static <T> T executeFacadeWithResponse(Supplier<BaseResponseDto<T>> execute, String logInfo) {
    try {
      log.info("executeFacadeWithResponse begin: [{}]", logInfo);
      BaseResponseDto<T> response = execute.get();
      log.info("executeFacadeWithResponse end: [{}] {}", logInfo, response);
      if (response != null && response.isSuccess()) {
        return response.getData();
      }
    } catch (Exception e) {
      log.error("executeFacadeWithResponse error!", e);
    }
    return null;
  }
}
