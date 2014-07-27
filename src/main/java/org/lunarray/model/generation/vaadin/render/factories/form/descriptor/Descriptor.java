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
package org.lunarray.model.generation.vaadin.render.factories.form.descriptor;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.data.Validator;

/**
 * Describes a property.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The type.
 */
public interface Descriptor<P>
		extends Property, Validator, ValueChangeNotifier {

	/**
	 * Adds a listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	void addListener(final ValueChangedListener<P> listener);

	/**
	 * Gets the access buffer.
	 * 
	 * @return The access buffer.
	 */
	AccessBuffer<P> getBufferAccessor();

	/**
	 * Gets the mutate buffer.
	 * 
	 * @return The mutate buffer.
	 */
	MutateBuffer<P> getBufferMutator();

	/**
	 * Gets the label.
	 * 
	 * @return The label.
	 */
	String getLabel();

	/**
	 * Gets the name.
	 * 
	 * @return The name.
	 */
	String getName();

	/**
	 * Gets the relation name.
	 * 
	 * @return The relation name.
	 */
	String getRelationName();

	/**
	 * Tests if the descriptor is related.
	 * 
	 * @return True if related, otherwise false.
	 */
	boolean isRelated();

	/**
	 * Tests if the descriptor is required.
	 * 
	 * @return True if required, otherwise false.
	 */
	boolean isRequired();

	/**
	 * Remove the listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	void removeListener(final ValueChangedListener<P> listener);

	/**
	 * Sets the preferred type.
	 * 
	 * @param preferred
	 *            The preferred type.
	 */
	void setPreferredType(Class<?> preferred);
}
