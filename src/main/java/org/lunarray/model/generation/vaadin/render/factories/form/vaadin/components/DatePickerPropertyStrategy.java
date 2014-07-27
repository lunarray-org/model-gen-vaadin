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

import java.util.Date;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.PopupDateField;

import org.lunarray.model.generation.vaadin.render.RenderContext;
import org.lunarray.model.generation.vaadin.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.Descriptor;

/**
 * Constructs the date picker.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public final class DatePickerPropertyStrategy<P>
		extends AbstractFormPropertyRenderStrategy<P> {

	/** Serial id. */
	private static final long serialVersionUID = -3287303265200736200L;
	/** Indicates the time is visible. */
	private boolean time;

	/**
	 * Constructs the strategy.
	 * 
	 * @param descriptor
	 *            The property descriptor. May not be null.
	 * @param context
	 *            The render context. May not be null.
	 * @param time
	 *            Whether or not to include the time.
	 */
	public DatePickerPropertyStrategy(final Descriptor<P> descriptor, final RenderContext<?> context, final boolean time) {
		super(descriptor, context, Date.class);
		this.time = time;
	}

	/**
	 * Gets the value for the time field.
	 * 
	 * @return The value for the time field.
	 */
	public boolean isTime() {
		return this.time;
	}

	/**
	 * Sets a new value for the time field.
	 * 
	 * @param time
	 *            The new value for the time field.
	 */
	public void setTime(final boolean time) {
		this.time = time;
	}

	/** {@inheritDoc} */
	@Override
	protected Component createComponent() {
		final PopupDateField dateField = new PopupDateField();
		int resolution;
		if (this.time) {
			resolution = DateField.RESOLUTION_SEC;
		} else {
			resolution = DateField.RESOLUTION_DAY;
		}
		dateField.setResolution(resolution);
		return dateField;
	}

	/**
	 * The date factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class DateFactory
			implements StrategyFactory {

		/**
		 * Default constructor.
		 */
		public DateFactory() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public <P> FormPropertyRenderStrategy<P> createStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
			return new DatePickerPropertyStrategy<P>(descriptor, context, false);
		}
	}

	/**
	 * The date time factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class DateTimeFactory
			implements StrategyFactory {

		/**
		 * Default constructor.
		 */
		public DateTimeFactory() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public <P> FormPropertyRenderStrategy<P> createStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
			return new DatePickerPropertyStrategy<P>(descriptor, context, true);
		}
	}

}
