package cs212.utils;

import java.util.ArrayList;

public class WorkQueue {
	
    private final int nThreads;
    private final PoolWorker[] threads;
    private final ArrayList<Runnable> queue;
    private volatile boolean shutdown;

    public WorkQueue(int nThreads)
    {
        this.nThreads = nThreads;
        queue = new ArrayList<Runnable>();
        threads = new PoolWorker[nThreads];
        shutdown = false;
        
        for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    public void execute(Runnable r) {
        synchronized(queue) {
            queue.add(r);
            queue.notify();
        }
    }

    private class PoolWorker extends Thread {
        public void run() {
            Runnable r;

            while (true) {
                synchronized(queue) {
                    while (queue.isEmpty() && !shutdown) {
                        try
                        {
                            queue.wait();//only wait when queue is empty and have not been shutdown
                        }
                        catch (InterruptedException ignored)
                        {
                        }
                    }
                    if(shutdown && queue.isEmpty()){
                    	break;
                    }
                    
                    r = (Runnable) queue.remove(0);
                }

                // If we don't catch RuntimeException, 
                // the pool could leak threads!
                
            	try {
                    r.run();
                }
                catch (RuntimeException e) {
                    // You might want to log something here
                } 
                  
            }
            
            
        }
    }
    
    
    public void shutdown(){//stop accepting new jobs
    	shutdown = true; //atomic operation
    }
    
   
    public void awaitTermination(){//wait till everyone is finished
    
    	for (int i=0; i<nThreads; i++) {
    		 try {
            	threads[i].join();//join, wait until entire run method has finished execution
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
}


