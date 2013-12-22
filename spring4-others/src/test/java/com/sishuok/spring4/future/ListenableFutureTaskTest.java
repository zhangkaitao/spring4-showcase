package com.sishuok.spring4.future;

import org.junit.Test;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-12-22
 * <p>Version: 1.0
 */
public class ListenableFutureTaskTest {

    @Test
    public void test() throws Exception {
        ListenableFutureTask<String> task = new ListenableFutureTask<String>(new Callable() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(10 * 1000L);
                System.out.println("=======task execute");
                return "hello";
            }
        });

        task.addCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("===success callback 1");
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

        task.addCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("===success callback 2");
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(task);
        String result = task.get();
        System.out.println(result);

    }
}
