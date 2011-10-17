package com.sleepcamel.ifdtoUtils;

import com.sleepcamel.ifdtoutils.DTOInterface;
import com.sleepcamel.ifdtoutils.ToDTO;

@ToDTO(dtoName="CustomDTOName")
public interface MusicInstrumentWithKeys extends DTOInterface{

	public boolean hasKeys();

}
