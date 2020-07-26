/*
 * JSimpleSim is a framework to build multi-agent systems in a quick and easy way. This software is published as open
 * source and licensed under the terms of GNU GPLv3.
 * 
 * Contributors: - Rene Kuhlemann - development and initial implementation
 */
package org.simplesim.examples;

import org.simplesim.core.messaging.AbstractPort;
import org.simplesim.core.messaging.Message;
import org.simplesim.core.messaging.SinglePort;
import org.simplesim.core.scheduling.Time;
import org.simplesim.model.AbstractAgent;
import org.simplesim.model.State;

/**
 * Base class for agents providing common functionality and variables
 * <p>
 * This base class can be used for multi-domain models with a routed messaging system.
 * For routing, each agent has a unique address implemented as an {@code int[]} where the index corresponds 
 * to the level of the model tree and the value is a unique id within this level. 
 *
 * @param S type of the agent state containing all state variables
 * @param E type of events
 *
 */
public abstract class BasicAgent<S extends State,E> extends AbstractAgent<S, E> {

	private final AbstractPort inport, outport;

	public BasicAgent(S state, int[] addr) {
		super(state);
		setAddress(addr);
		inport=addInport(new SinglePort(this));
		outport=addOutport(new SinglePort(this));
	}

	@Override
	protected Time doEvent(Time time) {
		processMessages();
		processEvents(time);
		executeStrategy(time);
		return getTimeOfNextEvent();
	}

	/**
	 * Handles the content of a due message.
	 *
	 * @param msg the next message to be handled by the agent
	 */
	protected abstract void handleMessage(Message<int[]> msg);

	/**
	 * Handles a due event.
	 *
	 * @param event the event as such (containing also additional information)
	 * @param time  the time stamp of the event
	 */
	protected abstract void handleEvent(E event, Time time);

	/**
	 * Executes the agent's strategy thus implements the agent's behavior
	 *
	 * @param time the current simulation time
	 */
	protected abstract void executeStrategy(Time time);

	/**
	 * Sends a message via the agent's outport.
	 *
	 * @param dest    the destination of the message
	 * @param content the content of the message
	 */
	protected final void sendMessage(int[] dest, Object content) {
		final Message<int[]> message=new Message<>(getAddress(),dest,content);
		getOutport().write(message);
	}

	protected final AbstractPort getInport() {
		return inport;
	}

	protected final AbstractPort getOutport() {
		return outport;
	}

	@SuppressWarnings("unchecked")
	private void processMessages() {
		while (getInport().hasMessages()) handleMessage(((Message<int[]>) (getInport().poll())));
	}

	private void processEvents(Time time) {
		while (getEventQueue().getMin().equals(time)) handleEvent(getEventQueue().dequeue(),time);
	}

}