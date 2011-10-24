package com.sleepcamel.ifdtoutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javassist.NotFoundException;

import org.junit.Test;

import com.sleepcamel.ifdtoutils.methodUtil.InterfaceJavaMethodsUtil;


public class DTOClassGeneratorTest {
	
	@Test
	public void testGeneratedDTOContainsInterfaceMethods(){
		Class<CoreInterface> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(CoreInterface.class);
		Field[] declaredFields = generatedDTOClass.getDeclaredFields();
		assertEquals(1, declaredFields.length);
		assertEquals("coreField", declaredFields[0].getName());
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		assertEquals(1, declaredMethods.length);
		assertEquals("getCoreField", declaredMethods[0].getName());
	}
	
	@Test
	public void testGeneratedDTOContainsSuperInterfaceMethods(){
		Class<BaseInterface> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(BaseInterface.class);
		Field[] declaredFields = generatedDTOClass.getDeclaredFields();
		assertEquals(2, declaredFields.length);
		
		for(Field field:declaredFields){
			String name = field.getName();
			boolean matchesField = name.equals("baseField") || name.equals("coreField");
			assertTrue(matchesField);
		}
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		assertEquals(2, declaredMethods.length);
		
		for(Method method:declaredMethods){
			String name = method.getName();
			boolean matchesMethod = name.equals("getBaseField") || name.equals("getCoreField");
			assertTrue(matchesMethod);
		}
	}
	
	@Test
	public void testGeneratedDTOContainsSuperInterfacesMethods(){
		Class<AnotherInterface> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(AnotherInterface.class);
		Field[] declaredFields = generatedDTOClass.getDeclaredFields();
		assertEquals(4, declaredFields.length);
		
		for(Field field:declaredFields){
			String name = field.getName();
			boolean matchesField = name.equals("baseField") || name.equals("anotherField") || name.equals("coreField") || name.equals("aBaseField");
			assertTrue(matchesField);
		}
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		assertEquals(4, declaredMethods.length);
		
		for(Method method:declaredMethods){
			String name = method.getName();
			boolean matchesMethod = name.equals("getBaseField") || name.equals("getAnotherField") || name.equals("getABaseField") || name.equals("getCoreField");
			assertTrue(matchesMethod);
		}
	}
	
	@Test
	public void testSuperInterfacesWithSameMethods(){
		Class<InterfaceWithSameInheritedMethods> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(InterfaceWithSameInheritedMethods.class);
		Field[] declaredFields = generatedDTOClass.getDeclaredFields();
		assertEquals(2, declaredFields.length);
		
		for(Field field:declaredFields){
			String name = field.getName();
			boolean matchesField = name.equals("something") || name.equals("coreField");
			assertTrue(matchesField);
		}
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		assertEquals(2, declaredMethods.length);
		
		for(Method method:declaredMethods){
			String name = method.getName();
			boolean matchesMethod = name.equals("something") || name.equals("getCoreField");
			assertTrue(matchesMethod);
		}
	}
	
	@Test
	public void testGenerateDTOWithNonExportableMethods(){
		assertEquals(1, InterfaceJavaMethodsUtil.instance().getExportableMethods(InterfaceWithNonExportableMethods.class).size());
		
		Class<InterfaceWithNonExportableMethods> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(InterfaceWithNonExportableMethods.class);
		Field[] declaredFields = generatedDTOClass.getDeclaredFields();
		assertEquals(1, declaredFields.length);
		assertEquals("getter", declaredFields[0].getName());
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		assertEquals(1, declaredMethods.length);
		assertEquals("normalGetter", declaredMethods[0].getName());

		assertEquals(1, InterfaceJavaMethodsUtil.instance().getExportableMethods(InterfaceWithNonExportableMethods.class).size());
	}
	
	@Test
	public void testPositionAnnotationsAreGenerated(){
		Class<PositionInterface> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(PositionInterface.class, true);
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		assertEquals(declaredMethods.length, 4);
		
		Method method = InterfaceJavaMethodsUtil.instance().getMethodWithPosition(generatedDTOClass, 0);
		assertEquals(method.getName(), "getName");
		
		method = InterfaceJavaMethodsUtil.instance().getMethodWithPosition(generatedDTOClass, 1);
		assertEquals(method.getName(), "getSomeNumber");
		
		method = InterfaceJavaMethodsUtil.instance().getMethodWithPosition(generatedDTOClass, 2);
		assertEquals(method.getName(), "list");
		
		method = InterfaceJavaMethodsUtil.instance().getMethodWithPosition(generatedDTOClass, 3);
		assertEquals(method.getName(), "longValue");
	}
	
	@Test
	public void testGeneratedSetters() throws NotFoundException, InstantiationException, IllegalAccessException{
		Class<InterfaceWithSetters> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(InterfaceWithSetters.class, true, true);
		InterfaceJavaMethodsUtil methodsUtil = InterfaceJavaMethodsUtil.instance();
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		assertEquals(declaredMethods.length, 5);
		
		boolean setNameIsFound = false;
		boolean setSomeNumberIsFound = false;
		
		for(Method method:declaredMethods){
			if ( method.getName().equals("setName") && methodsUtil.isSetter(method, String.class) ){
				setNameIsFound = true;
			}
			
			if ( method.getName().equals("setSomeNumber") && methodsUtil.isSetter(method, int.class) ){
				setSomeNumberIsFound = true;
			}
		}
		
		assertTrue(setNameIsFound);
		assertTrue(setSomeNumberIsFound);
		
		InterfaceWithSetters newInstance = generatedDTOClass.newInstance();
		
		assertNull(newInstance.getName());
		newInstance.setName("New Name");
		assertEquals(newInstance.getName(), "New Name");
		
		assertEquals(newInstance.getSomeNumber(),0);
		newInstance.setSomeNumber(5);
		assertEquals(newInstance.getSomeNumber(), 5);
	}
}

interface PositionInterface{
	public String getName();
	
	public int getSomeNumber();
	
	public List<?> list();
	
	public Long longValue();
}