package com.sleepcamel.ifdtoUtils.json;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

import com.sleepcamel.ifdtoUtils.Circle;
import com.sleepcamel.ifdtoUtils.IDrawable;
import com.sleepcamel.ifdtoUtils.Polygon;
import com.sleepcamel.ifdtoUtils.Square;
import com.sleepcamel.ifdtoUtils.Triangle;


public class InterfaceDTOJsonAndBuilderIntegrationTest {

	@Test
	public void testJsonIsSame(){
		IDrawable drawable = new Circle(2.56D);
		
		String justUtils = InterfaceDTOJsonUtils.toJson(IDrawable.class, drawable);
		String dto = InterfaceDTOJsonBuilder.builder(IDrawable.class).toJson(drawable);

		assertEquals(justUtils, dto);
		
		IDrawable utilsDrawable = InterfaceDTOJsonUtils.fromJson(IDrawable.class, justUtils);
		IDrawable gsonDrawable = InterfaceDTOJsonBuilder.builder(IDrawable.class)
								 .fromJson(justUtils);
		
		assertEquals(utilsDrawable.getArea(), gsonDrawable.getArea(), 0);
		assertEquals(utilsDrawable.getArea(), drawable.getArea(), 0);
		assertEquals(utilsDrawable.isHasSides(), gsonDrawable.isHasSides());
		assertEquals(utilsDrawable.isHasSides(), drawable.isHasSides());
	}
	
	@Test
	public void testListSerializationDeserialization() throws JsonGenerationException, JsonMappingException, IOException{
		List<IDrawable> drawables = new ArrayList<IDrawable>();
		for(int i=0; i<30; i++){
			drawables.add(new Square(1.2D));
			drawables.add(new Circle(1.3D));
			drawables.add(new Polygon(Arrays.asList(new Double[]{2.0D, 4.0D, -0.3D})));
			drawables.add(new Triangle(30.0D, 18.0D, 24.0D));
		}
		TypeReference<List<IDrawable>> typeReference = new TypeReference<List<IDrawable>>() {};
		
		String dtoSerialization = InterfaceDTOJsonBuilder.builder(IDrawable.class)
														  .withToken(typeReference)
														  .toJson(drawables);
		
		ObjectMapper mapper = InterfaceDTOJsonUtils.getMapper(IDrawable.class);
		String utilsSerialization = mapper.writerWithType(typeReference).writeValueAsString(drawables);
		
		Assert.assertEquals(dtoSerialization,utilsSerialization);
		
		List<IDrawable> deserializedDrawables = InterfaceDTOJsonBuilder.builder(IDrawable.class)
		  																.withToken(typeReference)
		  																.fromJson(utilsSerialization);
		
		Iterator<IDrawable> iterator = drawables.iterator();
		Iterator<IDrawable> iterator2 = deserializedDrawables.iterator();
		while(iterator.hasNext()){
			IDrawable next = iterator.next();
			IDrawable next2 = iterator2.next();
			Assert.assertEquals(next.getArea(), next2.getArea());
			Assert.assertEquals(next.isHasSides(), next2.isHasSides());	
		}
	}
}
