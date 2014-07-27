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
package org.lunarray.model.generation.vaadin.render.factories.form.vaadin.components;

import java.util.Collection;

import com.vaadin.data.Item;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.dictionary.Dictionary;
import org.lunarray.model.descriptor.dictionary.exceptions.DictionaryException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationEntityDescriptor;
import org.lunarray.model.generation.vaadin.render.RenderContext;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.Descriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract select field.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public abstract class AbstractSelectFieldPropertyStrategy<P>
		extends AbstractFormPropertyRenderStrategy<P> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSelectFieldPropertyStrategy.class);
	/** Serial id. */
	private static final long serialVersionUID = -2834618819595436581L;

	/**
	 * Constructs the strategy.
	 * 
	 * @param descriptor
	 *            The property descriptor. May not be null.
	 * @param context
	 *            The render context. May not be null.
	 * @param prefferedType
	 *            The preferred type. May not be null.
	 */
	public AbstractSelectFieldPropertyStrategy(final Descriptor<P> descriptor, final RenderContext<?> context, final Class<?> prefferedType) {
		super(descriptor, context, prefferedType);
		if (descriptor.isRelated()) {
			descriptor.setPreferredType(context.getModel().getEntity(descriptor.getRelationName()).getEntityType());
		}
	}

	/**
	 * Lookup all entities for this property.
	 * 
	 * @return The entities.
	 */
	private Collection<P> lookupEntities() {
		final Model<?> model = this.getModel();
		final Dictionary dictionary = model.getExtension(Dictionary.class);
		Collection<P> result = null;
		if (this.getDescriptor().isRelated() && !CheckUtil.isNull(dictionary)) {
			@SuppressWarnings("unchecked")
			final EntityDescriptor<P> entityDescriptor = (EntityDescriptor<P>) model.getEntity(this.getDescriptor().getRelationName());
			try {
				result = dictionary.lookup(entityDescriptor);
			} catch (final DictionaryException e) {
				AbstractSelectFieldPropertyStrategy.LOGGER.warn("Could not look up in dictionary '{}'.", this.getDescriptor()
						.getRelationName(), e);
			}
		}
		AbstractSelectFieldPropertyStrategy.LOGGER.debug("Found entities: {}", result);
		return result;
	}

	/**
	 * Process an entity.
	 * 
	 * @param select
	 *            The select component.
	 * @param displayProperty
	 *            The display property.
	 * @param entity
	 *            The entity.
	 */
	private void processEntity(final AbstractSelect select, final PropertyDescriptor<Object, P> displayProperty, final P entity) {
		final Item item = select.addItem(entity);
		if (CheckUtil.isNull(item)) {
			AbstractSelectFieldPropertyStrategy.LOGGER.warn("Tried to add existing item null.");
		}
		if (!CheckUtil.isNull(displayProperty)) {
			displayProperty.getName();
			Object displayValue;
			try {
				displayValue = displayProperty.getValue(entity);
			} catch (final ValueAccessException e) {
				AbstractSelectFieldPropertyStrategy.LOGGER.warn("Could not access value.", e);
				displayValue = null;
			}
			if (!CheckUtil.isNull(displayValue)) {
				select.setItemCaption(entity, displayValue.toString());
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
		final Model<?> model = this.getModel();
		PropertyDescriptor<?, P> result = null;
		if (this.getDescriptor().isRelated()) {
			@SuppressWarnings("unchecked")
			final EntityDescriptor<P> entityDescriptor = (EntityDescriptor<P>) model.getEntity(this.getDescriptor().getRelationName());
			@SuppressWarnings("unchecked")
			final KeyedEntityDescriptor<P, ?> keyedDescriptor = entityDescriptor.adapt(KeyedEntityDescriptor.class);
			result = this.processPresentation(entityDescriptor);
			if (CheckUtil.isNull(result) && !CheckUtil.isNull(keyedDescriptor)) {
				result = keyedDescriptor.getKeyProperty();
			}
		}
		AbstractSelectFieldPropertyStrategy.LOGGER.debug("Resolved display property {} for {}", result, this.getDescriptor());
		return result;
	}

	/** {@inheritDoc} */
	@Override
	protected final Component createComponent() {
		final Component component = this.createSelectComponent();
		if (component instanceof AbstractSelect) {
			final AbstractSelect select = (AbstractSelect) component;
			@SuppressWarnings("unchecked")
			final PropertyDescriptor<Object, P> displayProperty = (PropertyDescriptor<Object, P>) this.resolveDisplayProperty();
			if (!CheckUtil.isNull(displayProperty)) {
				final String displayName = displayProperty.getName();
				select.addContainerProperty(displayName, displayProperty.getPropertyType(), displayName);
			}
			final Collection<P> entities = this.lookupEntities();
			for (final P entity : entities) {
				this.processEntity(select, displayProperty, entity);
			}
		}
		return component;
	}

	/**
	 * Create the select component.
	 * 
	 * @return The component.
	 */
	protected abstract Component createSelectComponent();
}
