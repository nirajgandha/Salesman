package com.genetic.salesman.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DealerListFragmentResponse {

	@SerializedName("data")
	private ArrayList<DealerFragmentItem> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<DealerFragmentItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"DealerFragmentListResponse{" +
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}