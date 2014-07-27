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
package org.lunarray.model.generation.vaadin.render.factories.form.events;

import com.vaadin.data.Property;

/**
 * An entity change event.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class EntityChangeEvent
		implements Property.ValueChangeEvent {

	/** Serial id. */
	private static final long serialVersionUID = 8575448308490517852L;
	/** The property. */
	private Property property;

	/**
	 * Default constructor.
	 * 
	 * @param property
	 *            The property.
	 */
	public EntityChangeEvent(final Property property) {
		this.property = property;
	}

	/** {@inheritDoc} */
	@Override
	public Property getProperty() {
		return this.property;
	}

	/**
	 * Sets a new value for the property field.
	 * 
	 * @param property
	 *            The new value for the property field.
	 */
	public void setProperty(final Property property) {
		this.property = property;
	}
}
