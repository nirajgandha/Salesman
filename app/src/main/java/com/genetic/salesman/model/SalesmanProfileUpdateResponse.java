package com.genetic.salesman.model;

import com.google.gson.annotations.SerializedName;

public class SalesmanProfileUpdateResponse{

	@SerializedName("data")
	private String data;

	@SerializedName("meta")
	private Meta meta;

	public String getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"SalesmanProfileUpdateResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}