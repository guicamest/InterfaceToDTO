package com.sleepcamel.ifdtoutils;

public interface InterfaceWithSetters {

	public void setName(String bla);
	public String getName();
	
	public long getterWithoutSetter();

	public int getSomeNumber();
	public void setSomeNumber(int a);

	public void setterWithoutGetter(long something);
}
