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
package org.lunarray.model.generation.vaadin;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.ui.Window;

import org.lunarray.model.descriptor.builder.annotation.presentation.builder.PresQualBuilder;
import org.lunarray.model.descriptor.converter.def.DefaultConverterTool;
import org.lunarray.model.descriptor.dictionary.composite.EntityDictionary;
import org.lunarray.model.descriptor.dictionary.composite.simple.CompositeDictionary;
import org.lunarray.model.descriptor.dictionary.enumeration.EnumDictionary;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.resource.Resource;
import org.lunarray.model.descriptor.resource.ResourceException;
import org.lunarray.model.descriptor.resource.simpleresource.SimpleClazzResource;
import org.lunarray.model.descriptor.validator.beanvalidation.BeanValidationValidator;
import org.lunarray.model.generation.vaadin.components.impl.FormBuilder;
import org.lunarray.model.generation.vaadin.components.impl.TableBuilder;
import org.lunarray.model.generation.vaadin.model.Sample01;
import org.lunarray.model.generation.vaadin.model.Sample02;
import org.lunarray.model.generation.vaadin.model.SampleEnum;

/**
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class TestApplication
		extends Application {

	/** Serial id. */
	private static final long serialVersionUID = -3166774189735402221L;
	private Sample01 instance;
	private Model<Object> model;

	/** {@inheritDoc} */
	@Override
	public void init() {
		@SuppressWarnings("unchecked")
		final Resource<Class<? extends Object>> resource = new SimpleClazzResource<Object>(Sample01.class, Sample02.class, SampleEnum.class);
		final List<EntityDictionary<?, ?>> dictionaries = new LinkedList<EntityDictionary<?, ?>>();
		dictionaries.add(new Sample02Dictionary());
		final BeanValidationValidator validator = new BeanValidationValidator();
		try {
			this.model = PresQualBuilder.createBuilder()
					.extensions(new EnumDictionary(new CompositeDictionary(dictionaries)), validator, new DefaultConverterTool())
					.resources(resource).build();
			this.instance = new Sample01();
			final Window w = new Window();
			w.addComponent(FormBuilder.createBuilder().model(this.model).entityKey("Sample01").entity(this.instance).build());
			w.addComponent(TableBuilder.<Object, Sample01>createBuilder().model(this.model).entityKey("Sample01").entity(Sample01.DATA)
					.build());
			this.setMainWindow(w);
		} catch (final ResourceException e) {
			e.printStackTrace();
		}
	}
}
