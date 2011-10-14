package com.sleepcamel.ifdtoUtils;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;

import com.sleepcamel.ifdtoUtils.InterfaceDTOUtils;
import com.sleepcamel.ifdtoutils.DTOClassGenerator;

public class InterfaceDTOTest {

	@Test
	public void testDrawableCase(){
		Circle circle = new Circle(4.6D);
		Square square = new Square(4.5D);

		IDrawable drawableCircle = InterfaceDTOUtils.getFilledDto(IDrawable.class, circle);
		IDrawable drawableSquare = InterfaceDTOUtils.getFilledDto(IDrawable.class, square);
		
		Assert.assertFalse(circle == drawableCircle);
		Assert.assertFalse(square == drawableSquare);
		
		Method[] methods = drawableCircle.getClass().getDeclaredMethods();
		Assert.assertEquals(methods.length, 2);
		for(Method method:methods){
			Assert.assertTrue(method.getName().equals("getArea") || method.getName().equals("isHasSides") );
		}
		
		methods = drawableSquare.getClass().getDeclaredMethods();
		Assert.assertEquals(methods.length, 2);
		for(Method method:methods){
			Assert.assertTrue(method.getName().equals("getArea") || method.getName().equals("isHasSides") );
		}
		
		Assert.assertEquals(drawableCircle.getArea(), circle.getArea());
		Assert.assertEquals(drawableSquare.getArea(), square.getArea());
		
		Assert.assertEquals(drawableCircle.isHasSides(), circle.isHasSides());
		Assert.assertEquals(drawableSquare.isHasSides(), square.isHasSides());
	}

	@Test
	public void testFillDTOsWithSameInterface(){
		Node father = new Node();
		Node child = new Node();
		Node intermediate = new Node(father, child);
		father.setChild(intermediate);
		child.setFather(intermediate);
		
		INode node = InterfaceDTOUtils.getFilledDto(INode.class, intermediate, true);
		
		String dtoClassName = DTOClassGenerator.getDTOName(INode.class, "");

		Assert.assertEquals(node.getClass().getName(), dtoClassName);

		Assert.assertEquals(node.getChild().getClass().getName(), Node.class.getName());

		String fatherClassName = node.getFather().getClass().getName();
		Assert.assertTrue(fatherClassName.equals(dtoClassName));
	}
}