package com.sleepcamel.ifdtoUtils;

public class Bass implements MusicInstrument {

	@Override
	public String getEntityType() {
		return "BASS";
	}

	@Override
	public boolean hasStrings() {
		return true;
	}

}
