package com.genetic.salesman.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class DealerListResponse{

	@SerializedName("data")
	private ArrayList<DealerItem> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<DealerItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"DealerListResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}