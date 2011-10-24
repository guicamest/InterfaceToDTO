package com.sleepcamel.ifdtoutils.processor;

import java.util.Map;

import com.sleepcamel.ifdtoutils.methodInfo.MethodInfoKey;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public interface ISetterProcessor {

	public void process(CtClass cc, String string, Map<MethodInfoKey, CtMethod> settersInfo) throws NotFoundException, CannotCompileException;

}
