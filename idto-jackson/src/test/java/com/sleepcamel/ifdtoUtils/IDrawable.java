package com.sleepcamel.ifdtoUtils;

import com.sleepcamel.ifdtoutils.ToDTO;

@ToDTO(packageSuffix="drawableDTOs")
public interface IDrawable{
	double getArea();
	
	boolean isHasSides();
	
	boolean nonUsableMethod(String something);
	
	void calculate();
}