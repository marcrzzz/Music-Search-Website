package cs212.utils;

import java.util.HashMap;

public class ReentrantLock {

	private HashMap<Long, Integer> readMap;
	private HashMap<Long, Integer> writeMap;
	
	/**
	 * Construct a new ReentrantLock.
	 */
	public ReentrantLock(){
		this.readMap = new HashMap<>();
		this.writeMap = new HashMap<>();
	}

	/**
	 * Returns true if the invoking thread holds a read lock.
	 * @return
	 */
	public synchronized boolean hasRead() {
		Long id = Thread.currentThread().getId();
		if(readMap.containsKey(id) && readMap.get(id) > 0){
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the invoking thread holds a write lock.
	 * @return
	 */
	public synchronized boolean hasWrite() {
		Long id = Thread.currentThread().getId();
		if(writeMap.containsKey(id) && writeMap.get(id) > 0){
			return true;
		}
		return false;
	}

	/**
	 * Non-blocking method that attempts to acquire the read lock.
	 * Returns true if successful.
	 * @return
	 */
	public synchronized boolean tryLockRead() {
		if(writeMap.size() == 0 || hasWrite()){
			Long id = Thread.currentThread().getId();
			if(!readMap.containsKey(id)){
				readMap.put(id, 0);
			}
			int counter = readMap.get(id);
			readMap.put(id, ++counter);
			return true;
		}
		return false;
	}

	/**
	 * Non-blocking method that attempts to acquire the write lock.
	 * Returns true if successful.
	 * @return
	 */	
	public synchronized boolean tryLockWrite() {
		if((writeMap.size() == 0 && readMap.size() == 0) || hasWrite() ){
			Long id = Thread.currentThread().getId();
			if(!writeMap.containsKey(id)){
				writeMap.put(id, 0);
			}
			int counter = writeMap.get(id);
			writeMap.put(id, ++counter);
			return true;
		}
		return false;
	}

	/**
	 * Blocking method that will return only when the read lock has been 
	 * acquired.
	 *
	 */	 
	public synchronized void lockRead() {
		while(!tryLockRead()){
			try {
				wait();
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Releases the read lock held by the calling thread. Other threads may continue
	 * to hold a read lock.
	 */
	public synchronized void unlockRead() {
		Long id = Thread.currentThread().getId();
		if(readMap.get(id) == 1){
	    	readMap.remove(id);
	    }
	    else{
	    	int counter = readMap.get(id);
	    	readMap.put(id, --counter);
	    }
	    if(readMap.size() == 0){
	    	notifyAll();
	    }
		
	}

	/**
	 * Blocking method that will return only when the write lock has been 
	 * acquired.
	 */
	public synchronized void lockWrite() {
		while(!tryLockWrite()){
			try {
				wait();
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Releases the write lock held by the calling thread. The calling thread may continue to hold
	 * a read lock.
	 */
	public synchronized void unlockWrite() {
		Long id = Thread.currentThread().getId();
		if(writeMap.get(id) == 1){
	    	writeMap.remove(id);
	    }
	    else{
	    	int counter = writeMap.get(id);
	    	writeMap.put(id, --counter);
	    }
		if(writeMap.size() == 0 && readMap.size() ==0){
			notifyAll();
		}
	}

}
