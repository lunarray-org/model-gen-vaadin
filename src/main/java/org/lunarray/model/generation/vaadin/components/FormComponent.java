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
package org.lunarray.model.generation.vaadin.components;

import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

import org.lunarray.model.generation.vaadin.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.OperationOutputStrategy;

/**
 * The form component.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The super type.
 * @param <E>
 *            The entity type.
 */
public interface FormComponent<S, E extends S>
		extends Component {

	/**
	 * Adds a cancel listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	void addCancelListener(ClickListener listener);

	/**
	 * Adds a submit listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	void addSubmitListener(ClickListener listener);

	/**
	 * Get the entity.
	 * 
	 * @return The entity.
	 */
	E getEntity();

	/**
	 * Process begin.
	 * 
	 * @param strategy
	 *            The strategy.
	 */
	void processBeginStrategy(OperationOutputStrategy<E> strategy);

	/**
	 * Process end.
	 * 
	 * @param strategy
	 *            The strategy.
	 */
	void processEndStrategy(OperationOutputStrategy<E> strategy);

	/**
	 * Process strategy.
	 * 
	 * @param strategy
	 *            The strategy.
	 */
	void processStrategy(final FormPropertyRenderStrategy<?> strategy);

	/**
	 * Removes a cancel listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	void removeCancelListener(ClickListener listener);

	/**
	 * Removes a submit listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	void removeSubmitListener(ClickListener listener);
}
