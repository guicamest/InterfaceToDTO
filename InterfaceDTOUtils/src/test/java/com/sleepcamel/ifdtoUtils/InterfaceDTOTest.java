package com.sleepcamel.ifdtoUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
	
	@Test
	public void testIterableOfDTOs(){
		List<IDrawable> drawables = new ArrayList<IDrawable>();
		for(int i=0; i<30; i++){
			drawables.add(new Square(1.2D));
			drawables.add(new Circle(1.3D));
			drawables.add(new Polygon(Arrays.asList(new Double[]{2.0D, 4.0D, -0.3D})));
			drawables.add(new Triangle(30.0D, 18.0D, 24.0D));
		}
		
		Iterable<IDrawable> dtoDrawables = InterfaceDTOUtils.getFilledDtos(IDrawable.class, drawables);
		
		Iterator<IDrawable> iterator = drawables.iterator();
		Iterator<IDrawable> iterator2 = dtoDrawables.iterator();
		
		while(iterator.hasNext()){
			IDrawable next = iterator.next();
			IDrawable next2 = iterator2.next();
			Assert.assertEquals(next.getArea(), next2.getArea());
			Assert.assertEquals(next.isHasSides(), next2.isHasSides());	
		}
	}
}