package com.sleepcamel.ifdtoutils.processor;

import java.util.Map;

import com.sleepcamel.ifdtoutils.methodInfo.MethodInfoKey;

import javassist.CtClass;
import javassist.CtMethod;


public class NullProcessor implements ISetterProcessor, IAnnotationProcessor {

	private static NullProcessor instance;

	private NullProcessor(){}
	
	public static NullProcessor getInstance() {
		if ( instance == null ){
			instance = new NullProcessor();
		}
		return instance;
	}
	
	@Override
	public void process(CtClass cc, String string, Map<MethodInfoKey, CtMethod> settersInfo) {}

	@Override
	public void process(CtClass cc, CtMethod fieldMethod, int i) {}

}
