/*
 * QueueInterface.java 
 *
 * provides an generic interface for all of the functions
 * a Queue of T's should implement.
 *
 * That is, if a class implements the interface
 * QueueInterface<T>, it can then be used
 * as a queue of T's...
 */

interface QueueInterface<T extends Object>
{
  public boolean isEmpty();
  public T       peek();
  public T       dequeue();
  public void    enqueue( T data );
  public String  toString();
}

