package com.sleepcamel.ifdtoUtils;

import com.sleepcamel.ifdtoutils.ToDTO;

@ToDTO(dtoSuffix="ASuffix")
public interface INode {

	void setFather(INode father);
	INode getFather();
	Node getChild();

}
