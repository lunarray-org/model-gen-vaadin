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
package org.lunarray.model.generation.vaadin.render.factories.form.impl.property;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.converter.exceptions.ConverterException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.model.extension.ExtensionRef;
import org.lunarray.model.descriptor.model.member.Cardinality;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationEntityDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.lunarray.model.descriptor.validator.PropertyViolation;
import org.lunarray.model.descriptor.validator.ValueValidator;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.AccessBuffer;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.MutateBuffer;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.ValueChangedListener;
import org.lunarray.model.generation.vaadin.render.factories.form.events.EntityChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract property descriptor.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public abstract class AbstractPropertyDescriptorImpl<P, E>
		implements Descriptor<P>, AccessBuffer<P>, MutateBuffer<P> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPropertyDescriptorImpl.class);
	/** The serial id. */
	private static final long serialVersionUID = 61705228894679650L;
	/** Change listeners. */
	private List<ValueChangeListener> changeListeners;
	/** The converter. */
	private ExtensionRef<ConverterTool> converterTool;
	/** The entity. */
	private E entity;
	/** The listeners. */
	private List<ValueChangedListener<P>> listeners;
	/** The model. */
	private Model<?> model;
	/** The preferred type. */
	private Class<?> prefferedType;
	/** The presentation property. */
	private PresentationPropertyDescriptor<P, E> presentationProperty;
	/** The property. */
	private PropertyDescriptor<P, E> property;
	/** The relation descriptor. */
	private RelationDescriptor relationDescriptor;
	/** The validator. */
	private ExtensionRef<ValueValidator> valueValidator;

	/**
	 * Default constructor.
	 * 
	 * @param property
	 *            The property. May not be null.
	 * @param model
	 *            The model. May not be null.
	 */
	@SuppressWarnings("unchecked")
	public AbstractPropertyDescriptorImpl(final PropertyDescriptor<P, E> property, final Model<? super E> model) {
		Validate.notNull(property, "Property may not be null.");
		Validate.notNull(model, "Model may not be null.");
		this.property = property;
		this.model = model;
		this.presentationProperty = property.adapt(PresentationPropertyDescriptor.class);
		this.relationDescriptor = property.adapt(RelationDescriptor.class);
		this.converterTool = model.getExtensionRef(ConverterTool.class);
		Validate.notNull(this.converterTool, "Model must feature a converter tool.");
		this.valueValidator = model.getExtensionRef(ValueValidator.class);
		this.listeners = new LinkedList<ValueChangedListener<P>>();
		this.changeListeners = new LinkedList<ValueChangeListener>();
	}

	/** {@inheritDoc} */
	@Override
	public final void addListener(final ValueChangedListener<P> listener) {
		this.listeners.add(listener);
	}

	/** {@inheritDoc} */
	@Override
	public final void addListener(final ValueChangeListener listener) {
		this.changeListeners.add(listener);
	}

	/**
	 * Coerce to this type.
	 * 
	 * @param value
	 *            The value to coerce.
	 * @return The coerced type.
	 */
	@SuppressWarnings("unchecked")
	public final P coerceValue(final Object value) {
		final ConverterTool tool = this.getConverterTool().get();
		P result = null;
		String format = null;
		if (!CheckUtil.isNull(this.presentationProperty)) {
			format = this.presentationProperty.getFormat();
		}
		AbstractPropertyDescriptorImpl.LOGGER.debug("Coercing value {} with format {}.", value, format);
		try {
			if (this.property.getPropertyType().isInstance(value)) {
				result = (P) value;
			} else {
				final Class<Object> type = (Class<Object>) value.getClass();
				AbstractPropertyDescriptorImpl.LOGGER.debug("Coercing value {} to string with format {}.", value, format);
				final String stringValue = tool.convertToString(type, value, format);
				AbstractPropertyDescriptorImpl.LOGGER.debug("Coercing value {} to {} with format {}.", stringValue, type, format);
				result = tool.convertToInstance(this.property.getPropertyType(), stringValue, format);
			}
		} catch (final ConverterException e) {
			throw new ConversionException(e.getMessage(), e);
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final AccessBuffer<P> getBufferAccessor() {
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public final MutateBuffer<P> getBufferMutator() {
		return this;
	}

	/**
	 * Gets the value for the changeListeners field.
	 * 
	 * @return The value for the changeListeners field.
	 */
	public final List<ValueChangeListener> getChangeListeners() {
		return this.changeListeners;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public final <T> T getCoerceValue(final Class<T> valueType) {
		Validate.notNull(valueType, "Coerce type may not be null.");
		final ConverterTool tool = this.getConverterTool().get();
		T result = null;
		String format = null;
		if (!CheckUtil.isNull(this.presentationProperty)) {
			format = this.presentationProperty.getFormat();
		}
		AbstractPropertyDescriptorImpl.LOGGER.debug("Coercing value to {} with format {}.", valueType, format);
		try {
			if (valueType.isAssignableFrom(this.property.getPropertyType())) {
				result = (T) this.property.getValue(this.entity);
			} else {
				final String stringValue = tool.convertToString(this.property.getPropertyType(), this.property.getValue(this.entity),
						format);
				result = tool.convertToInstance(valueType, stringValue, format);
			}
		} catch (final ConverterException e) {
			throw new ConversionException(e.getMessage(), e);
		} catch (final ValueAccessException e) {
			throw new ConversionException(e.getMessage(), e);
		}
		return result;
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
	public final P getDirectValue() {
		try {
			return this.property.getValue(this.entity);
		} catch (final ValueAccessException e) {
			throw new ConversionException(e.getMessage(), e);
		}
	}

	/**
	 * Gets the value for the entity field.
	 * 
	 * @return The value for the entity field.
	 */
	public final E getEntity() {
		return this.entity;
	}

	/** {@inheritDoc} */
	@Override
	public final String getLabel() {
		String labelText;
		if (CheckUtil.isNull(this.presentationProperty)) {
			labelText = this.property.getName();
		} else {
			labelText = this.presentationProperty.getDescription();
		}
		return labelText;
	}

	/**
	 * Gets the value for the listeners field.
	 * 
	 * @return The value for the listeners field.
	 */
	public final List<ValueChangedListener<P>> getListeners() {
		return this.listeners;
	}

	/**
	 * Gets the value for the model field.
	 * 
	 * @return The value for the model field.
	 */
	public final Model<?> getModel() {
		return this.model;
	}

	/** {@inheritDoc} */
	@Override
	public final String getName() {
		return this.getProperty().getName();
	}

	/**
	 * Gets the value for the prefferedType field.
	 * 
	 * @return The value for the prefferedType field.
	 */
	public final Class<?> getPrefferedType() {
		return this.prefferedType;
	}

	/**
	 * Gets the value for the presentationProperty field.
	 * 
	 * @return The value for the presentationProperty field.
	 */
	public final PresentationPropertyDescriptor<P, E> getPresentationProperty() {
		return this.presentationProperty;
	}

	/**
	 * Gets the value for the property field.
	 * 
	 * @return The value for the property field.
	 */
	public final PropertyDescriptor<P, E> getProperty() {
		return this.property;
	}

	/**
	 * Gets the value for the relationDescriptor field.
	 * 
	 * @return The value for the relationDescriptor field.
	 */
	public final RelationDescriptor getRelationDescriptor() {
		return this.relationDescriptor;
	}

	/** {@inheritDoc} */
	@Override
	public final String getRelationName() {
		String result = null;
		if (this.isRelated()) {
			result = this.relationDescriptor.getRelatedName();
		}
		return result;
	}

	/**
	 * Gets the value.
	 * 
	 * @return The value.
	 */
	@Override
	public final String getStringValue() {
		final ConverterTool tool = this.getConverterTool().get();
		String result = null;
		String format = null;
		if (!CheckUtil.isNull(this.presentationProperty)) {
			format = this.presentationProperty.getFormat();
		}
		try {
			result = tool.convertToString(this.property.getPropertyType(), this.property.getValue(this.entity), format);
		} catch (final ConverterException e) {
			throw new ConversionException(e.getMessage(), e);
		} catch (final ValueAccessException e) {
			throw new ConversionException(e.getMessage(), e);
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final Class<?> getType() {
		return this.property.getPropertyType();
	}

	/** {@inheritDoc} */
	@Override
	public final Object getValue() {
		return this.getCoerceValue(this.prefferedType);
	}

	/**
	 * Gets the value for the valueValidator field.
	 * 
	 * @return The value for the valueValidator field.
	 */
	public final ExtensionRef<ValueValidator> getValueValidator() {
		return this.valueValidator;
	}

	/** {@inheritDoc} */
	@Override
	public final boolean isReadOnly() {
		return this.property.isImmutable();
	}

	/** {@inheritDoc} */
	@Override
	public final boolean isRelated() {
		return !CheckUtil.isNull(this.relationDescriptor);
	}

	/** {@inheritDoc} */
	@Override
	public final boolean isRequired() {
		boolean required = false;
		if (CheckUtil.isNull(this.presentationProperty)) {
			required = this.property.getCardinality() == Cardinality.SINGLE;
		} else {
			required = this.presentationProperty.isRequiredIndication();
		}
		return required;
	}

	/** {@inheritDoc} */
	@Override
	public final boolean isValid(final Object value) {
		boolean result = true;
		final ValueValidator validator = this.valueValidator.get();
		if (!CheckUtil.isNull(validator)) {
			Collection<PropertyViolation<E, P>> violations;
			violations = validator.validateValue(this.property, this.coerceValue(value));
			result = violations.isEmpty();
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final void removeListener(final ValueChangedListener<P> listener) {
		this.listeners.remove(listener);
	}

	/** {@inheritDoc} */
	@Override
	public final void removeListener(final ValueChangeListener listener) {
		this.changeListeners.remove(listener);
	}

	/**
	 * Sets a new value for the changeListeners field.
	 * 
	 * @param changeListeners
	 *            The new value for the changeListeners field.
	 */
	public final void setChangeListeners(final List<ValueChangeListener> changeListeners) {
		this.changeListeners = changeListeners;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public final <T> void setCoerceValue(final T value) {
		final ConverterTool tool = this.getConverterTool().get();
		String format = null;
		if (!CheckUtil.isNull(this.presentationProperty)) {
			format = this.presentationProperty.getFormat();
		}
		try {
			if (this.property.isAssignable(value)) {
				this.property.setValue(this.entity, (P) value);
			} else {
				final String stringValue = tool.convertToString((Class<T>) value.getClass(), value, format);
				this.property.setValue(this.entity, tool.convertToInstance(this.property.getPropertyType(), stringValue, format));
			}
		} catch (final ConverterException e) {
			throw new ConversionException(e.getMessage(), e);
		} catch (final ValueAccessException e) {
			throw new ConversionException(e.getMessage(), e);
		}
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

	/** {@inheritDoc} */
	@Override
	public final void setDirectValue(final P value) {
		try {
			this.property.setValue(this.entity, value);
		} catch (final ValueAccessException e) {
			throw new ConversionException(e.getMessage(), e);
		}
	}

	/**
	 * Sets a new value for the entity field.
	 * 
	 * @param entity
	 *            The new value for the entity field.
	 */
	public final void setEntity(final E entity) {
		this.entity = entity;
		final EntityChangeEvent event = new EntityChangeEvent(this);
		for (final ValueChangeListener listener : this.changeListeners) {
			listener.valueChange(event);
		}
	}

	/**
	 * Sets a new value for the listeners field.
	 * 
	 * @param listeners
	 *            The new value for the listeners field.
	 */
	public final void setListeners(final List<ValueChangedListener<P>> listeners) {
		this.listeners = listeners;
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

	/** {@inheritDoc} */
	@Override
	public final void setPreferredType(final Class<?> preferred) {
		this.prefferedType = preferred;
	}

	/**
	 * Sets a new value for the prefferedType field.
	 * 
	 * @param prefferedType
	 *            The new value for the prefferedType field.
	 */
	public final void setPrefferedType(final Class<?> prefferedType) {
		this.prefferedType = prefferedType;
	}

	/**
	 * Sets a new value for the presentationProperty field.
	 * 
	 * @param presentationProperty
	 *            The new value for the presentationProperty field.
	 */
	public final void setPresentationProperty(final PresentationPropertyDescriptor<P, E> presentationProperty) {
		this.presentationProperty = presentationProperty;
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

	/** {@inheritDoc} */
	@Override
	public final void setReadOnly(final boolean newStatus) {
		AbstractPropertyDescriptorImpl.LOGGER.warn("Could not set to read only.");
	}

	/**
	 * Sets a new value for the relationDescriptor field.
	 * 
	 * @param relationDescriptor
	 *            The new value for the relationDescriptor field.
	 */
	public final void setRelationDescriptor(final RelationDescriptor relationDescriptor) {
		this.relationDescriptor = relationDescriptor;
	}

	/**
	 * Sets the value.
	 * 
	 * @param stringValue
	 *            The value.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final void setStringValue(final String stringValue) {
		final ConverterTool tool = this.getConverterTool().get();
		String format = null;
		if (!CheckUtil.isNull(this.presentationProperty)) {
			format = this.presentationProperty.getFormat();
		}
		try {
			if (this.property.isAssignable(stringValue)) {
				this.property.setValue(this.entity, (P) stringValue);
			} else {
				this.property.setValue(this.entity, tool.convertToInstance(this.property.getPropertyType(), stringValue, format));
			}
		} catch (final ConverterException e) {
			throw new ConversionException(e.getMessage(), e);
		} catch (final ValueAccessException e) {
			throw new ConversionException(e.getMessage(), e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public final void setValue(final Object newValue) throws ConversionException {
		this.setCoerceValue(newValue);
	}

	/**
	 * Sets a new value for the valueValidator field.
	 * 
	 * @param valueValidator
	 *            The new value for the valueValidator field.
	 */
	public final void setValueValidator(final ExtensionRef<ValueValidator> valueValidator) {
		this.valueValidator = valueValidator;
	}

	/** {@inheritDoc} */
	@Override
	public final String toString() {
		String result;
		if (this.isRelated()) {
			result = this.toStringProperty();
		} else {
			result = this.getStringValue();
		}
		if (CheckUtil.isNull(result)) {
			result = "";
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final void validate(final Object value) throws InvalidValueException {
		final ValueValidator validator = this.valueValidator.get();
		if (!CheckUtil.isNull(validator)) {
			Collection<PropertyViolation<E, P>> violations;
			violations = validator.validateValue(this.property, this.coerceValue(value));
			if (!violations.isEmpty()) {
				final PropertyViolation<E, P> violation = violations.iterator().next();
				throw new InvalidValueException(violation.getMessage());
			}
		}
	}

	/**
	 * Process as presentation entity.
	 * 
	 * @param entityDescriptor
	 *            The entity descriptor.
	 * @return The result.
	 */
	private PropertyDescriptor<?, P> processPresentation(final EntityDescriptor<P> entityDescriptor) {
		PropertyDescriptor<?, P> result = null;
		@SuppressWarnings("unchecked")
		final PresentationEntityDescriptor<P> presentationDescriptor = entityDescriptor.adapt(PresentationEntityDescriptor.class);
		if (!CheckUtil.isNull(presentationDescriptor)) {
			final PropertyDescriptor<?, P> nameDescriptor = presentationDescriptor.getNameProperty();
			if (!CheckUtil.isNull(nameDescriptor)) {
				result = nameDescriptor;
			}
		}
		return result;
	}

	/**
	 * Resolves what property of the relation entity should be displayed, if
	 * any.
	 * 
	 * @return The property name, or null.
	 */
	private PropertyDescriptor<?, P> resolveDisplayProperty() {
		PropertyDescriptor<?, P> result = null;
		if (this.isRelated()) {
			@SuppressWarnings("unchecked")
			final EntityDescriptor<P> entityDescriptor = (EntityDescriptor<P>) this.model.getEntity(this.getRelationName());
			@SuppressWarnings("unchecked")
			final KeyedEntityDescriptor<P, ?> keyedDescriptor = entityDescriptor.adapt(KeyedEntityDescriptor.class);
			result = this.processPresentation(entityDescriptor);
			if (CheckUtil.isNull(result) && !CheckUtil.isNull(keyedDescriptor)) {
				result = keyedDescriptor.getKeyProperty();
			}
		}
		return result;
	}

	/**
	 * To string.
	 * 
	 * @return The string.
	 * @param <Q>
	 *            Intermediate type.
	 */
	private <Q> String toStringProperty() {
		String result = null;
		@SuppressWarnings("unchecked")
		final PropertyDescriptor<Q, P> propertyDescriptor = (PropertyDescriptor<Q, P>) this.resolveDisplayProperty();
		final ConverterTool tool = this.converterTool.get();
		if (!CheckUtil.isNull(propertyDescriptor)) {
			try {
				try {
					result = tool.convertToString(propertyDescriptor.getPropertyType(),
							propertyDescriptor.getValue(this.property.getValue(this.entity)));
				} catch (final ConverterException e) {
					AbstractPropertyDescriptorImpl.LOGGER.warn("Could not convert.", e);
				}
				if (CheckUtil.isNull(result)) {
					result = propertyDescriptor.getValue(this.property.getValue(this.entity)).toString();
				}
			} catch (final ValueAccessException e) {
				AbstractPropertyDescriptorImpl.LOGGER.warn("Could not access.", e);
			}
		}
		if (CheckUtil.isNull(result)) {
			try {
				result = this.property.getValue(this.entity).toString();
			} catch (final ValueAccessException e) {
				throw new ConversionException(e.getMessage(), e);
			}
		}
		return result;
	}
}
