package com.sleepcamel.ifdtoUtils;

public class Node implements INode {

	INode fatherNode;
	Node childNode;

	public Node(Node father, Node child) {
		fatherNode = father;
		childNode = child;
	}

	public Node() {
	}

	@Override
	public INode getFather() {
		return fatherNode;
	}

	@Override
	public Node getChild() {
		return childNode;
	}

	public void setChild(Node intermediate) {
		childNode = intermediate;
	}

	public void setFather(INode intermediate) {
		fatherNode = intermediate;
	}

}
