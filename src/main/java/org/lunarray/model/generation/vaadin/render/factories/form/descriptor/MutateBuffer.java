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

/**
 * A mutate buffer. Allows mutation of described values.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The described type.
 */
public interface MutateBuffer<P> {
	/**
	 * Coerce to this type.
	 * 
	 * @param value
	 *            The type.
	 * @param <T>
	 *            The type.
	 */
	<T> void setCoerceValue(T value);

	/**
	 * Sets value.
	 * 
	 * @param value
	 *            The new value.
	 */
	void setDirectValue(P value);

	/**
	 * Sets the string.
	 * 
	 * @param value
	 *            The new value.
	 */
	void setStringValue(String value);
}
