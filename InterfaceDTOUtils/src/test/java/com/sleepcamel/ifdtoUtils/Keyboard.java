package com.sleepcamel.ifdtoUtils;

public class Keyboard implements MusicInstrument, MusicInstrumentWithKeys {

	@Override
	public String getEntityType() {
		return "";
	}

	@Override
	public boolean hasStrings() {
		return false;
	}

	@Override
	public boolean hasKeys() {
		return true;
	}

}
