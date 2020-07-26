/*
 * JSimpleSim is a framework to build multi-agent systems in a quick and easy way. This software is published as open
 * source and licensed under the terms of GNU GPLv3. Contributors: - Rene Kuhlemann - development and initial
 * implementation
 */
package org.simplesim.model;

import java.util.Collections;

import org.simplesim.core.messaging.AbstractPort;
import org.simplesim.core.messaging.SinglePort;
import org.simplesim.core.scheduling.EventQueue;

/**
 * Extension of an {@code AbstractAgent} to enable massage routing.
 * <p>
 * When using message routing, there is no direct connection between source and destination. Rather the address is
 * specified in the message envelope. This can be used to route the message to the right destination. So the agent needs
 * only one inport and one outport. These are connected to the parent domain which does the actual routing. Therefore,
 * the parent must be a {@code RoutingDomain}.
 * <p>
 * Note: Connection of ports is done automatically when adding this agent to a {@code RoutingDomain}.
 *
 * @see AbstractAgent
 * @see RoutingDomain
 * @see org.simplesim.core.messaging.RoutedMessage RoutedMessage
 */
public abstract class RoutingAgent<S extends State, E> extends AbstractAgent<S, E> {

	/**
	 * {@inheritDoc}
	 */
	public RoutingAgent(EventQueue<E> queue, S s) {
		super(queue,s);
		init();
	}

	/**
	 * {@inheritDoc}
	 */
	public RoutingAgent(S s) {
		super(s);
		init();
	}

	private void init() {
		setInportList(Collections.singletonList(new SinglePort(this)));
		setOutportList(Collections.singletonList(new SinglePort(this)));
	}

	public AbstractPort getOutport() {
		return getOutport(0);
	}

	public AbstractPort getInport() {
		return getInport(0);
	}

}