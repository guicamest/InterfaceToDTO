package com.sleepcamel.ifdtoutils.methodInfo;

public class MethodInfoKey {

	private String name;
	private int index;
	
	public MethodInfoKey(String methodName) {
		this(methodName,0);
	}

	public MethodInfoKey(String methodName, int methodIndex) {
		this.name = methodName;
		this.index = methodIndex;
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodInfoKey other = (MethodInfoKey) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
