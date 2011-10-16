package com.sleepcamel.ifdtoUtils;

import com.sleepcamel.ifdtoutils.ToDTO;

@ToDTO(dtoSuffix="ASuffix")
public interface INode {

	INode getFather();
	Node getChild();

}
