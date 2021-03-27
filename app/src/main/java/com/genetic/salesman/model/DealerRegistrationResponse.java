package com.genetic.salesman.model;

import com.google.gson.annotations.SerializedName;

public class DealerRegistrationResponse{

	@SerializedName("data")
	private DealerRegistrationData data;

	@SerializedName("meta")
	private Meta meta;

	public DealerRegistrationData getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"DealerRegistrationResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}