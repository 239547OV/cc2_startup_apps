package com.aa.crewcomp.rulesengine.mock.domain;

import lombok.Data;

@Data
public class MyAPlusBSampleEntity {
	private Integer a;
	private Integer b;
	private Integer computedAPlusB;
	private Integer expectedAPlusB;

	public MyAPlusBSampleEntity(){

	}

	public MyAPlusBSampleEntity(Integer a, Integer b){
		this.a = a;
		this.b = b;
		this.computedAPlusB = null;
		this.expectedAPlusB = this.a % 2 == 0? this.a + this.b: null;
	}
}
