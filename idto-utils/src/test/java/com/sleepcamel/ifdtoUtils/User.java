package com.sleepcamel.ifdtoUtils;


public class User implements IUser{

	private User mother;
	private User father;
	private INode node = new Node();

	@Override
	public IDrawable getAvatar() {
		return new Circle(2.3);
	}

	@Override
	public IUser getFather() {
		return father;
	}

	@Override
	public IUser getMother() {
		return mother;
	}

	public void setFather(User father) {
		this.father = father;
	}

	public void setMother(User mother) {
		this.mother = mother;
	}

	@Override
	public INode getNode() {
		return node ;
	}

}
