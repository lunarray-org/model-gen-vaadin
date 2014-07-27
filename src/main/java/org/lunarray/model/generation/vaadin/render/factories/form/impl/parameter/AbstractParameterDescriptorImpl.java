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
package org.lunarray.model.generation.vaadin.render.factories.form.impl.parameter;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.converter.exceptions.ConverterException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.extension.ExtensionRef;
import org.lunarray.model.descriptor.model.member.Cardinality;
import org.lunarray.model.descriptor.model.operation.parameters.ParameterDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationParameterDescriptor;
import org.lunarray.model.descriptor.util.OperationInvocationBuilder;
import org.lunarray.model.descriptor.validator.ValueValidator;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.AccessBuffer;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.MutateBuffer;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.ValueChangedListener;
import org.lunarray.model.generation.vaadin.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract parameter descriptor.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The parameter type.
 * @param <E>
 *            The entity type.
 */
public abstract class AbstractParameterDescriptorImpl<P, E>
		implements Descriptor<P>, AccessBuffer<P>, MutateBuffer<P> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractParameterDescriptorImpl.class);
	/** Required message. */
	private static final String REQUIRED_MESSAGE = "validation.value.required";
	/** The serial id. */
	private static final long serialVersionUID = 3384550082453081949L;
	/** The buffer value. */
	private P bufferValue;
	/** The builder. */
	private OperationInvocationBuilder<E> builder;
	/** The listeners. */
	private List<ValueChangeListener> changeListeners;
	/** The converter tool. */
	private ExtensionRef<ConverterTool> converterTool;
	/** The listeners. */
	private List<ValueChangedListener<P>> listeners;
	/** The parameter. */
	private ParameterDescriptor<P> parameter;
	/** The preferred type. */
	private Class<?> prefferedType;
	/** The presentation descriptor. */
	private PresentationParameterDescriptor<P> presentationParameter;
	/** A relation descriptor. */
	private RelationDescriptor relationDescriptor;
	/** The value. */
	private ExtensionRef<ValueValidator> valueValidator;

	/**
	 * Default constructor.
	 * 
	 * @param parameter
	 *            The parameter.
	 * @param builder
	 *            The builder.
	 * @param model
	 *            The model.
	 */
	@SuppressWarnings("unchecked")
	public AbstractParameterDescriptorImpl(final ParameterDescriptor<P> parameter, final OperationInvocationBuilder<E> builder,
			final Model<? super E> model) {
		Validate.notNull(parameter, "Parameter may not be null.");
		Validate.notNull(model, "Model may not be null.");
		Validate.notNull(builder, "Operation Execution Builder may not be null.");
		this.parameter = parameter;
		this.builder = builder;
		this.presentationParameter = parameter.adapt(PresentationParameterDescriptor.class);
		this.relationDescriptor = parameter.adapt(RelationDescriptor.class);
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
	 * Gets the value for the bufferValue field.
	 * 
	 * @return The value for the bufferValue field.
	 */
	public final P getBufferValue() {
		return this.bufferValue;
	}

	/**
	 * Gets the value for the builder field.
	 * 
	 * @return The value for the builder field.
	 */
	public final OperationInvocationBuilder<E> getBuilder() {
		return this.builder;
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
	@Override
	public final <T> T getCoerceValue(final Class<T> valueType) {
		Validate.notNull(valueType, "Coerce type may not be null.");
		final ConverterTool tool = this.getConverterTool().get();
		T result = null;
		AbstractParameterDescriptorImpl.LOGGER.debug("Coercing value {} to {}.", this.bufferValue, valueType);
		if (valueType.isAssignableFrom(this.getType())) {
			result = valueType.cast(this.bufferValue);
		} else {
			String format = null;
			if (!CheckUtil.isNull(this.presentationParameter)) {
				format = this.presentationParameter.getFormat();
			}
			try {
				final String stringValue = tool.convertToString(this.parameter.getType(), this.bufferValue, format);
				result = tool.convertToInstance(valueType, stringValue, format);
			} catch (final ConverterException e) {
				throw new ConversionException(e.getMessage(), e);
			}
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
		return this.bufferValue;
	}

	/** {@inheritDoc} */
	@Override
	public final String getLabel() {
		String labelText;
		if (CheckUtil.isNull(this.presentationParameter)) {
			labelText = Integer.toString(this.parameter.getIndex());
		} else {
			labelText = this.presentationParameter.getDescription();
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

	/** {@inheritDoc} */
	@Override
	public final String getName() {
		return Integer.toString(this.parameter.getIndex());
	}

	/**
	 * Gets the value for the parameter field.
	 * 
	 * @return The value for the parameter field.
	 */
	public final ParameterDescriptor<P> getParameter() {
		return this.parameter;
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
	 * Gets the value for the presentationParameter field.
	 * 
	 * @return The value for the presentationParameter field.
	 */
	public final PresentationParameterDescriptor<P> getPresentationParameter() {
		return this.presentationParameter;
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
		if (!CheckUtil.isNull(this.presentationParameter)) {
			format = this.presentationParameter.getFormat();
		}
		try {
			result = tool.convertToString(this.parameter.getType(), this.bufferValue, format);
		} catch (final ConverterException e) {
			throw new ConversionException(e.getMessage(), e);
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final Class<?> getType() {
		return this.parameter.getType();
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
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public final boolean isRelated() {
		return !CheckUtil.isNull(this.relationDescriptor);
	}

	/** {@inheritDoc} */
	@Override
	public final boolean isRequired() {
		boolean result;
		if (CheckUtil.isNull(this.presentationParameter)) {
			result = this.parameter.getCardinality() == Cardinality.SINGLE;
		} else {
			result = this.presentationParameter.isRequiredIndication();
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final boolean isValid(final Object value) {
		boolean result = true;
		if (!CheckUtil.isNull(this.presentationParameter) && this.presentationParameter.isRequiredIndication()) {
			result ^= CheckUtil.isNull(value);
		}
		return result;
	}

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
	 * Sets a new value for the bufferValue field.
	 * 
	 * @param bufferValue
	 *            The new value for the bufferValue field.
	 */
	public final void setBufferValue(final P bufferValue) {
		this.bufferValue = bufferValue;
	}

	/**
	 * Sets a new value for the builder field.
	 * 
	 * @param builder
	 *            The new value for the builder field.
	 */
	public final void setBuilder(final OperationInvocationBuilder<E> builder) {
		this.builder = builder;
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
		if (!CheckUtil.isNull(this.presentationParameter)) {
			format = this.presentationParameter.getFormat();
		}
		AbstractParameterDescriptorImpl.LOGGER.debug("Coercing value {} with format {}.", value, format);
		try {
			if (this.parameter.isAssignable(value)) {
				this.bufferValue = (P) value;
			} else {
				final String stringValue = tool.convertToString((Class<T>) value.getClass(), value, format);
				this.bufferValue = tool.convertToInstance(this.parameter.getType(), stringValue, format);
			}
		} catch (final ConverterException e) {
			throw new ConversionException(e.getMessage(), e);
		}
		this.builder.parameter(this.parameter, this.bufferValue);
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
		this.bufferValue = value;
		this.builder.parameter(this.parameter, this.bufferValue);
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
	 * Sets a new value for the parameter field.
	 * 
	 * @param parameter
	 *            The new value for the parameter field.
	 */
	public final void setParameter(final ParameterDescriptor<P> parameter) {
		this.parameter = parameter;
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
	 * Sets a new value for the presentationParameter field.
	 * 
	 * @param presentationParameter
	 *            The new value for the presentationParameter field.
	 */
	public final void setPresentationParameter(final PresentationParameterDescriptor<P> presentationParameter) {
		this.presentationParameter = presentationParameter;
	}

	/** {@inheritDoc} */
	@Override
	public final void setReadOnly(final boolean newStatus) {
		AbstractParameterDescriptorImpl.LOGGER.warn("Could not set to read only.");
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
		if (!CheckUtil.isNull(this.presentationParameter)) {
			format = this.presentationParameter.getFormat();
		}
		try {
			if (this.parameter.isAssignable(stringValue)) {
				this.bufferValue = (P) stringValue;
			} else {
				this.bufferValue = tool.convertToInstance(this.parameter.getType(), stringValue, format);
			}
		} catch (final ConverterException e) {
			throw new ConversionException(e.getMessage(), e);
		}
		this.builder.parameter(this.parameter, this.bufferValue);
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
		String result = this.getStringValue();
		if (CheckUtil.isNull(result)) {
			result = "";
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final void validate(final Object value) throws InvalidValueException {
		if (!CheckUtil.isNull(this.presentationParameter) && this.presentationParameter.isRequiredIndication() && CheckUtil.isNull(value)) {
			throw new InvalidValueException(MessageUtil.getMessage(AbstractParameterDescriptorImpl.REQUIRED_MESSAGE));
		}
	}
}
