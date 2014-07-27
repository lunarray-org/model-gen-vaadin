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
package org.lunarray.model.generation.vaadin.components.impl;

import java.util.Collection;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.validator.EntityValidator;
import org.lunarray.model.descriptor.validator.PropertyViolation;
import org.lunarray.model.generation.vaadin.components.FormComponent;

/**
 * Enables validation and displays messages in case of a violation.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity type.
 * @param <S>
 *            The entity super type.
 */
public final class EntityValidationListener<S, E extends S>
		implements ClickListener {
	/** Serial id. */
	private static final long serialVersionUID = -4647657614250422554L;
	/** The form component. */
	private FormComponentImpl<S, E> formComponent;

	/**
	 * Default constructor.
	 * 
	 * @param formComponent
	 *            The form component.
	 */
	protected EntityValidationListener(final FormComponentImpl<S, E> formComponent) {
		Validate.notNull(formComponent, "Form may not be null.");
		this.formComponent = formComponent;
	}

	/** {@inheritDoc} */
	@Override
	public void buttonClick(final ClickEvent event) {
		for (final String property : this.formComponent.getProperties()) {
			final Field field = this.formComponent.getForm().getField(property);
			if (field instanceof AbstractComponent) {
				final AbstractComponent component = (AbstractComponent) field;
				component.setComponentError(null);
			}
		}
		final EntityValidator validator = this.formComponent.getModel().getExtension(EntityValidator.class);
		if (!CheckUtil.isNull(validator)) {
			final Collection<PropertyViolation<E, ?>> violations = validator.validate(this.formComponent.getEntityDescriptor(),
					this.formComponent.getEntity());
			for (final PropertyViolation<E, ?> violation : violations) {
				this.processViolation(violation);
			}
		}
	}

	/**
	 * Gets the value for the formComponent field.
	 * 
	 * @return The value for the formComponent field.
	 */
	public FormComponent<S, E> getFormComponent() {
		return this.formComponent;
	}

	/**
	 * Process a violation.
	 * 
	 * @param violation
	 *            The violation.
	 */
	private void processViolation(final PropertyViolation<?, ?> violation) {
		final Field field = this.formComponent.getForm().getField(violation.getProperty().getName());
		if (field instanceof AbstractComponent) {
			final AbstractComponent component = (AbstractComponent) field;
			component.setComponentError(new UserError(violation.getMessage()));
		}
	}

	/**
	 * Sets a new value for the formComponent field.
	 * 
	 * @param formComponent
	 *            The new value for the formComponent field.
	 */
	public void setFormComponent(final FormComponentImpl<S, E> formComponent) {
		this.formComponent = formComponent;
	}
}
