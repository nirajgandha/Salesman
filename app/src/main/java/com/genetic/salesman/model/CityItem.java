package com.genetic.salesman.model;

import com.google.gson.annotations.SerializedName;

public class CityItem {

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("state_id")
	private int stateId;

	@SerializedName("deleted_at")
	private String deletedAt;

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getName(){
		return name;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public int getStateId(){
		return stateId;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",state_id = '" + stateId + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			"}";
		}
}