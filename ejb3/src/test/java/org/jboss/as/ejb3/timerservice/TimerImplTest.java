package org.jboss.as.ejb3.timerservice;

import org.jboss.as.ejb3.timerservice.spi.TimedObjectInvoker;
import org.jboss.msc.service.ServiceName;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TimerImplTest {

    @Mock
    TimerServiceImpl timerService;

    @Mock
    private TimedObjectInvoker invoker;


    ExecutorService executorService = Executors.newCachedThreadPool();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(timerService.getInvoker()).thenReturn(invoker);
        when(timerService.getServiceName()).thenReturn(ServiceName.JBOSS);
        when(invoker.getTimedObjectId()).thenReturn("globalId");
    }

    @Test
    public void testLockUnlockNoTokenDifferentThreads() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        TimerImpl timer = TimerImpl.builder().setId("id").build(timerService);


        timer.lock();
        executorService.submit(() -> {
            try {
                timer.unlock();
            } catch (IllegalMonitorStateException e) {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testReentrantLockUnlockDifferentThreads() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        TimerImpl timer = TimerImpl.builder().setId("id").build(timerService);


        Object token1 = timer.lock();
        Object token2 = timer.lock();
        executorService.submit(() -> {
            try {
                timer.unlock(token2);
                timer.unlock(token1);
                latch.countDown();
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testMultipleThread() throws InterruptedException, BrokenBarrierException, TimeoutException {

        int numberOfConcurrentThreads = 100;
        CyclicBarrier barrier = new CyclicBarrier(numberOfConcurrentThreads + 1);
        CyclicBarrier endbarrier = new CyclicBarrier(numberOfConcurrentThreads + 1);
        TimerImpl timer = TimerImpl.builder().setId("id").build(timerService);

        AtomicInteger count = new AtomicInteger(0);

        IntStream.range(0, numberOfConcurrentThreads).forEach(i -> executorService.submit(() -> {
            try {
                barrier.await();

                boolean passed = false;
                timer.lock();
                try {
                    assertTrue(count.compareAndSet(0, 1));
                    Thread.yield();
                    assertTrue(count.compareAndSet(1, 0));
                    passed = true;
                } finally {
                    timer.unlock();
                    if (passed) {
                        endbarrier.await();
                    }
                }

            } catch (Throwable e) {
                System.out.println(e);
            }
        }));

        barrier.await();
        endbarrier.await(5, TimeUnit.SECONDS);

    }

}