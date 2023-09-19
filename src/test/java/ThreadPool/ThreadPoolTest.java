package ThreadPool;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTest
{
    @Test
    public void threadPoolTestDefaultCtor()
    {
        int result = 10;
        ThreadPool pool = new ThreadPool();

        AtomicInteger num = new AtomicInteger(0);
        Runnable task = () -> num.getAndIncrement();

        for(int i = 0; i < result; ++i)
        {
            pool.execute(task);
        }

        pool.stop();
        try
        {
            pool.waitForTermination();
        }
        catch(IllegalStateException | InterruptedException error)
        {
            error.printStackTrace();
        }

        Assert.assertEquals("The AtomicInteger should match the number of executed tasks", result, num.get());
    }

    @Test
    public void threadPoolTestCustomThreadNumber() throws Exception
    {
        int threadCount = 20;
        ThreadPool pool = new ThreadPool(threadCount);
        final AtomicInteger count = new AtomicInteger(0);
        Runnable task = () ->
        {
            count.getAndIncrement();
            try
            {
                Thread.sleep(20);
            }
            catch(Exception error)
            {
                //Do nothing
            }
        };

        for(int i = 0; i < threadCount; ++i)
        {
            pool.execute(task);
        }

        pool.stop();
        try
        {
            pool.waitForTermination();
        }
        catch(IllegalStateException | InterruptedException error)
        {
            error.printStackTrace();
        }

        Assert.assertEquals("All tasks must be executed", threadCount, count.get());
    }

    @Test
    public void threadPoolTestShutDown() throws Exception
    {
        int threadCount = 20;
        ThreadPool pool = new ThreadPool(threadCount);
        final AtomicInteger count = new AtomicInteger(0);
        Runnable task = () -> count.getAndIncrement();

        for(int i = 0; i < threadCount; ++i)
        {
            pool.execute(task);
        }

        try
        {
            pool.shutDown();
        }
        catch(IllegalStateException | InterruptedException error)
        {
            error.printStackTrace();
        }

        Assert.assertEquals("All tasks must be executed", threadCount, count.get());
    }

    @Test(expected = IllegalStateException.class)
    public void threadPoolTestExecutingAfterTermination() throws Exception
    {
        int threadCount = 100;
        ThreadPool pool = new ThreadPool(threadCount);
        final AtomicInteger count = new AtomicInteger(0);
        Runnable task = () ->
        {
            count.getAndIncrement();
            try
            {
                // sleep for 1 sec.
                Thread.sleep(400);
            }
            catch(Exception error)
            {
                // Do nothing
            }
        };
    
        for(int i = 0; i < threadCount; ++i)
        {
            pool.execute(task);
        }
    
        pool.terminate();

        try
        {
            pool.execute(task);
        }
        catch(IllegalStateException error)
        {
            Assert.assertEquals("Exception message should match", "Thread pool is terminating", error.getMessage());
            throw error;
        }

        try
        {
            pool.waitForTermination();
        }
        catch(IllegalStateException | InterruptedException error)
        {
            error.printStackTrace();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void threadPoolTestExecutingAfterExecution() throws Exception
    {
        int threadCount = 50;
        ThreadPool pool = new ThreadPool(threadCount);
        final AtomicInteger count = new AtomicInteger(0);
        Runnable task = () -> count.getAndIncrement();
        for(int i = 0; i < threadCount; ++i)
        {
            pool.execute(task);
        }

        try
        {
            pool.waitForTermination();

        }
        catch(IllegalStateException error)
        {
            Assert.assertEquals("Exception message should match", "Thread pool not terminated", error.getMessage());
            throw error;
        }

        pool.stop();
        try
        {
            pool.waitForTermination();
        }
        catch(IllegalStateException | InterruptedException error)
        {
            error.printStackTrace();
        }
    }

    @Test
    public void threadPoolTestPriorityOrder() throws InterruptedException
    {
        int threadCount = 2;
        ThreadPool pool = new ThreadPool(threadCount);
        AtomicInteger highPriorityCount = new AtomicInteger(0);
        AtomicInteger lowPriorityCount = new AtomicInteger(0);

        String[] strArray = new String[10];
        Runnable highPriorityTask = () ->
        {
            int index = highPriorityCount.getAndIncrement();
            synchronized (strArray) {strArray[index] = "High";}
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException error)
            {
                error.printStackTrace();
            }
        };

        Runnable lowPriorityTask = () ->
        {
            int index = lowPriorityCount.getAndIncrement();
            synchronized (strArray) {strArray[index] = "Low";}
            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException error)
            {
                error.printStackTrace();
            }
        };

        for(int i = 0; i < 10; ++i)
        {
            pool.execute(highPriorityTask, 10);
            pool.execute(lowPriorityTask, 1);
        }

        try
        {
            pool.shutDown();
        }
        catch(IllegalStateException | InterruptedException error)
        {
            error.printStackTrace();
        }

        for(String str: strArray)
        {
            assertEquals("Strings in the array need to be the high priority ones", str, "Low");
        }
    }
}
