package com.sleepcamel.dtoUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sleepcamel.dtoUtils.json.InterfaceDTOJsonUtils;

public class InterfaceDTOJsonTest {

	@Test
	public void testListSerializationDeserialization(){
		List<IDrawable> drawables = new ArrayList<IDrawable>();
		for(int i=0; i<30; i++){
			drawables.add(new Square(1.2D));
			drawables.add(new Circle(1.3D));
			drawables.add(new Polygon(Arrays.asList(new Double[]{2.0D, 4.0D, -0.3D})));
			drawables.add(new Triangle(30.0D, 18.0D, 24.0D));
		}

		String dtoSerialization = new Gson().toJson(InterfaceDTOUtils.getFilledDtos(IDrawable.class, drawables));
		
		Type listType = new TypeToken<List<IDrawable>>() {}.getType();
		String gsonSerialization = InterfaceDTOJsonUtils.getGson(IDrawable.class).toJson(drawables, listType);

		Assert.assertTrue(dtoSerialization.equals(gsonSerialization));
		
		List<IDrawable> deserializedDrawables = InterfaceDTOJsonUtils.getGson(IDrawable.class).fromJson(gsonSerialization, listType);
		
		Iterator<IDrawable> iterator = drawables.iterator();
		Iterator<IDrawable> iterator2 = deserializedDrawables.iterator();
		while(iterator.hasNext()){
			IDrawable next = iterator.next();
			IDrawable next2 = iterator2.next();
			Assert.assertEquals(next.getArea(), next2.getArea());
			Assert.assertEquals(next.isHasSides(), next2.isHasSides());	
		}
	}
	
	@Test
	public void testEntitySerializationDeserialization(){
		IDrawable drawable = new Circle(2.56D);
		
		String withGson = InterfaceDTOJsonUtils.getGson(IDrawable.class).toJson(drawable, IDrawable.class);
		
		String justUtils = InterfaceDTOJsonUtils.toJson(IDrawable.class, drawable);
		
		Assert.assertTrue(withGson.equals(justUtils));
		
		IDrawable utilsDrawable = InterfaceDTOJsonUtils.fromJson(IDrawable.class, withGson);
		IDrawable gsonDrawable = InterfaceDTOJsonUtils.getGson(IDrawable.class).fromJson(justUtils, IDrawable.class);
		
		Assert.assertEquals(utilsDrawable.getArea(), gsonDrawable.getArea());
		Assert.assertEquals(utilsDrawable.getArea(), drawable.getArea());
		Assert.assertEquals(utilsDrawable.isHasSides(), gsonDrawable.isHasSides());
		Assert.assertEquals(utilsDrawable.isHasSides(), drawable.isHasSides());
	}
}