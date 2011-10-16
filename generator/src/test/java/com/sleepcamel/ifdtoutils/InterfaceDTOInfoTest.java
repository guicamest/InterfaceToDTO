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
	public void testInterfaceWithPackageSuffix(){
		InterfaceDTOInfo<InterfaceWithPackageSuffix> info = InterfaceDTOInfo.getInfo(InterfaceWithPackageSuffix.class);
		Assert.assertEquals(info.getDTOCanonicalName(), "com.sleepcamel.ifdtoutils.dtoPackage.InterfaceWithPackageSuffixDTO");
	}
	
	@Test
	public void testInterfaceWithFullPackage(){
		InterfaceDTOInfo<InterfaceWithFullPackage> info = InterfaceDTOInfo.getInfo(InterfaceWithFullPackage.class);
		Assert.assertEquals(info.getDTOCanonicalName(), InterfaceWithFullPackage.class.getAnnotation(ToDTO.class).fullPackage()+".InterfaceWithFullPackageDTO");
	}
	
	@Test
	public void testNormalizeSubpackage(){
		InterfaceDTOInfo<InterfaceWithPackageSuffix> info = InterfaceDTOInfo.getInfo(InterfaceWithPackageSuffix.class);
		Assert.assertEquals(info.normalizePackage(".lala"),"lala");
		Assert.assertEquals(info.normalizePackage("aswd"),"aswd");
		Assert.assertEquals(info.normalizePackage("jkfr."),"jkfr");
		Assert.assertEquals(info.normalizePackage(".pofe."),"pofe");
	}
}
