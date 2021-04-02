package com.genetic.salesman.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class GetDealerDataResponse{

	@SerializedName("data")
	private ArrayList<DealerDataUpdate> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<DealerDataUpdate> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"GetDealerDataResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}