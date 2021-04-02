package com.genetic.salesman.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class DailyReportListResponse{

	@SerializedName("data")
	private ArrayList<DailyReportListItem> data;

	@SerializedName("meta")
	private Meta meta;

	public ArrayList<DailyReportListItem> getData(){
		return data;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"DailyReportListResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}