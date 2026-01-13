package com.aa.crewcomp.rulesengine.mock.domain;

import lombok.Data;

@Data
public class MySampleEntity {
	private String computedValue;
	private String expectedValue;
	public MySampleEntity(){
	}
	
	public MySampleEntity(String expectedValue){
		this.expectedValue = expectedValue;
	}
}
