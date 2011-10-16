package com.sleepcamel.ifdtoutils;

import org.junit.Assert;
import org.junit.Test;

public class InterfaceDTOInfoTest {

	@Test
	public void testInterfaceDoesntHaveToDTO(){
		InterfaceDTOInfo<String> info = InterfaceDTOInfo.getInfo(String.class);
		Assert.assertEquals(info.getDTOCanonicalName(), "java.lang.StringDTO");
	}
	
	@Test
	public void testInterfaceHasToDTO(){
		InterfaceDTOInfo<InterfaceWithToDTOAnnotation> info = InterfaceDTOInfo.getInfo(InterfaceWithToDTOAnnotation.class);
		Assert.assertEquals(info.getDTOCanonicalName(), "com.sleepcamel.ifdtoutils.dtoPackage.InterfaceWithToDTOAnnotationDTO");
	}
	
	@Test
	public void testNormalizeSubpackage(){
		InterfaceDTOInfo<InterfaceWithToDTOAnnotation> info = InterfaceDTOInfo.getInfo(InterfaceWithToDTOAnnotation.class);
		Assert.assertEquals(info.normalizePackage(".lala"),"lala");
		Assert.assertEquals(info.normalizePackage("aswd"),"aswd");
		Assert.assertEquals(info.normalizePackage("jkfr."),"jkfr");
		Assert.assertEquals(info.normalizePackage(".pofe."),"pofe");
	}
}
