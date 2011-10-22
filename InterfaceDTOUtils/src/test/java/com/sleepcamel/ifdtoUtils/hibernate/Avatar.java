package com.sleepcamel.ifdtoUtils.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Avatar {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column
	protected Long id;
	
	private String url;

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
