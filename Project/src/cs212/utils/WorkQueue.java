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
    	if(!shutdown){
	        synchronized(queue) {
	            queue.add(r);
	            queue.notify();
	        }
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
                            queue.wait();
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

               
            	try {
                    r.run();
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                } 
                  
            }
            
            
        }
    }
    
    
    public void shutdown(){
    	shutdown = true; 
    	synchronized(queue){
    		queue.notifyAll();
    	}
    	
    }
    
   
    public void awaitTermination(){
    	for (int i=0; i<nThreads; i++) {
    		 try {
            	threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
}


