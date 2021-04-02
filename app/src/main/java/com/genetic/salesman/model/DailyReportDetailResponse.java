package com.genetic.salesman.model;

import com.google.gson.annotations.SerializedName;

public class DailyReportDetailResponse{

	@SerializedName("data")
	private DailyReportData data;

	@SerializedName("meta")
	private Meta meta;

	public DailyReportData getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"DailyReportDetailResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}