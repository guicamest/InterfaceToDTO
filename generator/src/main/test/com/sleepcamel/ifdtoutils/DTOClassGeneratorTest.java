package com.sleepcamel.ifdtoutils;

import org.junit.Assert;
import org.junit.Test;


public class DTOClassGeneratorTest {

	@Test
	public void testGetDTOName(){
		Assert.assertEquals("java.lang.StringDTO", DTOClassGenerator.getDTOName(String.class, ""));
		
		Assert.assertEquals("java.lang.dtos.StringDTO", DTOClassGenerator.getDTOName(String.class, "dtos"));
		
		Assert.assertEquals("java.lang.dtos.StringDTO", DTOClassGenerator.getDTOName(String.class, ".dtos"));
	}
	
}
