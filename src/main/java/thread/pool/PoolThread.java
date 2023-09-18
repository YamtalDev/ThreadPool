package thread.pool;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
/******************************************************************************
 * Represents a worker thread in the thread pool.
 * @implNote The worker thread dequeues and run tasks from the task queue.
******************************************************************************/
public class PoolThread extends Thread
{
    private AtomicBoolean isRunning;
    private PriorityBlockingQueue<PrioritizedTask> taskQueue;

    /**************************************************************************
     * Initializes a new PoolThread with the given name, flag, and tasks queue.
     * @param taskName  The name of the thread.
     * @param isRunning A flag indicating whether the thread should continue running.
     * @param taskQueue The queue of tasks to be executed.
    **************************************************************************/
    public PoolThread(String taskName, AtomicBoolean isRunning, PriorityBlockingQueue<PrioritizedTask> taskQueue)
    {
        super(taskName);
        this.isRunning = isRunning;
        this.taskQueue = taskQueue;
    }

    /**************************************************************************
     * The overridden run method of the worker thread, continuously processes
     * tasks from the task queue while the thread is running.
     *
     * Exception Handling:
     * This method catches any RuntimeException that might occur during task execution
     * and wraps it in a ThreadPoolException. This is done to ensure that any unexpected
     * exceptions occurring within tasks do not cause the thread to abruptly terminate
     * and, instead, propagate to the thread pool's exception handling mechanism.
     *
     * @throws ThreadPoolException If a RuntimeException occurs during task execution.
    **************************************************************************/
    @Override
    public void run() throws ThreadPoolException
    {
        try
        {
            while(true == isRunning.get() || !taskQueue.isEmpty())
            {
                PrioritizedTask task;
                while((task = taskQueue.take()) != null)
                {
                    task.run();
                }
            }
        }
        catch(RuntimeException | InterruptedException error)
        {
            throw new ThreadPoolException(error);
        }
    }
}

