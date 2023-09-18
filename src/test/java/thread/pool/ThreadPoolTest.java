package thread.pool;

import org.junit.Test;
import org.junit.Assert;
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

        Assert.assertEquals("The AtomicInteger should match the number of executeted tasks", result, num.get());
    }

    @Test
    public void ThreadPoolTestCustomThreadNumber() throws Exception
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

        for(int i = 0; i < threadCount; i++)
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
    public void ThreadPoolTestTerminate() throws Exception
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
                Thread.sleep(100);
            }
            catch(Exception error)
            {
                //Do nothing
            }
        };

        for(int i = 0; i < threadCount; i++)
        {
            pool.execute(task);
        }
        
        pool.terminate();
        try
        {
            pool.waitForTermination();
        }
        catch(IllegalStateException | InterruptedException error)
        {
            error.printStackTrace();
        }

        Assert.assertTrue("Thread pool should terminate without executing pending tasks", threadCount != count.get());
    }

    @Test(expected = IllegalStateException.class)
    public void ThreadPoolTestExecutingAfterTermination() throws Exception
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
                Thread.sleep(100);
            }
            catch(Exception error)
            {
                // Do nothing
            }
        };
    
        for(int i = 0; i < threadCount; i++)
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
            Assert.assertTrue("Task count should not change after termination", threadCount != count.get());
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
    public void ThreadPoolTestExecutingAfterExecution() throws Exception
    {
        int threadCount = 50;
        ThreadPool pool = new ThreadPool(threadCount);
        final AtomicInteger count = new AtomicInteger(0);
        Runnable task = () -> count.getAndIncrement();
        for(int i = 0; i < threadCount; i++)
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
}
