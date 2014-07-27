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
package org.lunarray.model.generation.vaadin.render.factories.form.vaadin;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.lunarray.common.event.Bus;
import org.lunarray.model.descriptor.model.operation.OperationDescriptor;
import org.lunarray.model.descriptor.model.operation.parameters.CollectionParameterDescriptor;
import org.lunarray.model.descriptor.model.operation.parameters.ParameterDescriptor;
import org.lunarray.model.descriptor.model.operation.result.CollectionResultDescriptor;
import org.lunarray.model.descriptor.model.operation.result.ResultDescriptor;
import org.lunarray.model.descriptor.model.property.CollectionPropertyDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.RenderType;
import org.lunarray.model.descriptor.util.OperationInvocationBuilder;
import org.lunarray.model.generation.util.RenderFactory;
import org.lunarray.model.generation.vaadin.components.FormComponent;
import org.lunarray.model.generation.vaadin.render.RenderContext;
import org.lunarray.model.generation.vaadin.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.FormPropertyRenderStrategy.StrategyFactory;
import org.lunarray.model.generation.vaadin.render.factories.form.impl.parameter.CollectionParameterDescriptorImpl;
import org.lunarray.model.generation.vaadin.render.factories.form.impl.parameter.ParameterDescriptorImpl;
import org.lunarray.model.generation.vaadin.render.factories.form.impl.property.CollectionPropertyDescriptorImpl;
import org.lunarray.model.generation.vaadin.render.factories.form.impl.property.PropertyDescriptorImpl;
import org.lunarray.model.generation.vaadin.render.factories.form.impl.result.ResultValueDescriptorImpl;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.CheckboxPropertyStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.DatePickerPropertyStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.ListSelectPropertyStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.MenuSelectPropertyStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.OperationOutputStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.RadioSelectPropertyStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.RichTextAreaPropertyStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.ShuttleSelectPropertyStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.TextAreaPropertyStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.TextFieldPropertyStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.TextOutputPropertyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The form render strategy factory.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity type.
 */
