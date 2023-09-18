package thread.pool;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.PriorityBlockingQueue;

/******************************************************************************
 * A Fixed-size thread pool for executing tasks concurrently.
 * Tasks are submitted as Runnable instances and executed by worker threads.
 * @see PoolThread
******************************************************************************/
public class ThreadPool
{
    private AtomicBoolean isRunning;
    private List<PoolThread> threads;
    private PriorityBlockingQueue<PrioritizedTask> taskQueue;

    /**************************************************************************
     * Constructs a ThreadPool with a specified number of worker threads.
     * @param nThreads The number of worker threads in the pool.
    **************************************************************************/
    public ThreadPool(int nThreads)
    {
        isRunning = new AtomicBoolean(true);
        threads = new ArrayList<PoolThread>(nThreads);
        taskQueue = new PriorityBlockingQueue<PrioritizedTask>();

        for(int i = 0; i < nThreads; ++i)
        {
            PoolThread thread = new PoolThread("WorkerThread - " + i, isRunning, taskQueue);
            thread.start();
            threads.add(thread);
        }
    }

    /**************************************************************************
     * Default ThreadPool constructor with a default number of worker threads
     * based on the available processors on the machine.
    **************************************************************************/
    public ThreadPool()
    {
        this(Runtime.getRuntime().availableProcessors());
    }

    /**************************************************************************
     * Stops accepting new tasks and gracefully shuts down the thread pool by 
     * setting the isRunning flag to false.
    **************************************************************************/
    public void stop()
    {
        isRunning.set(false);
    }

    /**************************************************************************
     * Immediately terminates the thread pool, discarding pending tasks.
     * Worker threads are interrupted to ensure they stop if blocked on a task.
     * @throws InterruptedException
    **************************************************************************/
    public void terminate()
    {
        taskQueue.clear();
        isRunning.set(false);
    
        for(PoolThread thread : threads)
        {
            thread.interrupt();
        }
    }

    /**************************************************************************
     * Submits a task to be executed by a worker thread.
     * @param task The task to be executed.
     * @param priority The task priority of execution.
     * @throws IllegalStateException If the thread pool is in the process of terminating.
     * @throws IllegalArgumentException If the priority is not within the valid range.
    **************************************************************************/
    public void execute(Runnable task, int priority) throws IllegalStateException, IllegalArgumentException
    {
        if(isRunning.get())
        {
            taskQueue.add(new PrioritizedTask(task, priority));
        }
        else
        {
            throw new IllegalStateException("Thread pool is terminating");
        }
    }

    /**************************************************************************
     * Submits a task to be executed by a worker thread with a default level 5 priority.
     * @param task The task to be executed.
    **************************************************************************/
    public void execute(Runnable task) throws IllegalStateException, IllegalArgumentException
    {
        execute(task, 5);
    }

    /**************************************************************************
     * Waits for the thread pool to terminate by waiting for all worker threads to finish.
     * @throws InterruptedException If the current thread is interrupted while waiting.
     * @throws IllegalStateException If the thread pool is still running.
    **************************************************************************/
    public void waitForTermination() throws InterruptedException, IllegalStateException
    {
        if(isRunning.get())
        {
            throw new IllegalStateException("Thread pool not terminated");
        }

        for(PoolThread thread: threads)
        {
            thread.join();
        }
    }

    /**************************************************************************
     * Shut down the thread pool by stopping all worker threads and waiting for finish.
     * @throws InterruptedException If the current thread is interrupted while waiting.
     * @throws IllegalStateException If the thread pool is still running.
    **************************************************************************/
    public void shutDown() throws IllegalStateException, InterruptedException
    {
        stop();
        waitForTermination();
    }
}
