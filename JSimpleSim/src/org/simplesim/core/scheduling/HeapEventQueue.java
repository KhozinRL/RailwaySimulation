/*
 * JSimpleSim is a framework to build multi-agent systems in a quick and easy
 * way.
 *
 * This software is published as open source and licensed under the terms of GNU GPLv3.
 */
package org.simplesim.core.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Priority queue implementation of the {@link IEventQueue} interface. <p/>
 * Adapter class wrapping a {@link PriorityQueue} to hold the data, internally stored
 * in a binary heap structure. Most operations are done in O(log n), but searches
 * like {@code dequeue(E)} and {@code getTime(E)} take O(n). Look up of the minimal time
 * {@code getMin()} is done in O(1) but dequeuing has a complexity of O(log n).
 *
 * @author Rene Kuhlemann
 * @param <E> event type
 */
public class HeapEventQueue<E> extends AbstractEventQueue<E, PriorityQueue<EventQueueEntry<E>>> {

	/**
	 * Class constructor taking the cache size as parameter
	 * 
	 */
	public HeapEventQueue() {
		super(new PriorityQueue<EventQueueEntry<E>>());
	}

	/* (non-Javadoc)
	 * @see org.simplesim.core.scheduling.IEventQueue#getMin()
	 */
	@Override
	public Time getMin() {
		return getQueue().peek().getTime();
	}

	/* (non-Javadoc)
	 * @see org.simplesim.core.scheduling.IEventQueue#dequeue()
	 */
	@Override
	public E dequeue() {
		return getQueue().poll().getEvent();
	}

	/* (non-Javadoc)
	 * @see org.simplesim.core.scheduling.IEventQueue#dequeueAll(org.simplesim.core.scheduling.Time)
	 */
	@Override
	public List<E> dequeueAll(Time time) {
		if (isEmpty()) return Collections.emptyList();
		final List<E> result=new ArrayList<>();
		final Iterator<EventQueueEntry<E>> iterator=getQueue().iterator();
		while (iterator.hasNext()) {
			EventQueueEntry<E> entry=iterator.next();
			if (entry.getTime().equals(time)) {
				do { // copy all event queue entries with given time stamp to result list
					result.add(entry.getEvent());
					iterator.remove();
				} while (iterator.hasNext()&&(entry=iterator.next()).getTime().equals(time));
				return result;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.simplesim.core.scheduling.IEventQueue#dequeueAll()
	 */
	@Override
	public List<E> dequeueAll() {
		return dequeueAll(getMin());
	}

}
