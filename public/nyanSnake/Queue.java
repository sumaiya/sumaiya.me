// Name: Sumaiya Hashmi

// A generic queue class
//
// The class Queue<T> implements QueueInterface<T>,
// which guarantees the methods
//    isEmpty()
//    peek()
//    dequeue()    
//    enqueue(...)    and
//    toString()

class Queue<T extends Object>      // i.e., The input T can be any class
    extends Object                 // i.e., a Queue<T> is also an Object
    implements QueueInterface<T>   // i.e., Queue<T> supports all the
                                   //       methods listed in QueueInterface<T>
{

    /* QCell is an inner class that Queue uses to
     * wrap its data in a linked list. Nobody
     * outside this class should see or care about
     * QCell objects, in case we want to make
     * changes... */
    private class QCell
    {
        private T data;
        private QCell next;

        private QCell(T data)
        {
            this.data = data;
            this.next = null;
        }
    }

    /* the two data members of each Queue object,
       namely, a reference to the frontmost and backmost cells.
     */ 
	private QCell front; // the front of the Queue object - cells are removed from here
	private QCell back;  // the back of the Queue object - cells are added to here
	

    /* constructor
     * creates an empty queue
     */
    public Queue()
    {
	this.front = null;
	this.back = null;
    }
    
    /* method: isEmpty
     * inputs: none
     * outputs: a boolean - whether or not this is empty
     */
    public boolean isEmpty()
    {
        return (this.front == null) && (this.back == null);
    }

    /* method: enqueue
     * inputs: an object to store at the back of the queue
     * outputs: none
     */
    public void enqueue(T data)
    {
        QCell newBack = new QCell(data);
        // The new back is next, comes after current back.
        //newBack = this.back.next;
        if(this.isEmpty()){
        	this.front = newBack;
        	this.back = newBack;
        }
        else{
        	this.back.next = newBack;
        	this.back = newBack;
    }
    }

    /* method: dequeue
     * inputs: none
     * outputs: the data that was at the front (null if empty)
     *        dequeue also removes the front QCell
     */ 
    public T dequeue()
    {
        if (this.isEmpty())
            {
                System.out.println("You can't dequeue from an empty Queue!");
                return null;
            }
        else if (this.front == this.back){
        	T data = this.front.data;
        	this.front = null;
        	this.back = null;
        	return data;
        }
        else{
        T data = this.front.data;
        this.front = this.front.next;
        return data;
    }
    }

    /* method: peek
     * inputs: none
     * outputs: the data that was at the front (null if empty)
     *        peek does not alter the calling queue 
     */ 
    public T peek()
    {
        if (isEmpty())
            {
                System.out.println("You can't peek at an empty Queue!");
                return null;
            }
        T data = front.data;
        return data;
    }

    /* method: toString
     * inputs: none
     * outputs: the usual
     */
    public String toString()
    {
        StringBuffer accum = new StringBuffer( "<FRONT> " );
      
        // Loop through all the cells. Convert each
        //   data item to a string, and add it to
        //   our running accumulator.
      
        QCell current = this.front;
        while (current != null)
            {
                accum.append(current.data.toString() + " ");
                current = current.next; 
            } 
      
        accum.append( "<BACK>" );
      
        // Convert the accumulated characters to 
        //     an (immutable) String and return.
        return accum.toString ();
    }

    /* method: main
     * inputs: the usual
     * outputs: none - a place for testing...
     */
    public static void main(String[] args)
    {
        Queue<String> S = new Queue<String>();
        System.out.println("S is " + S);
        S.enqueue("Will");
        System.out.println("S is " + S);
        S.enqueue("this");
        System.out.println("S is " + S);
        S.enqueue("work?");
        System.out.println("S is " + S);
        System.out.println("peek returns" + S.peek());
        S.dequeue();
        System.out.println("S is " + S);
        S.dequeue();
        System.out.println("S is " + S);
        S.dequeue();
        System.out.println("S is " + S);
        S.dequeue();
        System.out.println("Is Empty?" + S.isEmpty());
        System.out.println("S is " + S);
        S.enqueue("Will");
        System.out.println("S is " + S);
        System.out.println("Is Empty?" + S.isEmpty());
        S.enqueue("this");
        System.out.println("S is " + S);
        S.enqueue("work?");
        System.out.println("S is " + S);
    }
}




