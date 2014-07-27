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
import java.util.Iterator;

import com.vaadin.data.Validator;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.validator.PropertyViolation;
import org.lunarray.model.descriptor.validator.ValueValidator;
import org.lunarray.model.generation.vaadin.util.MessageUtil;

/**
 * Validates values in the form, on violation, displays message.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public final class ValueValidationListener<P, E>
		implements Validator {

	/** Invalid format. */
	private static final String INVALID_VALUE = "validation.value.invalid";
	/** Serial id. */
	private static final long serialVersionUID = -996738269982012369L;

	/**
	 * Creates a value validator.
	 * 
	 * @param propertyDescriptor
	 *            The property descriptor.
	 * @param model
	 *            The model.
	 * @return The validation listener.
	 * @param <P>
	 *            The property type.
	 * @param <E>
	 *            The entity type.
	 */
	public static <P, E> ValueValidationListener<P, E> createListener(final PropertyDescriptor<P, E> propertyDescriptor,
			final Model<?> model) {
		Validate.notNull(propertyDescriptor, "Property descriptor may not be null.");
		Validate.notNull(model, "Model may not be null.");
		return new ValueValidationListener<P, E>(propertyDescriptor, model);
	}

	/** The model. */
	private Model<?> model;

	/** The property descriptor. */
	private PropertyDescriptor<P, E> propertyDescriptor;

	/**
	 * Default constructor.
	 * 
	 * @param propertyDescriptor
	 *            The property descriptor.
	 * @param model
	 *            The model.
	 */
	private ValueValidationListener(final PropertyDescriptor<P, E> propertyDescriptor, final Model<?> model) {
		this.propertyDescriptor = propertyDescriptor;
		this.model = model;
	}

	/**
	 * Gets the value for the model field.
	 * 
	 * @return The value for the model field.
	 */
	public Model<?> getModel() {
		return this.model;
	}

	/**
	 * Gets the value for the propertyDescriptor field.
	 * 
	 * @return The value for the propertyDescriptor field.
	 */
	public PropertyDescriptor<P, E> getPropertyDescriptor() {
		return this.propertyDescriptor;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isValid(final Object value) {
		boolean valid = true;
		if (this.propertyDescriptor.isAssignable(value)) {
			@SuppressWarnings("unchecked")
			// Check happens through the isAssignable.
			final P propertyValue = (P) value;
			final ValueValidator validator = this.model.getExtension(ValueValidator.class);
			if (!CheckUtil.isNull(validator)) {
				valid = validator.validateValue(this.propertyDescriptor, propertyValue).isEmpty();
			}
		} else {
			valid = false;
		}
		return valid;
	}

	/**
	 * Sets a new value for the model field.
	 * 
	 * @param model
	 *            The new value for the model field.
	 */
	public void setModel(final Model<?> model) {
		this.model = model;
	}

	/**
	 * Sets a new value for the propertyDescriptor field.
	 * 
	 * @param propertyDescriptor
	 *            The new value for the propertyDescriptor field.
	 */
	public void setPropertyDescriptor(final PropertyDescriptor<P, E> propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object value) {
		if (this.propertyDescriptor.isAssignable(value)) {
			@SuppressWarnings("unchecked")
			// Check happens through the isAssignable.
			final P propertyValue = (P) value;
			final ValueValidator validator = this.model.getExtension(ValueValidator.class);
			this.processValidation(propertyValue, validator);
		} else {
			throw new InvalidValueException(MessageUtil.getMessage(ValueValidationListener.INVALID_VALUE));
		}
	}

	/**
	 * Process the validation.
	 * 
	 * @param propertyValue
	 *            The property to validate.
	 * @param validator
	 *            The validator.
	 */
	private void processValidation(final P propertyValue, final ValueValidator validator) {
		if (!CheckUtil.isNull(validator)) {
			final Collection<PropertyViolation<E, P>> violations = validator.validateValue(this.propertyDescriptor, propertyValue);
			final Iterator<PropertyViolation<E, P>> violationsIt = violations.iterator();
			if (violationsIt.hasNext()) {
				throw new InvalidValueException(violationsIt.next().getMessage());
			}
		}
	}
}
