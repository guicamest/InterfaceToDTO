package com.sleepcamel.dtoUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javassist.NotFoundException;

import com.google.gson.Gson;
import com.sleepcamel.ifdtoutils.DTOClassGenerator;


public class InterfaceDTOUtils {

	public static void main(String[] args) {
		Circle circle = new Circle(4.6D);
		IDrawable drawableCircle = InterfaceDTOUtils.getFilledDto(IDrawable.class, circle);
		Gson gson = new Gson();
		
		System.out.println(gson.toJson(circle) + " becomes " + gson.toJson(drawableCircle));
		
		Square square = new Square(4.1D);
		IDrawable drawableSquare = InterfaceDTOUtils.getFilledDto(IDrawable.class, square);
		
		System.out.println(gson.toJson(square) + " becomes " + gson.toJson(drawableSquare));
		
		Polygon polygon = new Polygon(Arrays.asList(new Double[]{2.0D, 4.0D, -0.3D}));
		IDrawable drawablePolygon = InterfaceDTOUtils.getFilledDto(IDrawable.class, polygon);
		
		System.out.println(gson.toJson(polygon) + " becomes " + gson.toJson(drawablePolygon));
		
		Triangle triangle = new Triangle(30.0D, 18.0D, 24.0D);
		IDrawable drawableTriangle = InterfaceDTOUtils.getFilledDto(IDrawable.class, triangle);
		
		System.out.println(gson.toJson(triangle) + " becomes " + gson.toJson(drawableTriangle));
		
		List<IDrawable> drawables = new ArrayList<IDrawable>();
		for(int i=0; i<30; i++){
			drawables.add(new Square(1.2D));
			drawables.add(new Circle(1.3D));
			drawables.add(new Polygon(Arrays.asList(new Double[]{2.0D, 4.0D, -0.3D})));
			drawables.add(new Triangle(30.0D, 18.0D, 24.0D));
		}

		System.out.println(gson.toJson(drawables) + "\nbecomes\n" + gson.toJson(InterfaceDTOUtils.getFilledDtos(IDrawable.class, drawables)));
		
	}

	public static <T> Iterable<T> getFilledDtos(Class<T> interfaceClass, Iterable<T> object) {
		List<T> dtos = new ArrayList<T>();
		Iterator<T> iterator = object.iterator();
		while(iterator.hasNext()){
			dtos.add(getFilledDto(interfaceClass, iterator.next()));
		}
		return dtos;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getDto(Class<T> interfaceClass) {
		Class<T> dtoClass = null;
		try{
			try{
				dtoClass = (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(DTOClassGenerator.getDTOName(interfaceClass,""));
			}catch(ClassNotFoundException e){
				dtoClass = (Class<T>) DTOClassGenerator.generateDTOForInterface(interfaceClass);
			}
			return dtoClass.newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T getFilledDto(Class<T> interfaceClass, T object) {
		try{
			return fillInstance(interfaceClass, object, getDto(interfaceClass));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static <T> T fillInstance(Class<T> interfaceClass, T source, T destiny) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NotFoundException {
		Class<? extends Object> destinyClass = destiny.getClass();
		for(Method method:DTOClassGenerator.getExportableMethods(interfaceClass) ){
			Field field = destinyClass.getDeclaredField(DTOClassGenerator.getFieldNameFromMethod(method.getName()));
			field.set(destiny, method.invoke(source));
		}
		return destiny;
	}

}
