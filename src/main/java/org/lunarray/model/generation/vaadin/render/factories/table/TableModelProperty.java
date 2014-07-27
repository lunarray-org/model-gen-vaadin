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
package org.lunarray.model.generation.vaadin.render.factories.table;

import com.vaadin.data.Property;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.converter.exceptions.ConverterException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.model.extension.ExtensionRef;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationEntityDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstracts the property descriptor to a Vaadin property.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public final class TableModelProperty<P, E>
		implements Property {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TableModelProperty.class);
	/** Serial id. */
	private static final long serialVersionUID = 1440299225904911584L;
	/** The converter tool. */
	private ExtensionRef<ConverterTool> converterTool;
	/** The entity. */
	private E entity;
	/** The format. */
	private String format;
	/** The model. */
	private Model<?> model;
	/** The property. */
	private PropertyDescriptor<P, E> property;

	/**
	 * 
	 * Default constructor.
	 * 
	 * @param converterTool
	 *            The converter tool. May not be null.
	 * @param property
	 *            The property. May not be null.
	 * @param entity
	 *            The entity. May not be null.
	 * @param format
	 *            The render format.
	 * @param model
	 *            The model. May not be null.
	 */
	public TableModelProperty(final ExtensionRef<ConverterTool> converterTool, final PropertyDescriptor<P, E> property, final E entity,
			final String format, final Model<?> model) {
		Validate.notNull(converterTool, "Converter tool may not be null.");
		Validate.notNull(property, "Property may not be null.");
		Validate.notNull(entity, "Entity may not be null.");
		Validate.notNull(model, "Model may not be null.");
		this.converterTool = converterTool;
		this.property = property;
		this.entity = entity;
		this.format = format;
		this.model = model;
	}

	/**
	 * Converts to a string.
	 * 
	 * @param entity
	 *            The entity. May not be null.
	 * @return The string.
	 * @throws ConverterException
	 *             Thrown if the entity could not be converter.
	 * @param <C>
	 *            The entity type.
	 */
	public <C> String convertToString(final C entity) throws ConverterException {
		Validate.notNull(entity, "May not be null.");
		@SuppressWarnings("unchecked")
		final Class<C> entityType = (Class<C>) entity.getClass();
		return this.converterTool.get().convertToString(entityType, entity, this.format);
	}

	/**
	 * Gets the value for the converterTool field.
	 * 
	 * @return The value for the converterTool field.
	 */
	public ExtensionRef<ConverterTool> getConverterTool() {
		return this.converterTool;
	}

	/**
	 * Gets the value for the entity field.
	 * 
	 * @return The value for the entity field.
	 */
	public E getEntity() {
		return this.entity;
	}

	/**
	 * Gets the value for the format field.
	 * 
	 * @return The value for the format field.
	 */
	public String getFormat() {
		return this.format;
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
	 * Gets the value for the property field.
	 * 
	 * @return The value for the property field.
	 */
	public PropertyDescriptor<P, E> getProperty() {
		return this.property;
	}

	/** {@inheritDoc} */
	@Override
	public Class<P> getType() {
		return this.getProperty().getPropertyType();
	}

	/** {@inheritDoc} */
	@Override
	public P getValue() {
		try {
			return this.getProperty().getValue(this.getEntity());
		} catch (final ValueAccessException e) {
			throw new IllegalStateException("Could not get value.", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean isReadOnly() {
		return this.getProperty().isImmutable();
	}

	/**
	 * Sets a new value for the converterTool field.
	 * 
	 * @param converterTool
	 *            The new value for the converterTool field.
	 */
	public void setConverterTool(final ExtensionRef<ConverterTool> converterTool) {
		this.converterTool = converterTool;
	}

	/**
	 * Sets a new value for the entity field.
	 * 
	 * @param entity
	 *            The new value for the entity field.
	 */
	public void setEntity(final E entity) {
		this.entity = entity;
	}

	/**
	 * Sets a new value for the format field.
	 * 
	 * @param format
	 *            The new value for the format field.
	 */
	public void setFormat(final String format) {
		this.format = format;
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
	 * Sets a new value for the property field.
	 * 
	 * @param property
	 *            The new value for the property field.
	 */
	public void setProperty(final PropertyDescriptor<P, E> property) {
		this.property = property;
	}

	/**
	 * Sets the property value.
	 * 
	 * @param propertyValue
	 *            The property value.
	 */
	public void setPropertyValue(final P propertyValue) {
		try {
			this.getProperty().setValue(this.getEntity(), propertyValue);
		} catch (final ValueAccessException e) {
			TableModelProperty.LOGGER.warn("Could not access value.", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void setReadOnly(final boolean newStatus) {
		// Ignore
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(final Object newValue) {
		final Class<P> propertyType = this.getType();
		if (!CheckUtil.isNull(newValue)) {
			P propertyValue;
			try {
				if (propertyType.equals(newValue.getClass())) {
					propertyValue = propertyType.cast(newValue);
				} else if (newValue instanceof String) {
					propertyValue = this.converterTool.get().convertToInstance(propertyType, (String) newValue, this.format);
				} else {
					final String intermediaValue = this.convertToString(newValue);
					propertyValue = this.converterTool.get().convertToInstance(propertyType, intermediaValue, this.format);
				}
			} catch (final ConverterException e) {
				throw new ConversionException(String.format("Could not set convert of type %s with %s.", propertyType, newValue), e);
			}
			if (this.property.isAssignable(propertyValue)) {
				this.setPropertyValue(propertyValue);
			} else {
				throw new ConversionException(String.format("Could not set property of type %s with %s.", propertyType, newValue));
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final P value = this.getValue();
		String result = null;
		if (CheckUtil.isNull(value)) {
			result = "";
		}
		final ConverterTool tool = this.getConverterTool().get();
		if (CheckUtil.isNull(result) && this.property.isRelation()) {
			result = this.resolveRelation(value, tool);
		}
		if (CheckUtil.isNull(result)) {
			try {
				result = tool.convertToString(this.getProperty().getPropertyType(), value, this.format);
			} catch (final ConverterException e) {
				TableModelProperty.LOGGER.debug("Could not convert in toString.", e);
				result = value.toString();
			}
		}
		if (CheckUtil.isNull(result)) {
			result = "";
		}
		return result;
	}

	/**
	 * Resolve the relation name.
	 * 
	 * @param value
	 *            The value.
	 * @param tool
	 *            The converter.
	 * @return The string representation, or null.
	 */
	private String resolveRelation(final P value, final ConverterTool tool) {
		String result = null;
		final RelationDescriptor relationDescriptor = this.property.adapt(RelationDescriptor.class);
		final String relationName = relationDescriptor.getRelatedName();
		@SuppressWarnings("unchecked")
		final EntityDescriptor<P> relation = (EntityDescriptor<P>) this.model.getEntity(relationName);
		@SuppressWarnings("unchecked")
		final PresentationEntityDescriptor<P> presentationDescriptor = relation.adapt(PresentationEntityDescriptor.class);

		if (!CheckUtil.isNull(presentationDescriptor)) {
			@SuppressWarnings("unchecked")
			final PropertyDescriptor<Object, P> name = (PropertyDescriptor<Object, P>) presentationDescriptor.getNameProperty();
			if (!CheckUtil.isNull(name)) {
				try {
					final Object nameValue = name.getValue(value);
					result = tool.convertToString(name.getPropertyType(), nameValue, this.format);
				} catch (final ConverterException e) {
					TableModelProperty.LOGGER.warn("Could not convert value.", e);
					result = value.toString();
				} catch (final ValueAccessException e) {
					TableModelProperty.LOGGER.warn("Could not access value.", e);
					result = value.toString();
				}
			}
		}
		@SuppressWarnings("unchecked")
		final KeyedEntityDescriptor<P, ?> keyedDescriptor = relation.adapt(KeyedEntityDescriptor.class);
		if (CheckUtil.isNull(result) && !CheckUtil.isNull(keyedDescriptor)) {
			@SuppressWarnings("unchecked")
			final PropertyDescriptor<Object, P> key = (PropertyDescriptor<Object, P>) keyedDescriptor.getKeyProperty();
			try {
				final Object keyValue = key.getValue(value);
				result = tool.convertToString(key.getPropertyType(), keyValue, this.format);
			} catch (final ValueAccessException e) {
				TableModelProperty.LOGGER.warn("Could not access key value.", e);
				result = value.toString();
			} catch (final ConverterException e) {
				TableModelProperty.LOGGER.warn("Could not convert key value.", e);
				result = value.toString();
			}
		}
		return result;
	}
}
