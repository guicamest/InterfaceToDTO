package com.sleepcamel.ifdtoUtils;

import org.junit.Assert;
import org.junit.Test;

public class InterfaceDTOBuilderTest {

	@Test
	public void testNullObject(){
		Circle circle = null;
		Assert.assertNull(InterfaceDTOBuilder.builder(IDrawable.class).dto(circle));
	}
	
	@Test(expected=RuntimeException.class)
	public void testClassIsNull(){
		InterfaceDTOBuilder.builder(null).dto(new Circle(2.4));
	}
	
	@Test(expected=RuntimeException.class)
	public void testClassIsNotInterface(){
		InterfaceDTOBuilder.builder(Circle.class).dto(new Circle(2.4));
	}
	
}