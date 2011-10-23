package com.sleepcamel.ifdtoutils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class InterfaceDTOInfoTest {

	@Test
	public void testInterfaceDoesntHaveToDTO(){
		InterfaceDTOInfo<String> info = InterfaceDTOInfo.getInfo(String.class);
		assertEquals(info.getDTOCanonicalName(), "java.lang.StringDTO");
	}

	@Test
	public void testInterfaceWithPackageSuffix(){
		InterfaceDTOInfo<InterfaceWithPackageSuffix> info = InterfaceDTOInfo.getInfo(InterfaceWithPackageSuffix.class);
		assertEquals(info.getDTOCanonicalName(), "com.sleepcamel.ifdtoutils.dtoPackage.InterfaceWithPackageSuffixDTO");
	}

	@Test
	public void testInterfaceWithDTOSuffix(){
		InterfaceDTOInfo<InterfaceWithDtoSuffix> info = InterfaceDTOInfo.getInfo(InterfaceWithDtoSuffix.class);
		assertEquals(info.getDTOCanonicalName(), "com.sleepcamel.ifdtoutils.InterfaceWithDtoSuffix"+InterfaceWithDtoSuffix.class.getAnnotation(ToDTO.class).dtoSuffix());
	}
	

	@Test
	public void testInterfaceWithDTOName(){
		InterfaceDTOInfo<InterfaceWithDtoName> info = InterfaceDTOInfo.getInfo(InterfaceWithDtoName.class);
		assertEquals(info.getDTOCanonicalName(), "com.sleepcamel.ifdtoutils."+InterfaceWithDtoName.class.getAnnotation(ToDTO.class).dtoName());
	}
	
	@Test
	public void testInterfaceWithFullPackage(){
		InterfaceDTOInfo<InterfaceWithFullPackage> info = InterfaceDTOInfo.getInfo(InterfaceWithFullPackage.class);
		assertEquals(info.getDTOCanonicalName(), InterfaceWithFullPackage.class.getAnnotation(ToDTO.class).fullPackage()+".InterfaceWithFullPackageDTO");
	}
	
	@Test
	public void testNormalizeSubpackage(){
		InterfaceDTOInfo<InterfaceWithPackageSuffix> info = InterfaceDTOInfo.getInfo(InterfaceWithPackageSuffix.class);
		assertEquals(info.normalizePackage(".lala"),"lala");
		assertEquals(info.normalizePackage("aswd"),"aswd");
		assertEquals(info.normalizePackage("jkfr."),"jkfr");
		assertEquals(info.normalizePackage(".pofe."),"pofe");
	}
}
