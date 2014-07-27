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
package org.lunarray.model.generation.vaadin.render.factories.form;

import com.vaadin.ui.Component;

import org.lunarray.model.generation.vaadin.render.RenderContext;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.Descriptor;

/**
 * A render strategy for form components.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public interface FormPropertyRenderStrategy<P> {

	/**
	 * Gets the component.
	 * 
	 * @return The component.
	 */
	Component getComponent();

	/**
	 * Gets the property descriptor that this strategy describes.
	 * 
	 * @return The property descriptor.
	 */
	Descriptor<P> getDescriptor();

	/**
	 * Gets the property name.
	 * 
	 * @return The property name.
	 */
	String getPropertyName();

	/**
	 * A strategy factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	interface StrategyFactory {
		/**
		 * Creates a strategy.
		 * 
		 * @param descriptor
		 *            The descriptor.
		 * @param context
		 *            The context.
		 * @return The strategy.
		 * @param <P>
		 *            The parameter type.
		 */
		<P> FormPropertyRenderStrategy<P> createStrategy(final Descriptor<P> descriptor, final RenderContext<?> context);
	}
}
