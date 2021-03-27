package com.genetic.salesman.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CityListResponse{

	@SerializedName("data")
	private List<CityItem> data;

	@SerializedName("meta")
	private Meta meta;

	public List<CityItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"CityListResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}