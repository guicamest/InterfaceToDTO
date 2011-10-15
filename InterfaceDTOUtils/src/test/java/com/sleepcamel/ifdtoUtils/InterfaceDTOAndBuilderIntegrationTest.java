package com.sleepcamel.ifdtoUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class InterfaceDTOAndBuilderIntegrationTest {

	@Test
	public void testDTOIsSame(){
		Circle circle = new Circle(4.6D);
		
		IDrawable drawableCircle = InterfaceDTOUtils.getFilledDto(IDrawable.class, circle);
		
		IDrawable dto = InterfaceDTOBuilder.builder(IDrawable.class).dto(circle);
		Assert.assertEquals(drawableCircle.getClass(), dto.getClass());
		
		Assert.assertEquals(drawableCircle.getArea(), dto.getArea(),0.0D);
		
		Assert.assertEquals(drawableCircle.isHasSides(), dto.isHasSides());
	}
	
	@Test
	public void testRecursionIsSame(){
		Node father = new Node();
		Node child = new Node();
		Node intermediate = new Node(father, child);
		father.setChild(intermediate);
		child.setFather(intermediate);
		
		INode node = InterfaceDTOUtils.getFilledDto(INode.class, intermediate, true);
		
		INode dto = InterfaceDTOBuilder.builder(INode.class)
						.recursive(true).dto(intermediate);
		
		Assert.assertEquals(node.getClass(), dto.getClass());
		
		Assert.assertEquals(node.getFather().getClass(), dto.getFather().getClass());
		
		Assert.assertEquals(node.getChild().getClass(), dto.getChild().getClass());
	}
	
	@Test
	public void testIterablesOfDTOs(){
		List<IDrawable> drawables = new ArrayList<IDrawable>();
		for(int i=0; i<30; i++){
			drawables.add(new Square(1.2D));
			drawables.add(new Circle(1.3D));
			drawables.add(new Polygon(Arrays.asList(new Double[]{2.0D, 4.0D, -0.3D})));
			drawables.add(new Triangle(30.0D, 18.0D, 24.0D));
		}
		
		Iterable<IDrawable> dtoDrawables = InterfaceDTOBuilder.builder(IDrawable.class).dto(drawables);
		
		Iterator<IDrawable> iterator = drawables.iterator();
		Iterator<IDrawable> iterator2 = dtoDrawables.iterator();
		
		while(iterator.hasNext()){
			IDrawable next = iterator.next();
			IDrawable next2 = iterator2.next();
			Assert.assertEquals(next.getArea(), next2.getArea(), 0);
			Assert.assertEquals(next.isHasSides(), next2.isHasSides());	
		}
	}
}
