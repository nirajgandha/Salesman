package com.genetic.salesman.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class StateListResponse{

	@SerializedName("data")
	private ArrayList<StateItem> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<StateItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"StateListResponse{" +
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}