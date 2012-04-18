package com.sleepcamel.ifdtoUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import com.sleepcamel.ifdtoutils.InterfaceDTOInfo;

public class InterfaceDTOBuilderGenerationTest {

	@Test
	public void testDTOIsCorrectlyGenerated(){
		Circle circle = new Circle(4.6D);
		Square square = new Square(4.5D);
		
		IDrawable drawableCircle = InterfaceDTOBuilder.builder(IDrawable.class).dto(circle);
		IDrawable drawableSquare = InterfaceDTOBuilder.builder(IDrawable.class).dto(square);
		
		assertFalse(circle == drawableCircle);
		assertFalse(square == drawableSquare);
		
		Method[] methods = drawableCircle.getClass().getDeclaredMethods();
		assertEquals(methods.length, 2);
		for(Method method:methods){
			assertTrue(method.getName().equals("getArea") || method.getName().equals("isHasSides") );
		}
		
		methods = drawableSquare.getClass().getDeclaredMethods();
		assertEquals(methods.length, 2);
		for(Method method:methods){
			assertTrue(method.getName().equals("getArea") || method.getName().equals("isHasSides") );
		}
		
		assertEquals(drawableCircle.getArea(), circle.getArea(),0);
		assertEquals(drawableSquare.getArea(), square.getArea(),0);
		
		assertEquals(drawableCircle.isHasSides(), circle.isHasSides());
		assertEquals(drawableSquare.isHasSides(), square.isHasSides());
	}
	
	@Test
	public void testEntityTypeIsGeneratedOK(){
		Bass bass = new Bass();
		MusicInstrument bassDto = InterfaceDTOBuilder.builder(MusicInstrument.class).dto(bass);
		assertEquals(bassDto.getEntityType(),"BASS");
		
		Guitar guitar = new Guitar();
		MusicInstrument guitarDto = InterfaceDTOBuilder.builder(MusicInstrument.class).dto(guitar);
		assertEquals("Guitar", guitarDto.getEntityType());
		
		Keyboard keyboard = new Keyboard();
		MusicInstrument keyboardDto = InterfaceDTOBuilder.builder(MusicInstrument.class).dto(keyboard);
		assertEquals("Keyboard", keyboardDto.getEntityType());
		
		guitarDto = InterfaceDTOBuilder.builder(MusicInstrument.class)
										.useFullPackage()
										.dto(guitar);
		assertEquals("com.sleepcamel.ifdtoUtils.Guitar", guitarDto.getEntityType());
		
		keyboardDto = InterfaceDTOBuilder.builder(MusicInstrument.class)
										.useFullPackage()
										.dto(keyboard);
		assertEquals("com.sleepcamel.ifdtoUtils.Keyboard", keyboardDto.getEntityType());
		
		MusicInstrumentWithKeys keysDto = InterfaceDTOBuilder.builder(MusicInstrumentWithKeys.class)
										.useFullPackage()
										.dto(keyboard);
		assertEquals("com.sleepcamel.ifdtoUtils.Keyboard", keysDto.getEntityType());
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

		assertEquals(node.getClass().getName(), dtoClassName);

		assertEquals(node.getChild().getClass().getName(), Node.class.getName());

		String fatherClassName = node.getFather().getClass().getName();
		assertTrue(fatherClassName.equals(dtoClassName));
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
			assertEquals(next.getArea(), next2.getArea(), 0);
			assertEquals(next.isHasSides(), next2.isHasSides());	
		}
	}
	
	@Test
	public void testMultiInterfaceGeneration(){
		User user = new User();
		IFriendable friendable = InterfaceDTOBuilder.builder(IFriendable.class).dto(user);
		
		IDrawable generatedAvatar = friendable.getAvatar();
		IDrawable userAvatar = user.getAvatar();
		assertEquals(generatedAvatar.getClass(), userAvatar.getClass());
		
		friendable = InterfaceDTOBuilder.builder(IFriendable.class)
										.add(IDrawable.class)
										.dto(user);
		
		generatedAvatar = friendable.getAvatar();
		assertNotSame(generatedAvatar.getClass(), userAvatar.getClass());
		assertSame(generatedAvatar.getClass(), InterfaceDTOUtils.getDto(IDrawable.class).getClass());
		
		assertEquals(generatedAvatar.getArea(), userAvatar.getArea(), 0);
		assertEquals(generatedAvatar.isHasSides(), userAvatar.isHasSides());
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
		assertSame(generatedAvatar.getClass(), InterfaceDTOUtils.getDto(IDrawable.class).getClass());
		assertSame(friendable.getNode().getClass(), Node.class);		
		
		assertSame(friendable.getFather().getClass(), InterfaceDTOUtils.getDto(IUser.class).getClass());
		assertSame(friendable.getMother().getClass(), InterfaceDTOUtils.getDto(IUser.class).getClass());
		
		assertEquals(generatedAvatar.getArea(), userAvatar.getArea(), 0);
		assertEquals(generatedAvatar.isHasSides(), userAvatar.isHasSides());

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

		assertSame(friendable.getNode().getClass(), InterfaceDTOUtils.getDto(INode.class).getClass());
	}
}
