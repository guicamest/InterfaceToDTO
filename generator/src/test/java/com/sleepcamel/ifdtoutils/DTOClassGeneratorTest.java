package com.sleepcamel.ifdtoutils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class DTOClassGeneratorTest {
	
	@Test
	public void testGeneratedDTOContainsInterfaceMethods(){
		Class<CoreInterface> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(CoreInterface.class);
		Field[] declaredFields = generatedDTOClass.getDeclaredFields();
		Assert.assertEquals(1, declaredFields.length);
		Assert.assertEquals("coreField", declaredFields[0].getName());
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		Assert.assertEquals(1, declaredMethods.length);
		Assert.assertEquals("getCoreField", declaredMethods[0].getName());
	}
	
	@Test
	public void testGeneratedDTOContainsSuperInterfaceMethods(){
		Class<BaseInterface> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(BaseInterface.class);
		Field[] declaredFields = generatedDTOClass.getDeclaredFields();
		Assert.assertEquals(2, declaredFields.length);
		
		for(Field field:declaredFields){
			String name = field.getName();
			boolean matchesField = name.equals("baseField") || name.equals("coreField");
			Assert.assertTrue(matchesField);
		}
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		Assert.assertEquals(2, declaredMethods.length);
		
		for(Method method:declaredMethods){
			String name = method.getName();
			boolean matchesMethod = name.equals("getBaseField") || name.equals("getCoreField");
			Assert.assertTrue(matchesMethod);
		}
	}
	
	@Test
	public void testGeneratedDTOContainsSuperInterfacesMethods(){
		Class<AnotherInterface> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(AnotherInterface.class);
		Field[] declaredFields = generatedDTOClass.getDeclaredFields();
		Assert.assertEquals(4, declaredFields.length);
		
		for(Field field:declaredFields){
			String name = field.getName();
			boolean matchesField = name.equals("baseField") || name.equals("anotherField") || name.equals("coreField") || name.equals("aBaseField");
			Assert.assertTrue(matchesField);
		}
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		Assert.assertEquals(4, declaredMethods.length);
		
		for(Method method:declaredMethods){
			String name = method.getName();
			boolean matchesMethod = name.equals("getBaseField") || name.equals("getAnotherField") || name.equals("getABaseField") || name.equals("getCoreField");
			Assert.assertTrue(matchesMethod);
		}
	}
	
	@Test
	public void testSuperInterfacesWithSameMethods(){
		Class<InterfaceWithSameInheritedMethods> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(InterfaceWithSameInheritedMethods.class);
		Field[] declaredFields = generatedDTOClass.getDeclaredFields();
		Assert.assertEquals(2, declaredFields.length);
		
		for(Field field:declaredFields){
			String name = field.getName();
			boolean matchesField = name.equals("something") || name.equals("coreField");
			Assert.assertTrue(matchesField);
		}
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		Assert.assertEquals(2, declaredMethods.length);
		
		for(Method method:declaredMethods){
			String name = method.getName();
			boolean matchesMethod = name.equals("something") || name.equals("getCoreField");
			Assert.assertTrue(matchesMethod);
		}
	}
	
	@Test
	public void testGenerateDTOWithNonExportableMethods(){
		Assert.assertEquals(1, InterfaceJavaMethodsUtil.instance().getExportableMethods(InterfaceWithNonExportableMethods.class).size());
		
		Class<InterfaceWithNonExportableMethods> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(InterfaceWithNonExportableMethods.class);
		Field[] declaredFields = generatedDTOClass.getDeclaredFields();
		Assert.assertEquals(1, declaredFields.length);
		Assert.assertEquals("getter", declaredFields[0].getName());
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		Assert.assertEquals(1, declaredMethods.length);
		Assert.assertEquals("normalGetter", declaredMethods[0].getName());

		Assert.assertEquals(1, InterfaceJavaMethodsUtil.instance().getExportableMethods(InterfaceWithNonExportableMethods.class).size());
	}
	
	@Test
	public void testPositionAnnotationsAreGenerated(){
		Class<PositionInterface> generatedDTOClass = DTOClassGenerator.generateDTOForInterface(PositionInterface.class, true);
		
		Method[] declaredMethods = generatedDTOClass.getDeclaredMethods();
		Assert.assertEquals(declaredMethods.length, 4);
		for(Method method:declaredMethods){
			if ( method.getName().equals("getName") ){
				Pos annotation = method.getAnnotation(Pos.class);
				Assert.assertNotNull(annotation);
				Assert.assertEquals(annotation.value(), 0);
			}
			
			if ( method.getName().equals("getSomeNumber") ){
				Pos annotation = method.getAnnotation(Pos.class);
				Assert.assertNotNull(annotation);
				Assert.assertEquals(annotation.value(), 1);
			}
			
			if ( method.getName().equals("list") ){
				Pos annotation = method.getAnnotation(Pos.class);
				Assert.assertNotNull(annotation);
				Assert.assertEquals(annotation.value(), 2);
			}
			
			if ( method.getName().equals("longValue") ){
				Pos annotation = method.getAnnotation(Pos.class);
				Assert.assertNotNull(annotation);
				Assert.assertEquals(annotation.value(), 3);
			}
		}
	}
}

interface PositionInterface{
	public String getName();
	
	public int getSomeNumber();
	
	public List<?> list();
	
	public Long longValue();
}