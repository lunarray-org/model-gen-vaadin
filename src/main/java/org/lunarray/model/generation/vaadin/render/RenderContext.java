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
package org.lunarray.model.generation.vaadin.render;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.generation.util.Context;

/**
 * The render context.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity type.
 */
public final class RenderContext<E>
		implements Context {

	/** The model. */
	private final transient Model<? super E> model;
	/** The variable prefixes. */
	private final transient Deque<PropertyDescriptor<?, ?>> variablePrefixes;

	/**
	 * Constructs the context.
	 * 
	 * @param model
	 *            The model. May not be null.
	 */
	public RenderContext(final Model<? super E> model) {
		Validate.notNull(model, "The model may not be null.");
		this.variablePrefixes = new LinkedList<PropertyDescriptor<?, ?>>();
		this.model = model;
	}

	/**
	 * Gets the model.
	 * 
	 * @return The model.
	 */
	public Model<? super E> getModel() {
		return this.model;
	}

	/**
	 * Gets the property name.
	 * 
	 * @return The property name.
	 */
	public String getPropertyName() {
		final StringBuilder builder = new StringBuilder();
		final Iterator<PropertyDescriptor<?, ?>> iterator = this.variablePrefixes.iterator();
		if (iterator.hasNext()) {
			builder.append(iterator.next().getName());
			while (iterator.hasNext()) {
				builder.append(".").append(iterator.next().getName());
			}
		}
		return builder.toString();
	}

	/** {@inheritDoc} */
	@Override
	public PropertyDescriptor<?, ?> popPrefix() {
		return this.variablePrefixes.removeLast();
	}

	/** {@inheritDoc} */
	@Override
	public void pushPrefix(final PropertyDescriptor<?, ?> prefix) {
		this.variablePrefixes.add(prefix);
	}
}
