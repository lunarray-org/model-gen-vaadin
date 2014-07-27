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
package org.lunarray.model.generation.vaadin.components.impl;

import java.util.Collection;

import com.vaadin.ui.Table;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.generation.util.Composer;
import org.lunarray.model.generation.vaadin.components.TableComponent;
import org.lunarray.model.generation.vaadin.render.RenderContext;
import org.lunarray.model.generation.vaadin.render.factories.table.TablePropertyRenderStrategy;
import org.lunarray.model.generation.vaadin.render.factories.table.vaadin.TablePropertyRenderStrategyFactoryImpl;

/**
 * Implements the table.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The super type.
 * @param <E>
 *            The entity type.
 */
public final class TableComponentImpl<S, E extends S>
		extends AbstractComponent<S, E>
		implements TableComponent {

	/** Serial id. */
	private static final long serialVersionUID = -2445831011878758711L;
	/** The entities. */
	private Collection<E> entities;
	/** The table. */
	private Table table;

	/**
	 * Constructs the form component.
	 * 
	 * @param model
	 *            The model.
	 * @param entityKey
	 *            The entity key.
	 * @param entities
	 *            The entities.
	 */
	public TableComponentImpl(final Model<S> model, final String entityKey, final Collection<E> entities) {
		super(model, entityKey);
		this.entities = entities;
		this.init();
	}

	/**
	 * Constructs the form component.
	 * 
	 * @param model
	 *            The model.
	 * @param entityKey
	 *            The entity key.
	 */
	protected TableComponentImpl(final Model<S> model, final String entityKey) {
		this(model, entityKey, null);
	}

	/**
	 * Gets the value for the entities field.
	 * 
	 * @return The value for the entities field.
	 */
	public Collection<E> getEntities() {
		return this.entities;
	}

	/**
	 * Gets the value for the table field.
	 * 
	 * @return The value for the table field.
	 */
	public Table getTable() {
		return this.table;
	}

	/** {@inheritDoc} */
	@Override
	public void processStrategy(final TablePropertyRenderStrategy<?, ?> strategy) {
		Validate.notNull(strategy, "Strategy may not be null.");
		this.table.addContainerProperty(strategy.getPropertyName(), strategy.getPropertyType(), null);
		this.table.setColumnHeader(strategy.getPropertyName(), strategy.getPropertyLabel());
		this.table.addGeneratedColumn(strategy.getPropertyName(), strategy.getGenerator());
	}

	/**
	 * Sets a new value for the entities field.
	 * 
	 * @param entities
	 *            The new value for the entities field.
	 */
	public void setEntities(final Collection<E> entities) {
		this.entities = entities;
		this.table.removeAllItems();
		for (final E entity : this.entities) {
			this.table.addItem(entity);
		}
	}

	/**
	 * Sets a new value for the table field.
	 * 
	 * @param table
	 *            The new value for the table field.
	 */
	public void setTable(final Table table) {
		this.table = table;
	}

	/** Initializes the table. */
	private void init() {
		final Composer<RenderContext<E>, S, E> composer = new Composer<RenderContext<E>, S, E>();
		composer.setContext(new RenderContext<E>(this.getModel()));
		composer.setPropertyRenderStrategyFactory(new TablePropertyRenderStrategyFactoryImpl<E>(this));
		composer.setVariableResolver(new ComponentVariableResolver());

		this.table = new Table(composer.getLabel());
		if (!CheckUtil.isNull(this.entities)) {
			for (final E entity : this.entities) {
				this.table.addItem(entity);
			}
			composer.compose(false);
		}
		this.setCompositionRoot(this.table);
	}
}
