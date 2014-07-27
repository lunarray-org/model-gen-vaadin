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
package org.lunarray.model.generation.vaadin.render.factories.table.vaadin.components;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.model.generation.vaadin.render.factories.table.TableModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A column generator.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public final class CheckboxColumnGenerator<P, E>
		extends AbstractColumnGenerator<P, E> {
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckboxColumnGenerator.class);
	/** Serial id. */
	private static final long serialVersionUID = -2468802343741871528L;

	/**
	 * Default constructor.
	 * 
	 * @param strategy
	 *            The output strategy. May not be null.
	 */
	public CheckboxColumnGenerator(final CheckboxOutputPropertyStrategy<P, E> strategy) {
		super(strategy);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public Object generateCell(final Table source, final Object itemId, final Object columnId) {
		Validate.notNull(itemId, "Item id may not be null.");
		final OutputPropertyStrategy<P, E> strategy = this.getOutputPropertyStrategy();
		final RelationDescriptor relationProperty = strategy.getProperty().adapt(RelationDescriptor.class);
		Object result = null;
		String format = null;
		if (strategy.hasPresentationProperty()) {
			format = strategy.getPresentationProperty().getFormat();
		}
		if (CheckUtil.isNull(relationProperty)) {
			final CheckBox tmpCheckbox = new CheckBox("", new TableModelProperty<P, E>(strategy.getConverterTool(), this
					.getOutputPropertyStrategy().getProperty(), (E) itemId, format, strategy.getModel()));
			tmpCheckbox.setReadOnly(true);
			result = tmpCheckbox;
		} else {
			final PropertyDescriptor<Object, P> displayProperty = (PropertyDescriptor<Object, P>) this.resolveDisplayProperty();
			try {
				final CheckBox tmpCheckbox = new CheckBox("", new TableModelProperty<Object, P>(strategy.getConverterTool(),
						displayProperty, strategy.getProperty().getValue((E) itemId), format, strategy.getModel()));
				tmpCheckbox.setReadOnly(true);
				result = tmpCheckbox;
			} catch (final ValueAccessException e) {
				CheckboxColumnGenerator.LOGGER.warn("Could not access value.", e);
				result = itemId.toString();
			}
		}
		CheckboxColumnGenerator.LOGGER.debug("Resolved for item {} and column {}: {}", itemId, columnId, result);
		return result;
	}
}
