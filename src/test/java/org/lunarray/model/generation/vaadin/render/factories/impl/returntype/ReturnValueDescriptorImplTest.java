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
package org.lunarray.model.generation.vaadin.render.factories.impl.returntype;

import com.vaadin.data.Property.ReadOnlyException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunarray.common.event.EventException;
import org.lunarray.model.descriptor.builder.annotation.presentation.builder.PresQualBuilder;
import org.lunarray.model.descriptor.builder.annotation.simple.SimpleBuilder;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.converter.def.DefaultConverterTool;
import org.lunarray.model.descriptor.dictionary.enumeration.EnumDictionary;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.operation.OperationDescriptor;
import org.lunarray.model.descriptor.model.operation.result.ResultDescriptor;
import org.lunarray.model.descriptor.resource.simpleresource.SimpleClazzResource;
import org.lunarray.model.descriptor.validator.beanvalidation.BeanValidationValidator;
import org.lunarray.model.generation.vaadin.model.Sample01;
import org.lunarray.model.generation.vaadin.model.Sample02;
import org.lunarray.model.generation.vaadin.model.SampleEnum;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.vaadin.render.factories.form.descriptor.MutateBuffer;
import org.lunarray.model.generation.vaadin.render.factories.form.events.OperationInvocationEvent;
import org.lunarray.model.generation.vaadin.render.factories.form.impl.result.ResultValueDescriptorImpl;

