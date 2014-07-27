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

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.vaadin.ui.CustomComponent;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.generation.util.VariableResolver;
import org.lunarray.model.generation.vaadin.render.RenderContext;

/**
 * The abstract base component.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The super type.
 * @param <E>
 *            The entity type.
 */
public abstract class AbstractComponent<S, E extends S>
		extends CustomComponent {

	/** Serial id. */
	private static final long serialVersionUID = -2445831011878758711L;
	/** The entity descriptor. */
	private EntityDescriptor<E> entityDescriptor;
	/** The model. */
	private Model<S> model;
	/** The property list. */
	private Set<String> properties;
	/** The qualifier. */
	private Class<?> qualifier;

	/**
	 * Constructs the form component.
	 * 
	 * @param model
	 *            The model. May not be null.
	 * @param entityKey
	 *            The entity key. May not be null. Must refer to an entity known
	 *            to the model.
	 */
	@SuppressWarnings("unchecked")
	public AbstractComponent(final Model<S> model, final String entityKey) {
		super();
		Validate.notNull(model, "Model may not be null");
		Validate.notNull(entityKey, "Entity key may not be null");
		this.model = model;
		this.entityDescriptor = (EntityDescriptor<E>) this.model.getEntity(entityKey);
		Validate.notNull(this.entityDescriptor, String.format("Entity descriptor refered to by entity key '%s' does not exist.", entityKey));
		this.properties = new HashSet<String>();
	}

	/**
	 * Gets the value for the entityDescriptor field.
	 * 
	 * @return The value for the entityDescriptor field.
	 */
	public final EntityDescriptor<E> getEntityDescriptor() {
		return this.entityDescriptor;
	}

	/**
	 * Gets the value for the model field.
	 * 
	 * @return The value for the model field.
	 */
	public final Model<S> getModel() {
		return this.model;
	}

	/**
	 * Gets the value for the properties field.
	 * 
	 * @return The value for the properties field.
	 */
	public final Set<String> getProperties() {
		return this.properties;
	}

	/**
	 * Gets the value for the qualifier field.
	 * 
	 * @return The value for the qualifier field.
	 */
	public final Class<?> getQualifier() {
		return this.qualifier;
	}

	/**
	 * Sets a new value for the entityDescriptor field.
	 * 
	 * @param entityDescriptor
	 *            The new value for the entityDescriptor field.
	 */
	public final void setEntityDescriptor(final EntityDescriptor<E> entityDescriptor) {
		this.entityDescriptor = entityDescriptor;
	}

	/**
	 * Sets a new value for the model field.
	 * 
	 * @param model
	 *            The new value for the model field.
	 */
	public final void setModel(final Model<S> model) {
		this.model = model;
	}

	/**
	 * Sets a new value for the properties field.
	 * 
	 * @param properties
	 *            The new value for the properties field.
	 */
	public final void setProperties(final Set<String> properties) {
		this.properties = properties;
	}

	/**
	 * Sets a new value for the qualifier field.
	 * 
	 * @param qualifier
	 *            The new value for the qualifier field.
	 */
	public final void setQualifier(final Class<?> qualifier) {
		this.qualifier = qualifier;
	}

	/**
	 * A variable resolver.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	protected final class ComponentVariableResolver
			implements VariableResolver<RenderContext<E>, S, E> {

		/** The default constructor. */
		public ComponentVariableResolver() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public EntityDescriptor<E> getDescriptor(final RenderContext<E> arg0) {
			return AbstractComponent.this.entityDescriptor;
		}

		/** {@inheritDoc} */
		@Override
		public Locale getLocale(final RenderContext<E> arg0) {
			return AbstractComponent.this.getLocale();
		}

		/** {@inheritDoc} */
		@Override
		public Model<S> getModel(final RenderContext<E> arg0) {
			return AbstractComponent.this.getModel();
		}

		/** {@inheritDoc} */
		@Override
		public Class<?> getQualifier(final RenderContext<E> arg0) {
			return AbstractComponent.this.getQualifier();
		}

		/** {@inheritDoc} */
		@Override
		public boolean hasQualifier(final RenderContext<E> arg0) {
			return AbstractComponent.this.getQualifier() != null;
		}
	}

}
