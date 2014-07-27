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
package org.lunarray.model.generation.vaadin.render.factories.table;

import com.vaadin.ui.Table.ColumnGenerator;

import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.generation.vaadin.render.RenderContext;

/**
 * The table render strategy.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public interface TablePropertyRenderStrategy<P, E> {

	/**
	 * Gets a column generator.
	 * 
	 * @return The column generator.
	 */
	ColumnGenerator getGenerator();

	/**
	 * Gets the property descriptor.
	 * 
	 * @return The property descriptor.
	 */
	PropertyDescriptor<P, E> getPropertyDescriptor();

	/**
	 * Gets the property label.
	 * 
	 * @return The property label.
	 */
	String getPropertyLabel();

	/**
	 * Gets the property name.
	 * 
	 * @return The property name.
	 */
	String getPropertyName();

	/**
	 * Gets the property type.
	 * 
	 * @return The property type.
	 */
	Class<P> getPropertyType();

	/**
	 * The factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	interface StrategyFactory {
		/**
		 * Create a table strategy.
		 * 
		 * @param propertyDescriptor
		 *            The descriptor.
		 * @param context
		 *            The context.
		 * @return The strategy.
		 * @param <P>
		 *            The property type.
		 * @param <E>
		 *            The entity type.
		 */
		<P, E> TablePropertyRenderStrategy<P, E> createStrategy(PropertyDescriptor<P, E> propertyDescriptor, RenderContext<E> context);
	}
}
