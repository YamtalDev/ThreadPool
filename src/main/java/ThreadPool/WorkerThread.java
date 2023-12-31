package ThreadPool;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.PriorityBlockingQueue;
/******************************************************************************
 * Represents a worker thread in the thread pool.
 * @implNote The worker thread dequeues and run tasks from the task queue.
******************************************************************************/
public class WorkerThread extends Thread
{
    private AtomicBoolean isRunning;
    private PriorityBlockingQueue<PrioritizedTask> taskQueue;

    /**************************************************************************
     * Initializes a new WorkerThread with the given name, flag, and tasks queue.
     * @param taskName  The name of the thread.
     * @param isRunning A flag indicating whether the thread should continue running.
     * @param taskQueue The queue of tasks to be executed.
    **************************************************************************/
    public WorkerThread(String taskName, AtomicBoolean isRunning, PriorityBlockingQueue<PrioritizedTask> taskQueue)
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
        boolean interrupted = false;
        try
        {
            while(!interrupted && (isRunning.get() || !taskQueue.isEmpty()))
            {
                PrioritizedTask task;
                interrupted = Thread.currentThread().isInterrupted();
                while(null != (task = taskQueue.poll()))
                {
                    task.run();
                }
            }
        }
        catch(RuntimeException error)
        {
            throw new ThreadPoolException(error);
        }
    }
}

