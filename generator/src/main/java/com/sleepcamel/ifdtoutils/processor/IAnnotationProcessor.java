package com.sleepcamel.ifdtoutils.processor;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public interface IAnnotationProcessor {

	public void process(CtClass cc, CtMethod fieldMethod, int i) throws NotFoundException;

}
