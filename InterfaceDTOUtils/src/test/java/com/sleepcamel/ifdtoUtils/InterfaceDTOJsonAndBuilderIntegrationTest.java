package com.sleepcamel.ifdtoUtils;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.google.gson.reflect.TypeToken;
import com.sleepcamel.ifdtoUtils.json.InterfaceDTOJsonUtils;

public class InterfaceDTOJsonAndBuilderIntegrationTest {

	@Test
	public void testJsonIsSame(){
		IDrawable drawable = new Circle(2.56D);
		
		String justUtils = InterfaceDTOJsonUtils.toJson(IDrawable.class, drawable);
		String dto = InterfaceDTOBuilder.builder(IDrawable.class).toJson(drawable);

		assertEquals(justUtils, dto);
		
		IDrawable utilsDrawable = InterfaceDTOJsonUtils.fromJson(IDrawable.class, justUtils);
		IDrawable gsonDrawable = InterfaceDTOBuilder.builder(IDrawable.class)
								 .fromJson(justUtils);
		
		assertEquals(utilsDrawable.getArea(), gsonDrawable.getArea(), 0);
		assertEquals(utilsDrawable.getArea(), drawable.getArea(), 0);
		assertEquals(utilsDrawable.isHasSides(), gsonDrawable.isHasSides());
		assertEquals(utilsDrawable.isHasSides(), drawable.isHasSides());
	}
	
	@Test
	public void testJsonListIsSame(){
		List<IDrawable> drawables = new ArrayList<IDrawable>();
		for(int i=0; i<30; i++){
			drawables.add(new Square(1.2D));
			drawables.add(new Circle(1.3D));
			drawables.add(new Polygon(Arrays.asList(new Double[]{2.0D, 4.0D, -0.3D})));
			drawables.add(new Triangle(30.0D, 18.0D, 24.0D));
		}
		
		Type listType = new TypeToken<List<IDrawable>>() {}.getType();
		String gsonSerialization = InterfaceDTOJsonUtils.getGson(IDrawable.class).toJson(drawables, listType);
		
		String builderGson = InterfaceDTOBuilder.builder(IDrawable.class)
							.withToken(new TypeToken<List<IDrawable>>() {})
							.toJson(drawables);
		
		assertEquals(gsonSerialization, builderGson);
		
		List<IDrawable> deserializedDrawables = InterfaceDTOBuilder.builder(IDrawable.class)
									.withToken(new TypeToken<List<IDrawable>>() {})
									.fromJson(builderGson);
		
		Iterator<IDrawable> iterator = drawables.iterator();
		Iterator<IDrawable> iterator2 = deserializedDrawables.iterator();
		while(iterator.hasNext()){
			IDrawable next = iterator.next();
			IDrawable next2 = iterator2.next();
			assertEquals(next.getArea(), next2.getArea(), 0);
			assertEquals(next.isHasSides(), next2.isHasSides());	
		}
	}

}
