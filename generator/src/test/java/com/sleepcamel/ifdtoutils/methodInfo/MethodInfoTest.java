package com.sleepcamel.ifdtoutils.methodInfo;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;

import com.sleepcamel.ifdtoutils.CoreInterface;

public class MethodInfoTest {

	@Test
	public void testGettersAndSettersAreNotOverriten(){
		MethodInfo<Method> methodInfo = new MethodInfo<Method>();
		
		Method[] methods = CoreInterface.class.getMethods();
		Method method = methods[0];
		methodInfo.addToGetters("oneName", 2, method);
		methodInfo.addToGetters("oneName", 2, method);
		assertEquals(methodInfo.getGettersInfo().size(), 1);
		
		methodInfo.addToSetters("otherName", 2, method);
		methodInfo.addToSetters("otherName", 2, method);
		assertEquals(methodInfo.getSettersInfo().size(), 1);
	}
}
