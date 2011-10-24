package com.sleepcamel.ifdtoutils.methodInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class MethodInfoKeyTest {

	@Test
	public void testHashCode(){
		int hashCode = new MethodInfoKey("Hello",0).hashCode();
		int hashCode2 = new MethodInfoKey("Hello").hashCode();
		
		assertEquals(hashCode2, hashCode2);
		assertNotSame(hashCode2, new MethodInfoKey("Helloo").hashCode());
		
		assertEquals(new MethodInfoKey("Hello",1).hashCode(), new MethodInfoKey("Hello",3).hashCode());
		assertNotSame(hashCode, new MethodInfoKey("Helloo",0).hashCode());
		
		assertNotSame(hashCode, new MethodInfoKey(null,0).hashCode());
	}
	
	@Test
	public void testEquals(){
		MethodInfoKey methodInfoKey = new MethodInfoKey("Hello");
		
		assertEquals(methodInfoKey, methodInfoKey);
		
		assertEquals(methodInfoKey, new MethodInfoKey("Hello"));
		assertNotSame(methodInfoKey, new MethodInfoKey("Helloo"));
		
		assertEquals(new MethodInfoKey("Hello",1), new MethodInfoKey("Hello",3));
		assertFalse(new MethodInfoKey("Hello",0).equals(new MethodInfoKey("Helloo",0)));
		
		assertFalse(new MethodInfoKey("Hello",0).equals(null));
		
		assertFalse(new MethodInfoKey("Hello",0).equals(3));
		
		assertFalse(new MethodInfoKey(null,0).equals(null));
		
		assertEquals(new MethodInfoKey(null,1), new MethodInfoKey(null,3));
		
		assertFalse(new MethodInfoKey(null,0).equals(new MethodInfoKey("Hello",3)));
	}
}
