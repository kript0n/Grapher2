package com.kripton.grapher.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.util.Log;


public class Pool<T> {
	
	public interface PoolObjectFactory<T> {
		public T createObject();
	}
	
	private final List<T> freeObjects;
	private final PoolObjectFactory<T> factory;
	private final int maxSize;
	
	public Pool(PoolObjectFactory<T> factory, int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);
	}
	
	
	public T newObject() {
		synchronized(this) {
			T object = null;
			if(freeObjects.size() == 0) {
				object = factory.createObject();
			}
			else {
				object = freeObjects.remove(freeObjects.size()-1);
			}
			return object;
		}
	}
	
	
	public void free(T object) {
		if(freeObjects.size() < maxSize) {
			freeObjects.add(object);
		}
	}
	
	
	public void freeList(List<T> list) {
		synchronized(this) {
			ListIterator<T> iter = list.listIterator();
			while(iter.hasNext()) {
				T item = iter.next();
				if(item != null) {
					this.free(item);
				}
			}
			list.clear();
		}	
	}
	
	
	public void logFree() {
		synchronized(this) {
			Log.d("Pool", Integer.toString(freeObjects.size()));
		}
	}

}