public final class FormPropertyRenderStrategyFactoryImpl<E>
		implements RenderFactory<RenderContext<E>, E> {

	/** Validation message. */
	private static final String CONTEXT_NULL = "Context may not be null.";
	/** Validation message. */
	private static final String DESCRIPTOR_NULL = "Descriptor may not be null.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FormPropertyRenderStrategyFactoryImpl.class);
	/** The builders. */
	private Map<OperationDescriptor<E>, OperationInvocationBuilder<E>> builders;
	/** The default factory. */
	private StrategyFactory defaultFactory;
	/** The event bus. */
	private Bus eventBus;
	/** The factories. */
	private EnumMap<RenderType, StrategyFactory> factories;
	/** The form. */
	private FormComponent<? super E, E> form;

	/**
	 * The default constructor.
	 * 
	 * @param form
	 *            The form. May not be null.
	 */
	public FormPropertyRenderStrategyFactoryImpl(final FormComponent<? super E, E> form) {
		Validate.notNull(form, "Form may not be null.");
		this.form = form;
		this.eventBus = new Bus();
		this.builders = new HashMap<OperationDescriptor<E>, OperationInvocationBuilder<E>>();
		this.defaultFactory = new TextOutputPropertyStrategy.Factory();
		this.factories = new EnumMap<RenderType, StrategyFactory>(RenderType.class);
		this.factories.put(RenderType.CHECKBOX, new CheckboxPropertyStrategy.Factory());
		this.factories.put(RenderType.DATE_PICKER, new DatePickerPropertyStrategy.DateFactory());
		this.factories.put(RenderType.DATE_TIME_PICKER, new DatePickerPropertyStrategy.DateFactory());
		this.factories.put(RenderType.TIME_PICKER, new DatePickerPropertyStrategy.DateTimeFactory());
		this.factories.put(RenderType.PICKLIST, new ListSelectPropertyStrategy.Factory());
		this.factories.put(RenderType.DROPDOWN, new MenuSelectPropertyStrategy.Factory());
		this.factories.put(RenderType.RADIO, new RadioSelectPropertyStrategy.Factory());
		this.factories.put(RenderType.SHUTTLE, new ShuttleSelectPropertyStrategy.Factory());
		this.factories.put(RenderType.TEXT, new TextFieldPropertyStrategy.Factory());
		this.factories.put(RenderType.TEXT_AREA, new TextAreaPropertyStrategy.Factory());
		this.factories.put(RenderType.RICH_TEXT, new RichTextAreaPropertyStrategy.Factory());
	}

	/** {@inheritDoc} */
	@Override
	public void beginOperation(final RenderContext<E> context, final OperationDescriptor<E> descriptor) {
		Validate.notNull(descriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		final OperationInvocationBuilder<E> builder = new OperationInvocationBuilder<E>(descriptor);
		builder.target(this.form.getEntity());
		this.builders.put(descriptor, builder);
		this.form.processBeginStrategy(new OperationOutputStrategy<E>(descriptor, this.builders.get(descriptor), this.eventBus));
	}

	/** {@inheritDoc} */
	@Override
	public void endOperation(final RenderContext<E> context, final OperationDescriptor<E> descriptor) {
		Validate.notNull(descriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		this.form.processEndStrategy(new OperationOutputStrategy<E>(descriptor, this.builders.get(descriptor), this.eventBus));
	}

	/**
	 * Gets the value for the builders field.
	 * 
	 * @return The value for the builders field.
	 */
	public Map<OperationDescriptor<E>, OperationInvocationBuilder<E>> getBuilders() {
		return this.builders;
	}

	/**
	 * Gets the value for the defaultFactory field.
	 * 
	 * @return The value for the defaultFactory field.
	 */
	public StrategyFactory getDefaultFactory() {
		return this.defaultFactory;
	}

	/**
	 * Gets the value for the eventBus field.
	 * 
	 * @return The value for the eventBus field.
	 */
	public Bus getEventBus() {
		return this.eventBus;
	}

	/**
	 * Gets the value for the factories field.
	 * 
	 * @return The value for the factories field.
	 */
	public EnumMap<RenderType, StrategyFactory> getFactories() {
		return this.factories;
	}

	/**
	 * Gets the value for the form field.
	 * 
	 * @return The value for the form field.
	 */
	public FormComponent<? super E, E> getForm() {
		return this.form;
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionParameter(final RenderContext<E> context,
			final OperationDescriptor<E> operation, final CollectionParameterDescriptor<D, P> descriptor, final RenderType renderType) {
		Validate.notNull(descriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(operation, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		final CollectionParameterDescriptorImpl<D, P, E> collDescriptor = new CollectionParameterDescriptorImpl<D, P, E>(descriptor,
				this.builders.get(operation), context.getModel());
		FormPropertyRenderStrategy<P> strategy;
		if (this.factories.containsKey(renderType)) {
			strategy = this.factories.get(renderType).createStrategy(collDescriptor, context);
		} else {
			strategy = this.defaultFactory.createStrategy(collDescriptor, context);
		}
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Resolved startegy {} for render type {} of collection parameter: {}", strategy,
				renderType, descriptor);
		this.form.processStrategy(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionProperty(final RenderContext<E> context,
			final CollectionPropertyDescriptor<D, P, E> descriptor, final RenderType renderType) {
		Validate.notNull(descriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		final CollectionPropertyDescriptorImpl<D, P, E> collDescriptor = new CollectionPropertyDescriptorImpl<D, P, E>(descriptor,
				context.getModel());
		collDescriptor.setEntity(this.form.getEntity());
		FormPropertyRenderStrategy<P> strategy;
		if (this.factories.containsKey(renderType)) {
			strategy = this.factories.get(renderType).createStrategy(collDescriptor, context);
		} else {
			strategy = this.defaultFactory.createStrategy(collDescriptor, context);
		}
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Resolved startegy {} for render type {} of collection property: {}", strategy,
				renderType, descriptor);
		this.form.processStrategy(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <D, R extends Collection<D>> void renderCollectionResultType(final RenderContext<E> context,
			final OperationDescriptor<E> operation, final CollectionResultDescriptor<D, R> resultDescriptor, final RenderType renderType) {
		Validate.notNull(resultDescriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(operation, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		final ResultValueDescriptorImpl<R> descriptor = new ResultValueDescriptorImpl<R>(resultDescriptor, operation, context.getModel());
		this.eventBus.addListener(descriptor, this.builders.get(descriptor));
		FormPropertyRenderStrategy<R> strategy;
		if (this.factories.containsKey(renderType)) {
			strategy = this.factories.get(renderType).createStrategy(descriptor, context);
		} else {
			strategy = this.defaultFactory.createStrategy(descriptor, context);
		}
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Resolved startegy {} for render type {} of collection result type: {}",
				strategy, renderType, descriptor);
		this.form.processStrategy(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderParameter(final RenderContext<E> context, final ParameterDescriptor<P> descriptor,
			final OperationDescriptor<E> operation, final RenderType renderType) {
		Validate.notNull(descriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(operation, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		final ParameterDescriptorImpl<P, E> collDescriptor = new ParameterDescriptorImpl<P, E>(descriptor, this.builders.get(operation),
				context.getModel());
		FormPropertyRenderStrategy<P> strategy;
		if (this.factories.containsKey(renderType)) {
			strategy = this.factories.get(renderType).createStrategy(collDescriptor, context);
		} else {
			strategy = this.defaultFactory.createStrategy(collDescriptor, context);
		}
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Resolved startegy {} for render type {} of parameter: {}", strategy,
				renderType, descriptor);
		this.form.processStrategy(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderProperty(final RenderContext<E> context, final PropertyDescriptor<P, E> propertyDescriptor,
			final RenderType renderType) {
		Validate.notNull(propertyDescriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		final PropertyDescriptorImpl<P, E> descriptor = new PropertyDescriptorImpl<P, E>(propertyDescriptor, context.getModel());
		descriptor.setEntity(this.form.getEntity());
		FormPropertyRenderStrategy<P> strategy;
		if (this.factories.containsKey(renderType)) {
			strategy = this.factories.get(renderType).createStrategy(descriptor, context);
		} else {
			strategy = this.defaultFactory.createStrategy(descriptor, context);
		}
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Resolved startegy {} for render type {} of property: {}", strategy, renderType,
				descriptor);
		this.form.processStrategy(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <R> void renderResultType(final RenderContext<E> context, final OperationDescriptor<E> operation,
			final ResultDescriptor<R> resultDescriptor, final RenderType renderType) {
		Validate.notNull(resultDescriptor, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(operation, FormPropertyRenderStrategyFactoryImpl.DESCRIPTOR_NULL);
		Validate.notNull(context, FormPropertyRenderStrategyFactoryImpl.CONTEXT_NULL);
		final ResultValueDescriptorImpl<R> descriptor = new ResultValueDescriptorImpl<R>(resultDescriptor, operation, context.getModel());
		this.eventBus.addListener(descriptor, this.builders.get(operation));
		FormPropertyRenderStrategy<R> strategy;
		if (this.factories.containsKey(renderType)) {
			strategy = this.factories.get(renderType).createStrategy(descriptor, context);
		} else {
			strategy = this.defaultFactory.createStrategy(descriptor, context);
		}
		FormPropertyRenderStrategyFactoryImpl.LOGGER.debug("Resolved startegy {} for render type {} of result type: {}", strategy,
				renderType, descriptor);
		this.form.processStrategy(strategy);
	}

	/**
	 * Sets a new value for the builders field.
	 * 
	 * @param builders
	 *            The new value for the builders field.
	 */
	public void setBuilders(final Map<OperationDescriptor<E>, OperationInvocationBuilder<E>> builders) {
		this.builders = builders;
	}

	/**
	 * Sets a new value for the defaultFactory field.
	 * 
	 * @param defaultFactory
	 *            The new value for the defaultFactory field.
	 */
	public void setDefaultFactory(final StrategyFactory defaultFactory) {
		this.defaultFactory = defaultFactory;
	}

	/**
	 * Sets a new value for the eventBus field.
	 * 
	 * @param eventBus
	 *            The new value for the eventBus field.
	 */
	public void setEventBus(final Bus eventBus) {
		this.eventBus = eventBus;
	}

	/**
	 * Sets a new value for the factories field.
	 * 
	 * @param factories
	 *            The new value for the factories field.
	 */
	public void setFactories(final EnumMap<RenderType, StrategyFactory> factories) {
		this.factories = factories;
	}

	/**
	 * Sets a new value for the form field.
	 * 
	 * @param form
	 *            The new value for the form field.
	 */
	public void setForm(final FormComponent<? super E, E> form) {
		this.form = form;
	}

}
