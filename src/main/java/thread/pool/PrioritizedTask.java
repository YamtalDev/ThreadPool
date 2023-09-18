package thread.pool;

/******************************************************************************
 * A wrapper class for tasks that adds a priority level. Instances of this class
 * allow tasks to be executed with different priorities within the thread pool.
 * By implementing the Comparable interface with type parameter <PrioritizedTask>,
 * enable the comparison of tasks based on their priorities, ensuring that
 * higher-priority tasks are executed before lower-priority ones when added to a
 * PriorityBlockingQueue.
******************************************************************************/
public class PrioritizedTask implements Runnable, Comparable<PrioritizedTask>
{
    private Runnable task;
    private int priority;

    /**************************************************************************
     * Constructs a new PrioritizedTask with the specified task and priority.
     * @param task The underlying task to be executed.
     * @param priority The priority level assigned to the task (1 to 10).
     * @throws IllegalArgumentException If the priority is not within the valid range.
    **************************************************************************/
    public PrioritizedTask(Runnable task, int priority) throws IllegalArgumentException
    {
        if(priority < 1 || priority > 10)
        {
            throw new IllegalArgumentException("Priority must be between 1 and 10");
        }

        this.task = task;
        this.priority = priority;
    }

    /**************************************************************************
     * Get the priority of the task.
     * @return The priority level of the task (1 to 10).
    **************************************************************************/
    private int getPriority()
    {
        return (priority);
    }

    /*************************************************************************
     * compareTo Compares this PrioritizedTask with another PrioritizedTask to 
     * establish their relative execution priorities within a PriorityBlockingQueue. 
     * Tasks with higher priority values will be dequeued and executed first.
     * @param other The other PrioritizedTask to compare with.
     * @return A negative integer, zero, or a positive integer, depending on whether
     * this task's priority is less than, equal to, or greater than the other task's priority.
    **************************************************************************/
    @Override
    public int compareTo(PrioritizedTask other)
    {
        return (Integer.compare(getPriority(), other.getPriority()));
    }

    /**************************************************************************
     * Executes the underlying task associated with this PrioritizedTask.
    **************************************************************************/
    @Override
    public void run()
    {
        task.run();
    }
}