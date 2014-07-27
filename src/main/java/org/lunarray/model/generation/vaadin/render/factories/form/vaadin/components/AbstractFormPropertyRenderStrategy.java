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

import java.io.Serializable;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.extension.ExtensionRef;
import org.lunarray.model.generation.vaadin.render.RenderContext;
import org.lunarray.model.generation.vaadin.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.Descriptor;

/**
 * The abstract form strategy.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public abstract class AbstractFormPropertyRenderStrategy<P>
		implements FormPropertyRenderStrategy<P>, Serializable {

	/** Serial id. */
	private static final long serialVersionUID = -2260500796489959071L;
	/** The component. */
	private Component component;
	/** The converter tool. */
	private ExtensionRef<ConverterTool> converterTool;
	/** The property. */
	private Descriptor<P> descriptor;
	/** The model. */
	private Model<?> model;
	/** The property name. */
	private String propertyName;

	/**
	 * Constructs the strategy.
	 * 
	 * @param descriptor
	 *            The property descriptor. May not b e null.
	 * @param context
	 *            The render context. May not be null.
	 * @param preferredType
	 *            The preferred type. May not be null.
	 */
	public AbstractFormPropertyRenderStrategy(final Descriptor<P> descriptor, final RenderContext<?> context, final Class<?> preferredType) {
		Validate.notNull(descriptor, "Descriptor may not be null.");
		Validate.notNull(context, "Context may not be null.");
		Validate.notNull(preferredType, "Preferred type may not be null.");
		this.descriptor = descriptor;
		descriptor.setPreferredType(preferredType);
		this.model = context.getModel();
		this.propertyName = context.getPropertyName();
	}

	/** {@inheritDoc} */
	@Override
	public final Component getComponent() {
		if (CheckUtil.isNull(this.component)) {
			this.initiateComponent();
		}
		return this.component;
	}

	/**
	 * Gets the value for the converterTool field.
	 * 
	 * @return The value for the converterTool field.
	 */
	public final ExtensionRef<ConverterTool> getConverterTool() {
		return this.converterTool;
	}

	/** {@inheritDoc} */
	@Override
	public final Descriptor<P> getDescriptor() {
		return this.descriptor;
	}

	/**
	 * Gets the model.
	 * 
	 * @return The model.
	 */
	public final Model<?> getModel() {
		return this.model;
	}

	/** {@inheritDoc} */
	@Override
	public final String getPropertyName() {
		return this.propertyName;
	}

	/**
	 * Sets a new value for the component field.
	 * 
	 * @param component
	 *            The new value for the component field.
	 */
	public final void setComponent(final Component component) {
		this.component = component;
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
	 * Sets a new value for the descriptor field.
	 * 
	 * @param descriptor
	 *            The new value for the descriptor field.
	 */
	public final void setDescriptor(final Descriptor<P> descriptor) {
		this.descriptor = descriptor;
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
	 * Sets a new value for the propertyName field.
	 * 
	 * @param propertyName
	 *            The new value for the propertyName field.
	 */
	public final void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Initializes the component.
	 */
	private void initiateComponent() {
		this.component = this.createComponent();
		final String caption = this.descriptor.getLabel();
		if (this.component instanceof Field) {
			final Field field = (Field) this.component;
			if (this.descriptor.isRequired()) {
				field.setRequired(true);
			}
			field.addValidator(this.descriptor);
		}
		this.component.setCaption(caption);
		if (this.component instanceof Property.Viewer) {
			final Property.Viewer viewer = (Property.Viewer) this.component;
			viewer.setPropertyDataSource(this.descriptor);
		}
	}

	/**
	 * Creates a component.
	 * 
	 * @return The component.
	 */
	protected abstract Component createComponent();
}
