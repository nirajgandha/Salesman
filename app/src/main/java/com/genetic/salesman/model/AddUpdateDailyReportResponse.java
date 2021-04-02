package com.genetic.salesman.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AddUpdateDailyReportResponse{

	@SerializedName("data")
	private List<AddUpdateDailyReportDataItem> data;

	@SerializedName("meta")
	private Meta meta;

	public List<AddUpdateDailyReportDataItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"AddUpdateDailyReportResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}