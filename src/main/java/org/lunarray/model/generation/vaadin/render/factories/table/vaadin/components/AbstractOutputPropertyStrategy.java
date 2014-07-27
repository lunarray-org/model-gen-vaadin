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

import java.io.Serializable;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.extension.ExtensionRef;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.lunarray.model.generation.vaadin.render.RenderContext;

/**
 * Constructs the text output.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public abstract class AbstractOutputPropertyStrategy<P, E>
		implements Serializable, OutputPropertyStrategy<P, E> {

	/** Serial id. */
	private static final long serialVersionUID = -5347387297814017289L;
	/** The converter tool. */
	private ExtensionRef<ConverterTool> converterTool;
	/** The model. */
	private Model<?> model;
	/** The property. */
	private PropertyDescriptor<P, E> property;

	/**
	 * Constructs the strategy.
	 * 
	 * @param propertyDescriptor
	 *            The property descriptor. May not be null.
	 * @param context
	 *            The render context. May not be null.
	 */
	protected AbstractOutputPropertyStrategy(final PropertyDescriptor<P, E> propertyDescriptor, final RenderContext<E> context) {
		Validate.notNull(propertyDescriptor, "Descriptor may not be null.");
		Validate.notNull(context, "Context may not be null.");
		this.property = propertyDescriptor;
		this.model = context.getModel();
		Validate.notNull(this.model, "Model may not be null.");
		this.converterTool = this.model.getExtensionRef(ConverterTool.class);
		Validate.notNull(this.converterTool, "Model must have converter tool extension");
	}

	/** {@inheritDoc} */
	@Override
	public final ExtensionRef<ConverterTool> getConverterTool() {
		return this.converterTool;
	}

	/** {@inheritDoc} */
	@Override
	public final Model<?> getModel() {
		return this.model;
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public final PresentationPropertyDescriptor<P, E> getPresentationProperty() {
		return this.property.adapt(PresentationPropertyDescriptor.class);
	}

	/** {@inheritDoc} */
	@Override
	public final PropertyDescriptor<P, E> getProperty() {
		return this.property;
	}

	/** {@inheritDoc} */
	@Override
	public final PropertyDescriptor<P, E> getPropertyDescriptor() {
		return this.getProperty();
	}

	/** {@inheritDoc} */
	@Override
	public final String getPropertyLabel() {
		String result;
		if (this.hasPresentationProperty()) {
			result = this.getPresentationProperty().getDescription();
		} else {
			result = this.getProperty().getName();
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final String getPropertyName() {
		return this.getProperty().getName();
	}

	/** {@inheritDoc} */
	@Override
	public final Class<P> getPropertyType() {
		return this.getProperty().getPropertyType();
	}

	/** {@inheritDoc} */
	@Override
	public final boolean hasPresentationProperty() {
		return this.property.adapt(PresentationPropertyDescriptor.class) != null;
	}

	/**
	 * Sets a new value for the converterTool field.
	 * 
	 * @param converterTool
	 *            The new value for the converterTool field.
	 */
	public final void setConverterTool(final ExtensionRef<ConverterTool> converterTool) {
		this.converterTool = converterTool;
	}

	/**
	 * Sets a new value for the model field.
	 * 
	 * @param model
	 *            The new value for the model field.
	 */
	public final void setModel(final Model<?> model) {
		this.model = model;
	}

	/**
	 * Sets a new value for the property field.
	 * 
	 * @param property
	 *            The new value for the property field.
	 */
	public final void setProperty(final PropertyDescriptor<P, E> property) {
		this.property = property;
	}
}
