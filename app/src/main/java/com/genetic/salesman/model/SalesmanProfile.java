package com.genetic.salesman.model;

import com.google.gson.annotations.SerializedName;

public class SalesmanProfile {

	@SerializedName("code")
	private String code;

	@SerializedName("gender")
	private String gender;

	@SerializedName("mother_name")
	private String motherName;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("mobileno")
	private int mobileno;

	@SerializedName("experience")
	private String experience;

	@SerializedName("other_document")
	private String otherDocument;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("role_id")
	private int roleId;

	@SerializedName("emergency_no")
	private int emergencyNo;

	@SerializedName("id")
	private int id;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	@SerializedName("dealer_id")
	private String dealerId;

	@SerializedName("resume")
	private String resume;

	@SerializedName("joining_letter")
	private String joiningLetter;

	@SerializedName("permanet_address")
	private String permanetAddress;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("avatar")
	private String avatar;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("current_address")
	private String currentAddress;

	@SerializedName("qualification")
	private String qualification;

	@SerializedName("marital_status")
	private String maritalStatus;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("father_name")
	private String fatherName;

	@SerializedName("dob")
	private String dob;

	@SerializedName("date_of_joining")
	private String dateOfJoining;

	@SerializedName("status")
	private String status;

	public String getCode(){
		return code;
	}

	public String getGender(){
		return gender;
	}

	public String getMotherName(){
		return motherName;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getMobileno(){
		return mobileno;
	}

	public String getExperience(){
		return experience;
	}

	public String getOtherDocument(){
		return otherDocument;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public int getRoleId(){
		return roleId;
	}

	public int getEmergencyNo(){
		return emergencyNo;
	}

	public int getId(){
		return id;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getEmail(){
		return email;
	}

	public String getDealerId(){
		return dealerId;
	}

	public String getResume(){
		return resume;
	}

	public String getJoiningLetter(){
		return joiningLetter;
	}

	public String getPermanetAddress(){
		return permanetAddress;
	}

	public String getLastName(){
		return lastName;
	}

	public String getAvatar(){
		return avatar;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getCurrentAddress(){
		return currentAddress;
	}

	public String getQualification(){
		return qualification;
	}

	public String getMaritalStatus(){
		return maritalStatus;
	}

	public String getUserId(){
		return userId;
	}

	public String getFatherName(){
		return fatherName;
	}

	public String getDob(){
		return dob;
	}

	public String getDateOfJoining(){
		return dateOfJoining;
	}

	public String getStatus(){
		return status;
	}

	@Override
	public String toString(){
		return
				"DataItem{" +
						"code = '" + code + '\'' +
						",gender = '" + gender + '\'' +
						",mother_name = '" + motherName + '\'' +
						",created_at = '" + createdAt + '\'' +
						",mobileno = '" + mobileno + '\'' +
						",experience = '" + experience + '\'' +
						",other_document = '" + otherDocument + '\'' +
						",updated_at = '" + updatedAt + '\'' +
						",role_id = '" + roleId + '\'' +
						",emergency_no = '" + emergencyNo + '\'' +
						",id = '" + id + '\'' +
						",first_name = '" + firstName + '\'' +
						",email = '" + email + '\'' +
						",dealer_id = '" + dealerId + '\'' +
						",resume = '" + resume + '\'' +
						",joining_letter = '" + joiningLetter + '\'' +
						",permanet_address = '" + permanetAddress + '\'' +
						",last_name = '" + lastName + '\'' +
						",avatar = '" + avatar + '\'' +
						",deleted_at = '" + deletedAt + '\'' +
						",current_address = '" + currentAddress + '\'' +
						",qualification = '" + qualification + '\'' +
						",marital_status = '" + maritalStatus + '\'' +
						",user_id = '" + userId + '\'' +
						",father_name = '" + fatherName + '\'' +
						",dob = '" + dob + '\'' +
						",date_of_joining = '" + dateOfJoining + '\'' +
						",status = '" + status + '\'' +
						"}";
	}
}