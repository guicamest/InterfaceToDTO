package com.sleepcamel.ifdtoUtils;

import com.sleepcamel.ifdtoutils.DTOInterface;
import com.sleepcamel.ifdtoutils.ToDTO;

@ToDTO(fullPackage="com.sleepcamel.ifdtoUtils.musicdtopackage")
public interface MusicInstrument extends DTOInterface{

	public boolean hasStrings();

}
