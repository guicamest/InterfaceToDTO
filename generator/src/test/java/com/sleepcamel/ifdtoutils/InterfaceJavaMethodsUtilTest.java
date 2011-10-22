package com.sleepcamel.ifdtoutils;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class InterfaceJavaMethodsUtilTest {

	@Test
	public void testMethodsAreSorted(){
		List<Method> exportableMethods = InterfaceJavaMethodsUtil.instance().getExportableMethods(ISomeInterface.class, new MethodNameComparator());
		assertEquals(exportableMethods.size(), 4);
		
		assertEquals(exportableMethods.get(0).getName(), "getData");
		assertEquals(exportableMethods.get(1).getName(), "getH");
		assertEquals(exportableMethods.get(2).getName(), "getMoney");
		assertEquals(exportableMethods.get(3).getName(), "getString");
	}
	
	@Test
	public void testGetMethodWithPosition(){
		Class<ISomeInterface> generateDTOForInterface = DTOClassGenerator.generateDTOForInterface(ISomeInterface.class, true);
		InterfaceJavaMethodsUtil instance = InterfaceJavaMethodsUtil.instance();
		
		assertEquals(instance.getMethodWithPosition(generateDTOForInterface, 0).getName(), "getString");
		assertEquals(instance.getMethodWithPosition(generateDTOForInterface, 1).getName(), "getMoney");
		assertEquals(instance.getMethodWithPosition(generateDTOForInterface, 2).getName(), "getData");
		assertEquals(instance.getMethodWithPosition(generateDTOForInterface, 3).getName(), "getH");
		assertNull(instance.getMethodWithPosition(generateDTOForInterface, 4));
		
		Class<EmptyInterface> emptyDTO = DTOClassGenerator.generateDTOForInterface(EmptyInterface.class, true);
		assertNull(instance.getMethodWithPosition(emptyDTO, 0));
	}
}

class MethodNameComparator implements Comparator<Method> {
	
	@Override
	public int compare(Method arg0, Method arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
}

interface EmptyInterface{}

interface ISomeInterface{
	public String getString();
	
	public Double getMoney();
	
	public String getData();
	
	public Boolean getH();
	
}
