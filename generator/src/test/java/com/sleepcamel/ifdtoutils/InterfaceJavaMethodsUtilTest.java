package com.sleepcamel.ifdtoutils;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class InterfaceJavaMethodsUtilTest {

	@Test
	public void testMethodsAreSorted(){
		List<Method> exportableMethods = InterfaceJavaMethodsUtil.instance().getExportableMethods(ISomeInterface.class, new MethodNameComparator());
		Assert.assertEquals(exportableMethods.size(), 4);
		
		Assert.assertEquals(exportableMethods.get(0).getName(), "getData");
		Assert.assertEquals(exportableMethods.get(1).getName(), "getH");
		Assert.assertEquals(exportableMethods.get(2).getName(), "getMoney");
		Assert.assertEquals(exportableMethods.get(3).getName(), "getString");
	}
}

class MethodNameComparator implements Comparator<Method> {
	
	@Override
	public int compare(Method arg0, Method arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
}

interface ISomeInterface{
	public String getString();
	
	public Double getMoney();
	
	public String getData();
	
	public Boolean getH();
	
}
