package com.genetic.salesman.model;

import com.google.gson.annotations.SerializedName;

public class AddUpdateDailyReportDataItem {

	@SerializedName("date")
	private String date;

	@SerializedName("delear_business_name")
	private String delearBusinessName;

	@SerializedName("staying_charges")
	private String stayingCharges;

	@SerializedName("report_status")
	private String reportStatus;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("end_km")
	private int endKm;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("delear_product_review_date")
	private String delearProductReviewDate;

	@SerializedName("dealer_mobile_no")
	private String dealerMobileNo;

	@SerializedName("mobile_exp")
	private String mobileExp;

	@SerializedName("id")
	private int id;

	@SerializedName("end_km_pic")
	private String endKmPic;

	@SerializedName("dealer_demo_product")
	private String dealerDemoProduct;

	@SerializedName("start_km_pic")
	private String startKmPic;

	@SerializedName("sales_man_id")
	private int salesManId;

	@SerializedName("start_km")
	private int startKm;

	@SerializedName("other_exp")
	private String otherExp;

	@SerializedName("total_km")
	private int totalKm;

	@SerializedName("dealer_location")
	private String dealerLocation;

	@SerializedName("dinner_exp")
	private String dinnerExp;

	@SerializedName("deleted_at")
	private String deletedAt;

	@SerializedName("delear_product_review")
	private String delearProductReview;

	@SerializedName("dealer_used_crop_product")
	private String dealerUsedCropProduct;

	@SerializedName("dealer_name")
	private String dealerName;

	@SerializedName("dealer_email")
	private String dealerEmail;

	@SerializedName("farmer_delear_selfy_pic")
	private String farmerDelearSelfyPic;

	public String getDate(){
		return date;
	}

	public String getDelearBusinessName(){
		return delearBusinessName;
	}

	public String getStayingCharges(){
		return stayingCharges;
	}

	public String getReportStatus(){
		return reportStatus;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getEndKm(){
		return endKm;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getDelearProductReviewDate(){
		return delearProductReviewDate;
	}

	public String getDealerMobileNo(){
		return dealerMobileNo;
	}

	public String getMobileExp(){
		return mobileExp;
	}

	public int getId(){
		return id;
	}

	public String getEndKmPic(){
		return endKmPic;
	}

	public String getDealerDemoProduct(){
		return dealerDemoProduct;
	}

	public String getStartKmPic(){
		return startKmPic;
	}

	public int getSalesManId(){
		return salesManId;
	}

	public int getStartKm(){
		return startKm;
	}

	public String getOtherExp(){
		return otherExp;
	}

	public int getTotalKm(){
		return totalKm;
	}

	public String getDealerLocation(){
		return dealerLocation;
	}

	public String getDinnerExp(){
		return dinnerExp;
	}

	public String getDeletedAt(){
		return deletedAt;
	}

	public String getDelearProductReview(){
		return delearProductReview;
	}

	public String getDealerUsedCropProduct(){
		return dealerUsedCropProduct;
	}

	public String getDealerName(){
		return dealerName;
	}

	public String getDealerEmail(){
		return dealerEmail;
	}

	public String getFarmerDelearSelfyPic(){
		return farmerDelearSelfyPic;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"date = '" + date + '\'' + 
			",delear_business_name = '" + delearBusinessName + '\'' + 
			",staying_charges = '" + stayingCharges + '\'' + 
			",report_status = '" + reportStatus + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",end_km = '" + endKm + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",delear_product_review_date = '" + delearProductReviewDate + '\'' + 
			",dealer_mobile_no = '" + dealerMobileNo + '\'' + 
			",mobile_exp = '" + mobileExp + '\'' + 
			",id = '" + id + '\'' + 
			",end_km_pic = '" + endKmPic + '\'' + 
			",dealer_demo_product = '" + dealerDemoProduct + '\'' + 
			",start_km_pic = '" + startKmPic + '\'' + 
			",sales_man_id = '" + salesManId + '\'' + 
			",start_km = '" + startKm + '\'' + 
			",other_exp = '" + otherExp + '\'' + 
			",total_km = '" + totalKm + '\'' + 
			",dealer_location = '" + dealerLocation + '\'' + 
			",dinner_exp = '" + dinnerExp + '\'' + 
			",deleted_at = '" + deletedAt + '\'' + 
			",delear_product_review = '" + delearProductReview + '\'' + 
			",dealer_used_crop_product = '" + dealerUsedCropProduct + '\'' + 
			",dealer_name = '" + dealerName + '\'' + 
			",dealer_email = '" + dealerEmail + '\'' + 
			",farmer_delear_selfy_pic = '" + farmerDelearSelfyPic + '\'' + 
			"}";
		}
}