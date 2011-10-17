package com.sleepcamel.ifdtoutils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ToDTO {
	String packageSuffix() default "";
	
	String fullPackage() default "";
	
	String dtoSuffix() default "";
	
	String dtoName() default "";
}
