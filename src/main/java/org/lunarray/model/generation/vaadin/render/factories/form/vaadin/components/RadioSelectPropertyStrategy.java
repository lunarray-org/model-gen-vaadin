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

import com.vaadin.ui.Component;
import com.vaadin.ui.OptionGroup;

import org.apache.commons.lang.Validate;
import org.lunarray.model.generation.vaadin.render.RenderContext;
import org.lunarray.model.generation.vaadin.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.Descriptor;

/**
 * Constructs the radio selector.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public final class RadioSelectPropertyStrategy<P>
		extends AbstractSelectFieldPropertyStrategy<P> {

	/** Serial id. */
	private static final long serialVersionUID = -1784718295075052167L;

	/**
	 * Constructs the strategy.
	 * 
	 * @param descriptor
	 *            The property descriptor. May not be null.
	 * @param context
	 *            The render context. May not be null.
	 */
	protected RadioSelectPropertyStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
		super(descriptor, context, String.class);
	}

	/** {@inheritDoc} */
	@Override
	protected Component createSelectComponent() {
		final OptionGroup component = new OptionGroup();
		component.setMultiSelect(false);
		return component;
	}

	/**
	 * The factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class Factory
			implements StrategyFactory {

		/**
		 * Default constructor.
		 */
		public Factory() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public <P> FormPropertyRenderStrategy<P> createStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
			Validate.notNull(descriptor, "Descriptor may not be null.");
			Validate.notNull(context, "Context may not be null.");
			return new RadioSelectPropertyStrategy<P>(descriptor, context);
		}
	}
}
