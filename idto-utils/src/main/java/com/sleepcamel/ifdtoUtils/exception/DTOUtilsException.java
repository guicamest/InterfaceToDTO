package com.sleepcamel.ifdtoUtils.exception;

public class DTOUtilsException extends RuntimeException{

	public DTOUtilsException(Exception e) {
		super(e);
	}

	public DTOUtilsException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2675402191355614018L;

}
