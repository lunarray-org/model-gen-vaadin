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

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.validator.EntityValidator;
import org.lunarray.model.generation.util.Composer;
import org.lunarray.model.generation.vaadin.components.FormComponent;
import org.lunarray.model.generation.vaadin.render.RenderContext;
import org.lunarray.model.generation.vaadin.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.FormPropertyRenderStrategyFactoryImpl;
import org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components.OperationOutputStrategy;
import org.lunarray.model.generation.vaadin.util.MessageUtil;

/**
 * The form implementation.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The super type.
 * @param <E>
 *            The entity type.
 */
public final class FormComponentImpl<S, E extends S>
		extends AbstractComponent<S, E>
		implements FormComponent<S, E> {

	/** The bundle button cancel key. */
	private static final String BUTTON_CANCEL = "form.button.cancel";
	/** The bundle button submit key. */
	private static final String BUTTON_SUBMIT = "form.button.submit";
	/** Serial id. */
	private static final long serialVersionUID = -2445831011878758711L;
	/** Validation message. */
	private static final String STRATEGY_NULL = "Strategy may not be null.";
	/** The cancel button. */
	private Button cancelButton;
	/** The entity. */
	private E entity;
	/** The form. */
	private Form form;
	/** The subform. */
	private Form subForm;
	/** The submit button. */
	private Button submitButton;

	/**
	 * Constructs the form component.
	 * 
	 * @param model
	 *            The model.
	 * @param entityKey
	 *            The entity key.
	 * @param entity
	 *            The entity.
	 */
	protected FormComponentImpl(final Model<S> model, final String entityKey, final E entity) {
		super(model, entityKey);
		this.entity = entity;
		this.init();
	}

	/** {@inheritDoc} */
	@Override
	public void addCancelListener(final ClickListener listener) {
		this.cancelButton.addListener(listener);
	}

	/** {@inheritDoc} */
	@Override
	public void addSubmitListener(final ClickListener listener) {
		this.submitButton.addListener(listener);
	}

	/**
	 * Gets the value for the cancelButton field.
	 * 
	 * @return The value for the cancelButton field.
	 */
	public Button getCancelButton() {
		return this.cancelButton;
	}

	/**
	 * Gets the value for the entity field.
	 * 
	 * @return The value for the entity field.
	 */
	@Override
	public E getEntity() {
		return this.entity;
	}

	/**
	 * Gets the value for the form field.
	 * 
	 * @return The value for the form field.
	 */
	public Form getForm() {
		return this.form;
	}

	/**
	 * Gets the value for the subForm field.
	 * 
	 * @return The value for the subForm field.
	 */
	public Form getSubForm() {
		return this.subForm;
	}

	/**
	 * Gets the value for the submitButton field.
	 * 
	 * @return The value for the submitButton field.
	 */
	public Button getSubmitButton() {
		return this.submitButton;
	}

	/** {@inheritDoc} */
	@Override
	public void processBeginStrategy(final OperationOutputStrategy<E> strategy) {
		Validate.notNull(strategy, FormComponentImpl.STRATEGY_NULL);
		this.subForm = new Form();
		this.subForm.getLayout().addComponent(strategy.getLabel());
		this.form.addField(strategy.getDescriptor().getName(), this.subForm);
	}

	/** {@inheritDoc} */
	@Override
	public void processEndStrategy(final OperationOutputStrategy<E> strategy) {
		Validate.notNull(strategy, FormComponentImpl.STRATEGY_NULL);
		this.subForm.addField(strategy.getDescriptor().getName(), strategy.getButton());
		this.subForm = this.form;
	}

	/** {@inheritDoc} */
	@Override
	public void processStrategy(final FormPropertyRenderStrategy<?> strategy) {
		Validate.notNull(strategy, FormComponentImpl.STRATEGY_NULL);
		final Component component = strategy.getComponent();
		final String propertyName = strategy.getPropertyName();
		if (component instanceof Field) {
			final Field field = (Field) component;
			this.subForm.addField(propertyName, field);
			this.getProperties().add(propertyName);
		} else {
			this.subForm.getLayout().addComponent(strategy.getComponent());
		}
	}

	/** {@inheritDoc} */
	@Override
	public void removeCancelListener(final ClickListener listener) {
		this.cancelButton.removeListener(listener);
	}

	/** {@inheritDoc} */
	@Override
	public void removeSubmitListener(final ClickListener listener) {
		this.submitButton.removeListener(listener);
	}

	/**
	 * Sets a new value for the cancelButton field.
	 * 
	 * @param cancelButton
	 *            The new value for the cancelButton field.
	 */
	public void setCancelButton(final Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	/**
	 * Sets a new value for the entity field.
	 * 
	 * @param entity
	 *            The new value for the entity field.
	 */
	public void setEntity(final E entity) {
		this.entity = entity;
		this.init();
	}

	/**
	 * Sets a new value for the form field.
	 * 
	 * @param form
	 *            The new value for the form field.
	 */
	public void setForm(final Form form) {
		this.form = form;
	}

	/**
	 * Sets a new value for the subForm field.
	 * 
	 * @param subForm
	 *            The new value for the subForm field.
	 */
	public void setSubForm(final Form subForm) {
		this.subForm = subForm;
	}

	/**
	 * Sets a new value for the submitButton field.
	 * 
	 * @param submitButton
	 *            The new value for the submitButton field.
	 */
	public void setSubmitButton(final Button submitButton) {
		this.submitButton = submitButton;
	}

	/** Initializes the form. */
	private void init() {
		this.form = new Form(new FormLayout());
		final Composer<RenderContext<E>, S, E> composer = new Composer<RenderContext<E>, S, E>();
		final Model<S> model = this.getModel();
		composer.setContext(new RenderContext<E>(model));
		composer.setPropertyRenderStrategyFactory(new FormPropertyRenderStrategyFactoryImpl<E>(this));
		composer.setVariableResolver(new ComponentVariableResolver());
		this.form.setCaption(composer.getLabel());
		this.setCompositionRoot(this.form);
		this.subForm = this.form;
		composer.compose(true);
		this.form.setFooter(new HorizontalLayout());
		this.submitButton = new Button(MessageUtil.getMessage(FormComponentImpl.BUTTON_SUBMIT), this.form, "commit");
		if (!CheckUtil.isNull(this.getModel().getExtension(EntityValidator.class))) {
			this.submitButton.addListener(new EntityValidationListener<S, E>(this));
		}
		this.form.getFooter().addComponent(this.submitButton);
		this.cancelButton = new Button(MessageUtil.getMessage(FormComponentImpl.BUTTON_CANCEL));
		this.form.getFooter().addComponent(this.cancelButton);
	}
}
