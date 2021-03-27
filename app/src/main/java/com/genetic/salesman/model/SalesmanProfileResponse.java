package com.genetic.salesman.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class SalesmanProfileResponse{

	@SerializedName("data")
	private ArrayList<SalesmanProfile> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<SalesmanProfile> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"SalesmanProfileResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}