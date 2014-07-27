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
package org.lunarray.model.generation.vaadin.render.factories.table.vaadin;

import java.util.Collection;
import java.util.EnumMap;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.model.operation.OperationDescriptor;
import org.lunarray.model.descriptor.model.operation.parameters.CollectionParameterDescriptor;
import org.lunarray.model.descriptor.model.operation.parameters.ParameterDescriptor;
import org.lunarray.model.descriptor.model.operation.result.CollectionResultDescriptor;
import org.lunarray.model.descriptor.model.operation.result.ResultDescriptor;
import org.lunarray.model.descriptor.model.property.CollectionPropertyDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.RenderType;
import org.lunarray.model.generation.util.RenderFactory;
import org.lunarray.model.generation.vaadin.components.TableComponent;
import org.lunarray.model.generation.vaadin.render.RenderContext;
import org.lunarray.model.generation.vaadin.render.factories.table.TablePropertyRenderStrategy;
import org.lunarray.model.generation.vaadin.render.factories.table.TablePropertyRenderStrategy.StrategyFactory;
import org.lunarray.model.generation.vaadin.render.factories.table.vaadin.components.CheckboxOutputPropertyStrategy;
import org.lunarray.model.generation.vaadin.render.factories.table.vaadin.components.TextOutputPropertyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The form render strategy factory.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity type.
 */
public final class TablePropertyRenderStrategyFactoryImpl<E>
		implements RenderFactory<RenderContext<E>, E> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TablePropertyRenderStrategy.class);
	/** The default factory. */
	private StrategyFactory defaultFactory;
	/** The factories. */
	private EnumMap<RenderType, StrategyFactory> factories;
	/** The table. */
	private TableComponent tableComponent;

	/**
	 * The default constructor.
	 * 
	 * @param tableComponent
	 *            The table.
	 */
	public TablePropertyRenderStrategyFactoryImpl(final TableComponent tableComponent) {
		Validate.notNull(tableComponent, "Table component may not be null.");
		this.tableComponent = tableComponent;
		this.defaultFactory = new TextOutputPropertyStrategy.Factory();
		this.factories = new EnumMap<RenderType, StrategyFactory>(RenderType.class);
		this.factories.put(RenderType.CHECKBOX, new CheckboxOutputPropertyStrategy.Factory());
	}

	/** {@inheritDoc} */
	@Override
	public void beginOperation(final RenderContext<E> context, final OperationDescriptor<E> descriptor) {
		// Empty.
	}

	/** {@inheritDoc} */
	@Override
	public void endOperation(final RenderContext<E> context, final OperationDescriptor<E> operation) {
		// Empty.
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
	 * Gets the value for the factories field.
	 * 
	 * @return The value for the factories field.
	 */
	public EnumMap<RenderType, StrategyFactory> getFactories() {
		return this.factories;
	}

	/**
	 * Gets the value for the tableComponent field.
	 * 
	 * @return The value for the tableComponent field.
	 */
	public TableComponent getTableComponent() {
		return this.tableComponent;
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionParameter(final RenderContext<E> context,
			final OperationDescriptor<E> operation, final CollectionParameterDescriptor<D, P> descriptor, final RenderType renderType) {
		// Empty.
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionProperty(final RenderContext<E> context,
			final CollectionPropertyDescriptor<D, P, E> descriptor, final RenderType renderType) {
		this.renderProperty(context, descriptor, renderType);
	}

	/** {@inheritDoc} */
	@Override
	public <D, R extends Collection<D>> void renderCollectionResultType(final RenderContext<E> context,
			final OperationDescriptor<E> operation, final CollectionResultDescriptor<D, R> resultDescriptor, final RenderType renderType) {
		// Empty.
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderParameter(final RenderContext<E> context, final ParameterDescriptor<P> descriptor,
			final OperationDescriptor<E> operation, final RenderType renderType) {
		// Empty.
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderProperty(final RenderContext<E> context, final PropertyDescriptor<P, E> descriptor, final RenderType renderType) {
		Validate.notNull(descriptor, "Descriptor may not be null.");
		Validate.notNull(context, "Render context may not be null.");
		TablePropertyRenderStrategy<P, E> strategy;
		if (this.factories.containsKey(renderType)) {
			strategy = this.factories.get(renderType).createStrategy(descriptor, context);
		} else {
			strategy = this.defaultFactory.createStrategy(descriptor, context);
		}
		TablePropertyRenderStrategyFactoryImpl.LOGGER.debug("Resolved startegy {} for render type {} of property: {}", strategy,
				renderType, descriptor);
		this.tableComponent.processStrategy(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <R> void renderResultType(final RenderContext<E> context, final OperationDescriptor<E> operation,
			final ResultDescriptor<R> resultDescriptor, final RenderType renderType) {
		// Empty.
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
	 * Sets a new value for the factories field.
	 * 
	 * @param factories
	 *            The new value for the factories field.
	 */
	public void setFactories(final EnumMap<RenderType, StrategyFactory> factories) {
		this.factories = factories;
	}

	/**
	 * Sets a new value for the tableComponent field.
	 * 
	 * @param tableComponent
	 *            The new value for the tableComponent field.
	 */
	public void setTableComponent(final TableComponent tableComponent) {
		this.tableComponent = tableComponent;
	}
}
