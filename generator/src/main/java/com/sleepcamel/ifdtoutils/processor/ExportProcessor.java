package com.sleepcamel.ifdtoutils.processor;

import java.lang.reflect.Modifier;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.IntegerMemberValue;

import org.apache.commons.lang3.StringUtils;

import com.sleepcamel.ifdtoutils.Pos;
import com.sleepcamel.ifdtoutils.methodInfo.MethodInfoKey;


public class ExportProcessor implements ISetterProcessor, IAnnotationProcessor {

	private static ExportProcessor instance;

	private ExportProcessor(){}
	
	public static ExportProcessor getInstance() {
		if ( instance == null ){
			instance = new ExportProcessor();
		}
		return instance;
	}
	
	@Override
	public void process(CtClass cc, String fieldName, Map<MethodInfoKey, CtMethod> settersInfo) throws NotFoundException, CannotCompileException {
		String setterName = "set"+StringUtils.capitalize(fieldName);
		CtMethod methodToUse = settersInfo.get(new MethodInfoKey(setterName));
		
		if ( methodToUse == null )
			return;

		// Copy Interface Method
		CtMethod fieldMethod = new CtMethod(methodToUse.getReturnType(), methodToUse.getName(), methodToUse.getParameterTypes(), cc);
		fieldMethod.setModifiers(fieldMethod.getModifiers() - Modifier.ABSTRACT);
		fieldMethod.setBody("this."+fieldName+" = $1;");
		cc.addMethod(fieldMethod);
	}

	@Override
	public void process(CtClass cc, CtMethod fieldMethod, int i) throws NotFoundException {
		CtClass ctClass = cc.getClassPool().getCtClass(Pos.class.getName());
		ConstPool constPool = cc.getClassFile().getConstPool();
		AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);

		Annotation annotation = new Annotation(constPool, ctClass);
		annotation.addMemberValue("value", new IntegerMemberValue(constPool, i));
		attr.setAnnotation(annotation);

		fieldMethod.getMethodInfo().addAttribute(attr);		
	}

}
