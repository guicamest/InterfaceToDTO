package com.sleepcamel.ifdtoUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sleepcamel.ifdtoutils.InterfaceDTOInfo;

public class InterfaceDTOBuilderGenerationTest {

	@Test
	public void testDTOIsCorrectlyGenerated(){
		Circle circle = new Circle(4.6D);
		Square square = new Square(4.5D);
		
		IDrawable drawableCircle = InterfaceDTOBuilder.builder(IDrawable.class).dto(circle);
		IDrawable drawableSquare = InterfaceDTOBuilder.builder(IDrawable.class).dto(square);
		
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
		
		Assert.assertEquals(drawableCircle.getArea(), circle.getArea(),0);
		Assert.assertEquals(drawableSquare.getArea(), square.getArea(),0);
		
		Assert.assertEquals(drawableCircle.isHasSides(), circle.isHasSides());
		Assert.assertEquals(drawableSquare.isHasSides(), square.isHasSides());
	}
	
	@Test
	public void testEntityTypeIsGeneratedOK(){
		Bass bass = new Bass();
		MusicInstrument bassDto = InterfaceDTOBuilder.builder(MusicInstrument.class).dto(bass);
		Assert.assertEquals(bassDto.getEntityType(),"BASS");
		
		Guitar guitar = new Guitar();
		MusicInstrument guitarDto = InterfaceDTOBuilder.builder(MusicInstrument.class).dto(guitar);
		Assert.assertEquals("Guitar", guitarDto.getEntityType());
		
		Keyboard keyboard = new Keyboard();
		MusicInstrument keyboardDto = InterfaceDTOBuilder.builder(MusicInstrument.class).dto(keyboard);
		Assert.assertEquals("Keyboard", keyboardDto.getEntityType());
		
		guitarDto = InterfaceDTOBuilder.builder(MusicInstrument.class)
										.useFullPackage()
										.dto(guitar);
		Assert.assertEquals("com.sleepcamel.ifdtoUtils.Guitar", guitarDto.getEntityType());
		
		keyboardDto = InterfaceDTOBuilder.builder(MusicInstrument.class)
										.useFullPackage()
										.dto(keyboard);
		Assert.assertEquals("com.sleepcamel.ifdtoUtils.Keyboard", keyboardDto.getEntityType());
	}
	
	@Test
	public void testRecursionIsSame(){
		Node father = new Node();
		Node child = new Node();
		Node intermediate = new Node(father, child);
		father.setChild(intermediate);
		child.setFather(intermediate);
		
		INode node = InterfaceDTOBuilder.builder(INode.class)
						.recursive(true).dto(intermediate);
		
		String dtoClassName = InterfaceDTOInfo.getInfo(INode.class).getDTOCanonicalName();

		Assert.assertEquals(node.getClass().getName(), dtoClassName);

		Assert.assertEquals(node.getChild().getClass().getName(), Node.class.getName());

		String fatherClassName = node.getFather().getClass().getName();
		Assert.assertTrue(fatherClassName.equals(dtoClassName));
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
	
	@Test
	public void testMultiInterfaceGeneration(){
		User user = new User();
		IFriendable friendable = InterfaceDTOBuilder.builder(IFriendable.class).dto(user);
		
		IDrawable generatedAvatar = friendable.getAvatar();
		IDrawable userAvatar = user.getAvatar();
		Assert.assertEquals(generatedAvatar.getClass(), userAvatar.getClass());
		
		friendable = InterfaceDTOBuilder.builder(IFriendable.class)
										.add(IDrawable.class)
										.dto(user);
		
		generatedAvatar = friendable.getAvatar();
		Assert.assertNotSame(generatedAvatar.getClass(), userAvatar.getClass());
		Assert.assertSame(generatedAvatar.getClass(), InterfaceDTOUtils.getDto(IDrawable.class).getClass());
		
		Assert.assertEquals(generatedAvatar.getArea(), userAvatar.getArea(), 0);
		Assert.assertEquals(generatedAvatar.isHasSides(), userAvatar.isHasSides());
	}
	
	@Test
	public void testMultiInterfaceGenerationWithRecursion(){
		User user = new User();
		user.setFather(new User());
		user.setMother(new User());

		IDrawable userAvatar = user.getAvatar();
		
		IUser friendable = InterfaceDTOBuilder.builder(IUser.class)
										.recursive(true)
										.add(IDrawable.class)
										.dto(user);
		
		IDrawable generatedAvatar = friendable.getAvatar();
		Assert.assertSame(generatedAvatar.getClass(), InterfaceDTOUtils.getDto(IDrawable.class).getClass());
		Assert.assertSame(friendable.getNode().getClass(), Node.class);		
		
		Assert.assertSame(friendable.getFather().getClass(), InterfaceDTOUtils.getDto(IUser.class).getClass());
		Assert.assertSame(friendable.getMother().getClass(), InterfaceDTOUtils.getDto(IUser.class).getClass());
		
		Assert.assertEquals(generatedAvatar.getArea(), userAvatar.getArea(), 0);
		Assert.assertEquals(generatedAvatar.isHasSides(), userAvatar.isHasSides());

		// Now add INode twice, one of them is discarded
		// Add String and IGenerable also, they shouldn't have effect 
		friendable = InterfaceDTOBuilder.builder(IUser.class)
												.recursive(true)
												.add(IDrawable.class)
												.add(INode.class)
												.add(INode.class)
												.add(String.class)
												.add(IGenerable.class)
												.dto(user);

		Assert.assertSame(friendable.getNode().getClass(), InterfaceDTOUtils.getDto(INode.class).getClass());
	}
}
