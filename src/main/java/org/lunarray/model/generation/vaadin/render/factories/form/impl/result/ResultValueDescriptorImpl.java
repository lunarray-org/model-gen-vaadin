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
package org.lunarray.model.generation.vaadin.render.factories.form.impl.result;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.common.event.EventException;
import org.lunarray.common.event.Listener;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.converter.exceptions.ConverterException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.model.extension.ExtensionRef;
import org.lunarray.model.descriptor.model.operation.OperationDescriptor;
import org.lunarray.model.descriptor.model.operation.result.ResultDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationEntityDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationResultDescriptor;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.AccessBuffer;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.MutateBuffer;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.ValueChangedListener;
import org.lunarray.model.generation.vaadin.render.factories.form.events.OperationInvocationEvent;
import org.lunarray.model.generation.vaadin.render.factories.form.events.OperationValueChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The result value descriptor.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <R>
 *            The result type.
 */
public final class ResultValueDescriptorImpl<R>
		implements Descriptor<R>, AccessBuffer<R>, MutateBuffer<R>, Listener<OperationInvocationEvent<?, R>> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ResultValueDescriptorImpl.class);
	/** The serial id. */
	private static final long serialVersionUID = -915757680475327083L;
	/** The buffered value. */
	private R bufferValue;
	/** The change listeners. */
	private List<ValueChangeListener> changeListeners;
	/** The converter tool. */
	private ExtensionRef<ConverterTool> converterTool;
	/** The descriptor. */
	private OperationDescriptor<?> descriptor;
	/** The listeners. */
	private List<ValueChangedListener<R>> listeners;
	/** The model. */
	private Model<?> model;
	/** The preferred type. */
	private Class<?> prefferedType;
	/** The presentation descriptor. */
	private PresentationResultDescriptor<R> presentationResult;
	/** The relation descriptor. */
	private RelationDescriptor relationDescriptor;
	/** The result descriptor. */
	private ResultDescriptor<R> resultDescriptor;

	/**
	 * Default constructor.
	 * 
	 * @param resultDescriptor
	 *            The property.
	 * @param descriptor
	 *            The descriptor.
	 * @param model
	 *            The model.
	 */
	@SuppressWarnings("unchecked")
	public ResultValueDescriptorImpl(final ResultDescriptor<R> resultDescriptor, final OperationDescriptor<?> descriptor,
			final Model<?> model) {
		Validate.notNull(resultDescriptor, "Result descriptor may not be null.");
		Validate.notNull(model, "Model may not be null.");
		Validate.notNull(descriptor, "Operation descriptor may not be null.");
		this.resultDescriptor = resultDescriptor;
		this.presentationResult = resultDescriptor.adapt(PresentationResultDescriptor.class);
		this.relationDescriptor = resultDescriptor.adapt(RelationDescriptor.class);
		this.converterTool = model.getExtensionRef(ConverterTool.class);
		Validate.notNull(this.converterTool, "Model must feature a converter tool.");
		this.listeners = new LinkedList<ValueChangedListener<R>>();
		this.descriptor = descriptor;
		this.changeListeners = new LinkedList<ValueChangeListener>();
		this.model = model;
	}

	/** {@inheritDoc} */
	@Override
	public void addListener(final ValueChangedListener<R> listener) {
		this.listeners.add(listener);
	}

	/** {@inheritDoc} */
	@Override
	public void addListener(final ValueChangeListener listener) {
		this.changeListeners.add(listener);
	}

	/** {@inheritDoc} */
	@Override
	public AccessBuffer<R> getBufferAccessor() {
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public MutateBuffer<R> getBufferMutator() {
		return this;
	}

	/**
	 * Gets the value for the bufferValue field.
	 * 
	 * @return The value for the bufferValue field.
	 */
	public R getBufferValue() {
		return this.bufferValue;
	}

	/**
	 * Gets the value for the changeListeners field.
	 * 
	 * @return The value for the changeListeners field.
	 */
	public List<ValueChangeListener> getChangeListeners() {
		return this.changeListeners;
	}

	/** {@inheritDoc} */
	@Override
	public <T> T getCoerceValue(final Class<T> valueType) {
		Validate.notNull(valueType, "Coerce type may not be null.");
		final ConverterTool tool = this.converterTool.get();
		T result = null;
		String format = null;
		if (!CheckUtil.isNull(this.presentationResult)) {
			format = this.presentationResult.getFormat();
		}
		ResultValueDescriptorImpl.LOGGER.debug("Coercing value {} to {} with format {}.", this.bufferValue, valueType, format);
		try {
			if (valueType.isAssignableFrom(this.getType())) {
				result = valueType.cast(this.bufferValue);
			} else {
				final String stringValue = tool.convertToString(this.resultDescriptor.getResultType(), this.bufferValue, format);
				result = tool.convertToInstance(valueType, stringValue, format);
			}
		} catch (final ConverterException e) {
			throw new ConversionException(e.getMessage(), e);
		}
		return result;
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
	 * Gets the value for the descriptor field.
	 * 
	 * @return The value for the descriptor field.
	 */
	public OperationDescriptor<?> getDescriptor() {
		return this.descriptor;
	}

	/** {@inheritDoc} */
	@Override
	public R getDirectValue() {
		return this.bufferValue;
	}

	/** {@inheritDoc} */
	@Override
	public String getLabel() {
		return "";
	}

	/**
	 * Gets the value for the listeners field.
	 * 
	 * @return The value for the listeners field.
	 */
	public List<ValueChangedListener<R>> getListeners() {
		return this.listeners;
	}

	/**
	 * Gets the value for the model field.
	 * 
	 * @return The value for the model field.
	 */
	public Model<?> getModel() {
		return this.model;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return this.descriptor.getName();
	}

	/**
	 * Gets the value for the prefferedType field.
	 * 
	 * @return The value for the prefferedType field.
	 */
	public Class<?> getPrefferedType() {
		return this.prefferedType;
	}

	/**
	 * Gets the value for the presentationResult field.
	 * 
	 * @return The value for the presentationResult field.
	 */
	public PresentationResultDescriptor<R> getPresentationResult() {
		return this.presentationResult;
	}

	/**
	 * Gets the value for the relationDescriptor field.
	 * 
	 * @return The value for the relationDescriptor field.
	 */
	public RelationDescriptor getRelationDescriptor() {
		return this.relationDescriptor;
	}

	/** {@inheritDoc} */
	@Override
	public String getRelationName() {
		String result = null;
		if (this.isRelated()) {
			result = this.relationDescriptor.getRelatedName();
		}
		return result;
	}

	/**
	 * Gets the value for the resultDescriptor field.
	 * 
	 * @return The value for the resultDescriptor field.
	 */
	public ResultDescriptor<R> getResultDescriptor() {
		return this.resultDescriptor;
	}

	/** {@inheritDoc} */
	@Override
	public String getStringValue() {
		final ConverterTool tool = this.converterTool.get();
		String result = null;
		String format = null;
		if (!CheckUtil.isNull(this.presentationResult)) {
			format = this.presentationResult.getFormat();
		}
		try {
			result = tool.convertToString(this.resultDescriptor.getResultType(), this.bufferValue, format);
		} catch (final ConverterException e) {
			throw new ConversionException(e.getMessage(), e);
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public Class<?> getType() {
		return this.resultDescriptor.getResultType();
	}

	/** {@inheritDoc} */
	@Override
	public Object getValue() {
		return this.getCoerceValue(this.prefferedType);
	}

	/** {@inheritDoc} */
	@Override
	public void handleEvent(final OperationInvocationEvent<?, R> event) throws EventException {
		this.bufferValue = event.getResult();
		for (final ValueChangedListener<R> listener : this.listeners) {
			listener.valueChanged(this.bufferValue);
		}
		final OperationValueChangeEvent cvce = new OperationValueChangeEvent(this);
		for (final ValueChangeListener listener : this.changeListeners) {
			listener.valueChange(cvce);
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean isReadOnly() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isRelated() {
		return this.resultDescriptor.isRelation();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isRequired() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isValid(final Object value) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void removeListener(final ValueChangedListener<R> listener) {
		this.listeners.remove(listener);
	}

	/** {@inheritDoc} */
	@Override
	public void removeListener(final ValueChangeListener listener) {
		this.changeListeners.remove(listener);
	}

	/**
	 * Sets a new value for the bufferValue field.
	 * 
	 * @param bufferValue
	 *            The new value for the bufferValue field.
	 */
	public void setBufferValue(final R bufferValue) {
		this.bufferValue = bufferValue;
	}

	/**
	 * Sets a new value for the changeListeners field.
	 * 
	 * @param changeListeners
	 *            The new value for the changeListeners field.
	 */
	public void setChangeListeners(final List<ValueChangeListener> changeListeners) {
		this.changeListeners = changeListeners;
	}

	/** {@inheritDoc} */
	@Override
	public <T> void setCoerceValue(final T value) {
		throw new ReadOnlyException();
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
	 * Sets a new value for the descriptor field.
	 * 
	 * @param descriptor
	 *            The new value for the descriptor field.
	 */
	public void setDescriptor(final OperationDescriptor<?> descriptor) {
		this.descriptor = descriptor;
	}

	/** {@inheritDoc} */
	@Override
	public void setDirectValue(final R value) {
		throw new ReadOnlyException();
	}

	/**
	 * Sets a new value for the listeners field.
	 * 
	 * @param listeners
	 *            The new value for the listeners field.
	 */
	public void setListeners(final List<ValueChangedListener<R>> listeners) {
		this.listeners = listeners;
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

	/** {@inheritDoc} */
	@Override
	public void setPreferredType(final Class<?> preferred) {
		this.prefferedType = preferred;
	}

	/**
	 * Sets a new value for the prefferedType field.
	 * 
	 * @param prefferedType
	 *            The new value for the prefferedType field.
	 */
	public void setPrefferedType(final Class<?> prefferedType) {
		this.prefferedType = prefferedType;
	}

	/**
	 * Sets a new value for the presentationResult field.
	 * 
	 * @param presentationResult
	 *            The new value for the presentationResult field.
	 */
	public void setPresentationResult(final PresentationResultDescriptor<R> presentationResult) {
		this.presentationResult = presentationResult;
	}

	/** {@inheritDoc} */
	@Override
	public void setReadOnly(final boolean newStatus) {
		// Empty.
	}

	/**
	 * Sets a new value for the relationDescriptor field.
	 * 
	 * @param relationDescriptor
	 *            The new value for the relationDescriptor field.
	 */
	public void setRelationDescriptor(final RelationDescriptor relationDescriptor) {
		this.relationDescriptor = relationDescriptor;
	}

	/**
	 * Sets a new value for the resultDescriptor field.
	 * 
	 * @param resultDescriptor
	 *            The new value for the resultDescriptor field.
	 */
	public void setResultDescriptor(final ResultDescriptor<R> resultDescriptor) {
		this.resultDescriptor = resultDescriptor;
	}

	/** {@inheritDoc} */
	@Override
	public void setStringValue(final String value) {
		throw new ReadOnlyException();
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(final Object newValue) throws ReadOnlyException {
		throw new ReadOnlyException();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
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
	public void validate(final Object value) throws InvalidValueException {
		// Empty.
	}

	/**
	 * Process as presentation entity.
	 * 
	 * @param entityDescriptor
	 *            The entity descriptor.
	 * @return The result.
	 */
	private PropertyDescriptor<?, R> processPresentation(final EntityDescriptor<R> entityDescriptor) {
		PropertyDescriptor<?, R> result = null;
		@SuppressWarnings("unchecked")
		final PresentationEntityDescriptor<R> presentationDescriptor = entityDescriptor.adapt(PresentationEntityDescriptor.class);
		if (!CheckUtil.isNull(presentationDescriptor)) {
			final PropertyDescriptor<?, R> nameDescriptor = presentationDescriptor.getNameProperty();
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
	private PropertyDescriptor<?, R> resolveDisplayProperty() {
		PropertyDescriptor<?, R> result = null;
		if (this.isRelated()) {
			@SuppressWarnings("unchecked")
			final EntityDescriptor<R> entityDescriptor = (EntityDescriptor<R>) this.model.getEntity(this.getRelationName());
			@SuppressWarnings("unchecked")
			final KeyedEntityDescriptor<R, ?> keyedDescriptor = entityDescriptor.adapt(KeyedEntityDescriptor.class);
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
		final PropertyDescriptor<Q, R> propertyDescriptor = (PropertyDescriptor<Q, R>) this.resolveDisplayProperty();
		final ConverterTool tool = this.converterTool.get();
		if (!CheckUtil.isNull(propertyDescriptor)) {
			try {
				try {
					result = tool.convertToString(propertyDescriptor.getPropertyType(), propertyDescriptor.getValue(this.bufferValue));
				} catch (final ConverterException e) {
					ResultValueDescriptorImpl.LOGGER.warn("Could not convert.", e);
				}
				if (CheckUtil.isNull(result)) {
					result = propertyDescriptor.getValue(this.bufferValue).toString();
				}
			} catch (final ValueAccessException e) {
				ResultValueDescriptorImpl.LOGGER.warn("Could not access.", e);
			}
		}
		if (CheckUtil.isNull(result)) {
			result = this.bufferValue.toString();
		}
		return result;
	}
}
