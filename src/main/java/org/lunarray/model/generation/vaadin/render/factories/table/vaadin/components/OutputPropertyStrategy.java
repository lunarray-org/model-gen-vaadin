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
package org.lunarray.model.generation.vaadin.render.factories.table.vaadin.components;

import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.extension.ExtensionRef;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.lunarray.model.generation.vaadin.render.factories.table.TablePropertyRenderStrategy;

/**
 * The output property strategy.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public interface OutputPropertyStrategy<P, E>
		extends TablePropertyRenderStrategy<P, E> {

	/**
	 * Gets the value for the converterTool field.
	 * 
	 * @return The value for the converterTool field.
	 */
	ExtensionRef<ConverterTool> getConverterTool();

	/**
	 * Gets the model.
	 * 
	 * @return The model.
	 */
	Model<?> getModel();

	/**
	 * Gets the presentation property descriptor.
	 * 
	 * @return The property descriptor.
	 */
	PresentationPropertyDescriptor<P, E> getPresentationProperty();

	/**
	 * Gets the property descriptor.
	 * 
	 * @return The property descriptor.
	 */
	PropertyDescriptor<P, E> getProperty();

	/**
	 * Tests if the property is a presentation property.
	 * 
	 * @return The property.
	 */
	boolean hasPresentationProperty();
}
