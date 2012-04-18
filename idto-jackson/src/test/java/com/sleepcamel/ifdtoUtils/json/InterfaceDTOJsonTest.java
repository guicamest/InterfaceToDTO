package com.sleepcamel.ifdtoUtils.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

import com.sleepcamel.ifdtoUtils.Circle;
import com.sleepcamel.ifdtoUtils.IDrawable;
import com.sleepcamel.ifdtoUtils.InterfaceDTOBuilder;
import com.sleepcamel.ifdtoUtils.Polygon;
import com.sleepcamel.ifdtoUtils.Square;
import com.sleepcamel.ifdtoUtils.Triangle;
import com.sleepcamel.ifdtoUtils.exception.DTOUtilsException;


public class InterfaceDTOJsonTest {

	@Test
	public void testEntitySerializationDeserialization() throws JsonGenerationException, JsonMappingException, IOException{
		Circle drawable = new Circle(2.56D);
		
		String json = InterfaceDTOJsonUtils.toJson(IDrawable.class, drawable);
		
		IDrawable utilsDrawable = InterfaceDTOJsonUtils.fromJson(IDrawable.class, json);
		assertNotNull("Should not be null",utilsDrawable);
		assertNotSame(utilsDrawable, drawable);

		assertEquals(utilsDrawable.getArea(), drawable.getArea(),0);
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
		
		Iterable<IDrawable> dtos = InterfaceDTOBuilder.builder(IDrawable.class).dto(drawables);
		String dtoSerialization = new ObjectMapper().writeValueAsString(dtos);
		
		TypeReference<List<IDrawable>> typeReference = new TypeReference<List<IDrawable>>() {};
		
		ObjectMapper mapper = InterfaceDTOJsonUtils.getMapper(IDrawable.class);
		String utilsSerialization = mapper.writerWithType(typeReference).writeValueAsString(drawables);
		
		Assert.assertEquals(dtoSerialization,utilsSerialization);
		
		List<IDrawable> deserializedDrawables = mapper.reader(typeReference).readValue(utilsSerialization);
		
		Iterator<IDrawable> iterator = drawables.iterator();
		Iterator<IDrawable> iterator2 = deserializedDrawables.iterator();
		while(iterator.hasNext()){
			IDrawable next = iterator.next();
			IDrawable next2 = iterator2.next();
			Assert.assertEquals(next.getArea(), next2.getArea());
			Assert.assertEquals(next.isHasSides(), next2.isHasSides());	
		}
	}
	
	@Test(expected=DTOUtilsException.class)
	public void testJsonBuilderForNotInterface(){
		InterfaceDTOJsonUtils.toJson(Circle.class, new Circle(32.2));
	}
	
	@Test
	public void testDeserializeEmptyJson() throws JsonProcessingException, IOException{
		IDrawable gsonDrawable = InterfaceDTOJsonUtils.getMapper(IDrawable.class).reader(IDrawable.class).readValue("{}");
		Assert.assertNotNull(gsonDrawable);
		Assert.assertEquals(0.0D, gsonDrawable.getArea());
		Assert.assertEquals(false, gsonDrawable.isHasSides());
	}

}