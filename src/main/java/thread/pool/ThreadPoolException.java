package thread.pool;

/******************************************************************************
 * Custom exception class for the ThreadPool.
 * @param cause The cause of this exception, typically an underlying exception that occurred.
 ******************************************************************************/
public class ThreadPoolException extends RuntimeException
{
    public ThreadPoolException(Throwable cause)
    {
        super(cause);
    }
}

