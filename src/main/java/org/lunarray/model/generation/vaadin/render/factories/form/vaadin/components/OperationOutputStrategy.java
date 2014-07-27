/* 
 * Model Tools.
 * Copyright (C) 2013 Pal Hargitai (pal@lunarray.org)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components;

import java.lang.reflect.InvocationTargetException;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.common.event.Bus;
import org.lunarray.common.event.EventException;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.model.operation.OperationDescriptor;
import org.lunarray.model.descriptor.model.operation.result.ResultDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationOperationDescriptor;
import org.lunarray.model.descriptor.util.OperationInvocationBuilder;
import org.lunarray.model.generation.vaadin.render.factories.form.events.OperationInvocationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A operation output strategy.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The type.
 */
public final class OperationOutputStrategy<E>
		implements ClickListener {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(OperationOutputStrategy.class);
	/** Serial id. */
	private static final long serialVersionUID = -6956543501583704039L;
	/** The builder. */
	private OperationInvocationBuilder<E> builder;
	/** An event bus. */
	private Bus bus;
	/** The button. */
	private Button button;
	/** The descriptor. */
	private OperationDescriptor<E> descriptor;
	/** The label. */
	private Label label;
	/** The presentation descriptor. */
	private PresentationOperationDescriptor<E> presentationDescriptor;

	/**
	 * Default constructor.
	 * 
	 * @param descriptor
	 *            The descriptor.
	 * @param builder
	 *            The builder.
	 * @param bus
	 *            The event bus.
	 */
	@SuppressWarnings("unchecked")
	public OperationOutputStrategy(final OperationDescriptor<E> descriptor, final OperationInvocationBuilder<E> builder, final Bus bus) {
		Validate.notNull(descriptor, "Descriptor may not be null.");
		Validate.notNull(builder, "Builder may not be null.");
		Validate.notNull(bus, "Event bus may not be null.");
		this.descriptor = descriptor;
		this.presentationDescriptor = descriptor.adapt(PresentationOperationDescriptor.class);
		this.builder = builder;
		String text;
		String buttonText;
		if (CheckUtil.isNull(this.presentationDescriptor)) {
			text = descriptor.getName();
			buttonText = descriptor.getName();
		} else {
			text = this.presentationDescriptor.getDescription();
			buttonText = this.presentationDescriptor.getButton();
		}
		this.label = new Label(text);
		this.button = new Button(buttonText);
		this.button.addListener(this);
		this.bus = bus;
	}

	/** {@inheritDoc} */
	@Override
	public void buttonClick(final ClickEvent event) {
		// Button pressed.
		this.execute(this.descriptor.getResultDescriptor());
	}

	/**
	 * Gets the value for the builder field.
	 * 
	 * @return The value for the builder field.
	 */
	public OperationInvocationBuilder<E> getBuilder() {
		return this.builder;
	}

	/**
	 * Gets the value for the bus field.
	 * 
	 * @return The value for the bus field.
	 */
	public Bus getBus() {
		return this.bus;
	}

	/**
	 * Gets the value for the button field.
	 * 
	 * @return The value for the button field.
	 */
	public Button getButton() {
		return this.button;
	}

	/**
	 * Gets the value for the descriptor field.
	 * 
	 * @return The value for the descriptor field.
	 */
	public OperationDescriptor<E> getDescriptor() {
		return this.descriptor;
	}

	/**
	 * Gets the label.
	 * 
	 * @return The label.
	 */
	public Label getLabel() {
		return this.label;
	}

	/**
	 * Gets the value for the presentationDescriptor field.
	 * 
	 * @return The value for the presentationDescriptor field.
	 */
	public PresentationOperationDescriptor<E> getPresentationDescriptor() {
		return this.presentationDescriptor;
	}

	/**
	 * Sets a new value for the builder field.
	 * 
	 * @param builder
	 *            The new value for the builder field.
	 */
	public void setBuilder(final OperationInvocationBuilder<E> builder) {
		this.builder = builder;
	}

	/**
	 * Sets a new value for the bus field.
	 * 
	 * @param bus
	 *            The new value for the bus field.
	 */
	public void setBus(final Bus bus) {
		this.bus = bus;
	}

	/**
	 * Sets a new value for the button field.
	 * 
	 * @param button
	 *            The new value for the button field.
	 */
	public void setButton(final Button button) {
		this.button = button;
	}

	/**
	 * Sets a new value for the descriptor field.
	 * 
	 * @param descriptor
	 *            The new value for the descriptor field.
	 */
	public void setDescriptor(final OperationDescriptor<E> descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Sets a new value for the label field.
	 * 
	 * @param label
	 *            The new value for the label field.
	 */
	public void setLabel(final Label label) {
		this.label = label;
	}

	/**
	 * Sets a new value for the presentationDescriptor field.
	 * 
	 * @param presentationDescriptor
	 *            The new value for the presentationDescriptor field.
	 */
	public void setPresentationDescriptor(final PresentationOperationDescriptor<E> presentationDescriptor) {
		this.presentationDescriptor = presentationDescriptor;
	}

	/**
	 * Execute the operation.
	 * 
	 * @param descriptor
	 *            The descriptor.
	 * @param <R>
	 *            The result type
	 */
	private <R> void execute(final ResultDescriptor<R> descriptor) {
		OperationOutputStrategy.LOGGER.debug("Invoking {}", descriptor);
		try {
			final R result = this.builder.execute(descriptor.getResultType());
			final OperationInvocationEvent<E, R> event = new OperationInvocationEvent<E, R>(this.descriptor, result);
			this.bus.handleEvent(event, this.builder);
		} catch (final ValueAccessException e) {
			OperationOutputStrategy.LOGGER.debug("Could not access value.", e);
			if (e.getCause() instanceof InvocationTargetException) {
				final InvocationTargetException ite = (InvocationTargetException) e.getCause();
				this.button.setComponentError(new UserError(ite.getCause().getMessage()));
			} else {
				this.button.setComponentError(new UserError(e.getMessage()));
			}
		} catch (final EventException e) {
			OperationOutputStrategy.LOGGER.warn("Could not process events.", e);
		}
	}
}