/**
 * Tests the abstract parameter descriptor.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class ReturnValueDescriptorImplTest {
	/** The presentation model. */
	private Model<Object> presentationModel;
	/** The simple model. */
	private Model<Object> simpleModel;

	/** Sets up the test. */
	@Before
	public void setup() throws Exception {
		@SuppressWarnings("unchecked")
		final SimpleClazzResource<Object> resource = new SimpleClazzResource<Object>(Sample01.class, Sample02.class, SampleEnum.class);
		final BeanValidationValidator validator = new BeanValidationValidator();
		final EnumDictionary dictionary = new EnumDictionary(null);
		final ConverterTool converter = new DefaultConverterTool();
		this.presentationModel = PresQualBuilder.createBuilder().resources(resource).extensions(validator, dictionary, converter).build();
		this.simpleModel = SimpleBuilder.createBuilder().resources(resource).extensions(validator, dictionary, converter).build();
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#getLabel()
	 */
	@Test
	public void testGetLabelPresentation() {
		Assert.assertEquals("", this.getPresentationIntegerDescriptor().getLabel());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#getLabel()
	 */
	@Test
	public void testGetLabelSimple() {
		Assert.assertEquals("", this.getSimpleIntegerDescriptor().getLabel());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getName()
	 */
	@Test
	public void testGetNamePresentation() {
		Assert.assertEquals("echoMethodInt", this.getPresentationIntegerDescriptor().getName());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getName()
	 */
	@Test
	public void testGetNameSimple() {
		Assert.assertEquals("echoMethodInt", this.getSimpleIntegerDescriptor().getName());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#isRequired()
	 */
	@Test
	public void testGetReadOnlyPresentation() {
		Assert.assertTrue(this.getPresentationIntegerDescriptor().isReadOnly());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#isRequired()
	 */
	@Test
	public void testGetReadOnlySimple() {
		Assert.assertTrue(this.getSimpleIntegerDescriptor().isReadOnly());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getRelationName()
	 */
	@Test
	public void testGetRelationNamePresentation() {
		Assert.assertEquals("Sample02", this.getPresentationRelationDescriptor().getRelationName());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getRelationName()
	 */
	@Test
	public void testGetRelationNameSimple() {
		Assert.assertEquals("Sample02", this.getSimpleRelationDescriptor().getRelationName());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getRelationName()
	 */
	@Test
	public void testGetRelationNameUnmappedPresentation() {
		Assert.assertNull(this.getPresentationIntegerDescriptor().getRelationName());
	}

	/**
	 * Test getting the name.
	 * 
	 * @see Descriptor#getRelationName()
	 */
	@Test
	public void testGetRelationNameUnmappedSimple() {
		Assert.assertNull(this.getSimpleIntegerDescriptor().getRelationName());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#isRequired()
	 */
	@Test
	public void testGetRequiredPresentation() {
		Assert.assertTrue(!this.getPresentationIntegerDescriptor().isRequired());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#isRequired()
	 */
	@Test
	public void testGetRequiredSimple() {
		Assert.assertTrue(!this.getSimpleIntegerDescriptor().isRequired());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#isValid(Object)
	 */
	@Test
	public void testIsValidPresentation() {
		Assert.assertTrue(this.getPresentationIntegerDescriptor().isValid(null));
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#isValid(Object)
	 */
	@Test
	public void testIsValidSimple() {
		Assert.assertTrue(this.getSimpleIntegerDescriptor().isValid(null));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setCoerceValue(Object)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetDirectValueEntityCoerceCopyPresentation() {
		this.getPresentationIntegerDescriptor().getBufferMutator().setCoerceValue(Integer.valueOf(100));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setCoerceValue(Object)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetDirectValueEntityCoerceCopySimple() {
		this.getSimpleIntegerDescriptor().getBufferMutator().setCoerceValue(Integer.valueOf(100));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setCoerceValue(Object)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetDirectValueEntityCoercePresentation() {
		this.getPresentationIntegerDescriptor().getBufferMutator().setCoerceValue(Long.valueOf(100L));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setCoerceValue(Object)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetDirectValueEntityCoerceSimple() {
		this.getSimpleIntegerDescriptor().getBufferMutator().setCoerceValue(Long.valueOf(100L));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setDirectValue(Object)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetDirectValueEntityNullPresentation() {
		this.getPresentationIntegerDescriptor().getBufferMutator().setDirectValue(null);
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setDirectValue(Object)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetDirectValueEntityNullSimple() {
		this.getSimpleIntegerDescriptor().getBufferMutator().setDirectValue(null);
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setDirectValue(Object)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetDirectValueEntityPresentation() {
		this.getPresentationIntegerDescriptor().getBufferMutator().setDirectValue(Integer.valueOf(100));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setDirectValue(Object)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetDirectValueEntitySimple() {
		this.getSimpleIntegerDescriptor().getBufferMutator().setDirectValue(Integer.valueOf(100));
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(String)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetDirectValueStringCopyPresentation() {
		this.getPresentationStringDescriptor().getBufferMutator().setStringValue("100");
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(String)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetDirectValueStringCopySimple() {
		this.getSimpleStringDescriptor().getBufferMutator().setStringValue("100");
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(String)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetInvalidValueStringPresentation() {
		this.getPresentationIntegerDescriptor().getBufferMutator().setStringValue("test");
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(String)
	 */
	@Test(expected = ReadOnlyException.class)
	public void testSetInvalidValueStringSimple() {
		this.getSimpleIntegerDescriptor().getBufferMutator().setStringValue("test");
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(String)
	 */
	@Test
	public void testSetValueStringPresentation() {
		final Descriptor<Integer> descriptor = this.getPresentationIntegerDescriptor();
		Assert.assertEquals("5", descriptor.getBufferAccessor().getStringValue());
		Assert.assertEquals(Long.valueOf(5), descriptor.getBufferAccessor().getCoerceValue(Long.class));
		Assert.assertEquals(Integer.valueOf(5), descriptor.getBufferAccessor().getDirectValue());
	}

	/**
	 * Test setting a value.
	 * 
	 * @see MutateBuffer#setStringValue(String)
	 */
	@Test
	public void testSetValueStringSimple() {
		final Descriptor<Integer> descriptor = this.getSimpleIntegerDescriptor();
		Assert.assertEquals("5", descriptor.getBufferAccessor().getStringValue());
		Assert.assertEquals(Long.valueOf(5), descriptor.getBufferAccessor().getCoerceValue(Long.class));
		Assert.assertEquals(Integer.valueOf(5), descriptor.getBufferAccessor().getDirectValue());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Object#toString()
	 */
	@Test
	public void testToStringIntegerPresentation() {
		Assert.assertEquals("5", this.getPresentationIntegerDescriptor().toString());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Object#toString()
	 */
	@Test
	public void testToStringIntegerSimple() {
		Assert.assertEquals("5", this.getSimpleIntegerDescriptor().toString());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Object#toString()
	 */
	@Test
	public void testToStringRelationPresentation() {
		Assert.assertEquals("id 1", this.getPresentationRelationDescriptor().toString());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Object#toString()
	 */
	@Test
	public void testToStringRelationSimple() {
		Assert.assertEquals("id 1", this.getSimpleRelationDescriptor().toString());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Object#toString()
	 */
	@Test
	public void testToStringStringPresentation() {
		Assert.assertEquals("", this.getPresentationStringDescriptor().toString());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Object#toString()
	 */
	@Test
	public void testToStringStringSimple() {
		Assert.assertEquals("", this.getSimpleStringDescriptor().toString());
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#validate(Object)
	 */
	@Test
	public void testValidatePresentation() {
		this.getPresentationIntegerDescriptor().validate(null);
	}

	/**
	 * Test getting the label.
	 * 
	 * @see Descriptor#validate(Object)
	 */
	@Test
	public void testValidateSimple() {
		this.getSimpleIntegerDescriptor().validate(null);
	}

	/** Get a presentation descriptor. */
	protected Descriptor<Integer> getPresentationIntegerDescriptor() {
		final EntityDescriptor<Sample01> entity = this.presentationModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethodInt");
		final ResultDescriptor<Integer> resultDescriptor = operation.getResultDescriptor(Integer.TYPE);
		final ResultValueDescriptorImpl<Integer> result = new ResultValueDescriptorImpl<Integer>(resultDescriptor, operation,
				this.presentationModel);
		try {
			result.handleEvent(new OperationInvocationEvent<Sample01, Integer>(operation, Integer.valueOf(5)));
		} catch (final EventException e) {
			Assert.fail();
		}
		return result;
	}

	/** Get a presentation descriptor. */
	protected Descriptor<Sample02> getPresentationRelationDescriptor() {
		final EntityDescriptor<Sample01> entity = this.presentationModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethodMapped");
		final ResultDescriptor<Sample02> resultDescriptor = operation.getResultDescriptor(Sample02.class);
		final ResultValueDescriptorImpl<Sample02> result = new ResultValueDescriptorImpl<Sample02>(resultDescriptor, operation,
				this.simpleModel);
		try {
			result.handleEvent(new OperationInvocationEvent<Sample01, Sample02>(operation, Sample02.SAMPLE_01));
		} catch (final EventException e) {
			Assert.fail();
		}
		return result;
	}

	/** Get a presentation descriptor. */
	protected Descriptor<String> getPresentationStringDescriptor() {
		final EntityDescriptor<Sample01> entity = this.presentationModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethod");
		final ResultDescriptor<String> resultDescriptor = operation.getResultDescriptor(String.class);
		return new ResultValueDescriptorImpl<String>(resultDescriptor, operation, this.presentationModel);
	}

	/** Get a simple descriptor. */
	protected Descriptor<Integer> getSimpleIntegerDescriptor() {
		final EntityDescriptor<Sample01> entity = this.simpleModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethodInt");
		final ResultDescriptor<Integer> resultDescriptor = operation.getResultDescriptor(Integer.TYPE);
		final ResultValueDescriptorImpl<Integer> result = new ResultValueDescriptorImpl<Integer>(resultDescriptor, operation,
				this.simpleModel);
		try {
			result.handleEvent(new OperationInvocationEvent<Sample01, Integer>(operation, Integer.valueOf(5)));
		} catch (final EventException e) {
			Assert.fail();
		}
		return result;
	}

	/** Get a simple descriptor. */
	protected Descriptor<Sample02> getSimpleRelationDescriptor() {
		final EntityDescriptor<Sample01> entity = this.simpleModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethodMapped");
		final ResultDescriptor<Sample02> resultDescriptor = operation.getResultDescriptor(Sample02.class);
		final ResultValueDescriptorImpl<Sample02> result = new ResultValueDescriptorImpl<Sample02>(resultDescriptor, operation,
				this.simpleModel);
		try {
			result.handleEvent(new OperationInvocationEvent<Sample01, Sample02>(operation, Sample02.SAMPLE_01));
		} catch (final EventException e) {
			Assert.fail();
		}
		return result;
	}

	/** Get a simple descriptor. */
	protected Descriptor<String> getSimpleStringDescriptor() {
		final EntityDescriptor<Sample01> entity = this.simpleModel.getEntity(Sample01.class);
		final OperationDescriptor<Sample01> operation = entity.getOperation("echoMethod");
		final ResultDescriptor<String> resultDescriptor = operation.getResultDescriptor(String.class);
		return new ResultValueDescriptorImpl<String>(resultDescriptor, operation, this.simpleModel);
	}
}
