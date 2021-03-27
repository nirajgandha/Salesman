package com.genetic.salesman.retrofit_api;

import com.genetic.salesman.model.*;
import com.genetic.salesman.model.DashboardResponse;
import com.genetic.salesman.model.LoginResponse;
import com.genetic.salesman.model.OrderDetailResponse;
import com.genetic.salesman.model.OrderListResponse;
import com.genetic.salesman.model.PaymentDetailResponse;
import com.genetic.salesman.model.PaymentListResponse;
import com.genetic.salesman.model.PercentageResponse;
import com.genetic.salesman.model.PlaceOrderResponse;
import com.genetic.salesman.model.ProductCategoryResponse;
import com.genetic.salesman.model.ProductListResponse;
import com.genetic.salesman.model.ProductOptionListResponse;
import com.genetic.salesman.model.SalesmanProfileUpdateResponse;

import org.jetbrains.annotations.NotNull;

import okhttp3.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface APIInterface {

    @FormUrlEncoded
    @POST(ServerConfig.LOGIN_API)
    Call<LoginResponse> loginApi(@Field("phone") String phone,
                                 @Field("device_type") String device_type,
                                 @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST(ServerConfig.OTP_API)
    Call<LoginResponse> verifyOtpApi(@Field("phone") String phone,
                                     @Field("otp") String otp);

    @FormUrlEncoded
    @POST(ServerConfig.GET_PRODUCT_CATEGORY)
    Call<ProductCategoryResponse> getProductCategory(@Field("dealer_id") String dealer_id);

    @FormUrlEncoded
    @POST(ServerConfig.GET_PRODUCT_LIST)
    Call<ProductListResponse> getProductList(@Field("dealer_id") String dealer_id,
                                             @Field("category_id") String category_id);

    @FormUrlEncoded
    @POST(ServerConfig.GET_PRODUCT_OPTION_LIST)
    Call<ProductOptionListResponse> getProductOptionList(@Field("dealer_id") String dealer_id,
                                                         @Field("product_id") String product_id);

    @FormUrlEncoded
    @POST(ServerConfig.GET_ORDER_DETAIL)
    Call<OrderDetailResponse> getOrderDetail(@Field("dealer_id") String dealer_id,
                                             @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST(ServerConfig.GET_ORDER_LIST)
    Call<OrderListResponse> getOrderList(@Field("dealer_id") String dealer_id);

    @FormUrlEncoded
    @POST(ServerConfig.GET_PAYMENT_LIST)
    Call<PaymentListResponse> getPaymentList(@Field("dealer_id") String dealer_id);

    @FormUrlEncoded
    @POST(ServerConfig.GET_PAYMENT_DETAIL)
    Call<PaymentDetailResponse> getPaymentDetail(@Field("dealer_id") String dealer_id,
                                                 @Field("payment_id") String payment_id);

    @FormUrlEncoded
    @POST(ServerConfig.PLACE_ORDER)
    Call<PlaceOrderResponse> placeOrder(@Field("order") String order_json);

    @FormUrlEncoded
    @POST(ServerConfig.GET_TAX_PERCENTAGE)
    Call<PercentageResponse> getTaxPercentage(@Field("dealer_id") String dealer_id);

    @FormUrlEncoded
    @POST(ServerConfig.GET_PROFILE)
    Call<SalesmanProfileResponse> getProfile(@Field("phone") String phone);

    @FormUrlEncoded
    @POST(ServerConfig.GET_DEALER_LIST)
    Call<DealerListResponse> getDealerList(@Field("phone") String phone);

    @FormUrlEncoded
    @POST(ServerConfig.GET_DASHBOARD)
    Call<DashboardResponse> getDashboard(@Field("dealer_id") String dealer_id);

    @Multipart
    @POST(ServerConfig.UPDATE_PROFILE)
    Call<SalesmanProfileUpdateResponse> addSalesman(@Part("first_name") RequestBody first_name,
                     @Part("last_name") RequestBody last_name,
                     @Part("father_name") RequestBody father_name,
                     @Part("mother_name") RequestBody mother_name,
                     @Part("gender") RequestBody gender,
                     @Part("dob") RequestBody dob,
                     @Part("mobileno") RequestBody mobileno,
                     @Part("emergency_no") RequestBody emergency_no,
                     @Part("date_of_joining") RequestBody date_of_joining,
                     @Part("marital_status") RequestBody marital_status,
                     @Part("qualification") RequestBody qualification,
                     @Part("experience") RequestBody experience,
                     @Part("current_address") RequestBody current_address,
                     @Part("permanent_address") RequestBody permanet_address,
                     @Part MultipartBody.Part avatar,
                     @Part MultipartBody.Part resume,
                     @Part MultipartBody.Part joining_letter,
                     @Part MultipartBody.Part other_document);

    @Multipart
    @POST(ServerConfig.ADD_DEALER)
    Call<DealerRegistrationResponse> addDealer(@Part("salesman_id") RequestBody salesman_id,
                   @Part("firm_name") RequestBody firm_name,
                   @Part("dealer_qualification") RequestBody dealer_qualification,
                   @Part("responsible_person_name") RequestBody responsible_person_name,
                   @Part("residential_address") RequestBody residential_address,
                   @Part("mobile_no") RequestBody mobile_no,
                   @Part("whatsapp_no") RequestBody whatsapp_no,
                   @Part("date_of_birth") RequestBody date_of_birth,
                   @Part("date_of_anniversary") RequestBody date_of_anniversary,
                   @Part("year_of_establishment") RequestBody year_of_establishment,
                   @Part("nature_of_firm") RequestBody nature_of_firm,
                   @Part("total_turnover") RequestBody total_turnover,
                   @Part("pesticide") RequestBody pesticide,
                   @Part("fertilizers") RequestBody fertilizers,
                   @Part("pgr_other") RequestBody pgr_other,
                   @Part("ownership_of_shop") RequestBody ownership_of_shop,
                   @Part("other_agency_names") RequestBody other_agency_names,
                   @Part("gst_no") RequestBody gst_no,
                   @Part("adharcard_no") RequestBody adharcard_no,
                   @Part("pan_card_no") RequestBody pan_card_no,
                   @Part("name_of_bank") RequestBody name_of_bank,
                   @Part("bank_ac_no") RequestBody bank_ac_no,
                   @Part("ifsc_code") RequestBody ifsc_code,
                   @Part("insecticide_license_no") RequestBody insecticide_license_no,
                   @Part("insecticide_license_validity") RequestBody insecticide_license_validity,
                   @Part("fertilizer_license_no") RequestBody fertilizer_license_no,
                   @Part("fertilizer_license_validity") RequestBody fertilizer_license_validity,
                   @Part("amount_of_first_cheque") RequestBody amount_of_first_cheque,
                   @Part("amount_of_first_cheque_no") RequestBody amount_of_first_cheque_no,
                   @Part("amount_of_cheque_2") RequestBody amount_of_cheque_2,
                   @Part("amount_of_cheque_no_2") RequestBody amount_of_cheque_no_2,
                   @Part("amount_of_cheque_3") RequestBody amount_of_cheque_3,
                   @Part("amount_of_cheque_no_3") RequestBody amount_of_cheque_no_3,
                   @Part("amount_of_cheque_4") RequestBody amount_of_cheque_4,
                   @Part("amount_of_cheque_no_4") RequestBody amount_of_cheque_no_4,
                   @Part("firm_address") RequestBody firm_address,
                   @Part("firm_state_id") RequestBody firm_state_id,
                   @Part("firm_city_id") RequestBody firm_city_id,
                   @Part("firm_taluka") RequestBody firm_taluka,
                   @Part("firm_zipcode") RequestBody firm_zipcode,
                   @Part("residential_address_1") RequestBody residential_address_1,
                   @Part("residential_state_id") RequestBody residential_state_id,
                   @Part("residential_city_id") RequestBody residential_city_id,
                   @Part("residential_takula") RequestBody residential_takula,
                   @Part("residential_zipcode") RequestBody residential_zipcode,
                   @Part MultipartBody.Part avatar,
                   @Part MultipartBody.Part partnership_memorandum_of_firm_doc ,
                   @Part MultipartBody.Part pesticide_license_doc,
                   @Part MultipartBody.Part fertilizer_license_doc,
                   @Part MultipartBody.Part gst_certificate_doc,
                   @Part MultipartBody.Part adharcard_doc,
                   @Part MultipartBody.Part pancard_doc,
                   @Part MultipartBody.Part electricity_bill_doc,
                   @Part MultipartBody.Part rent_agreement_doc);

    @FormUrlEncoded
    @POST(ServerConfig.GET_STATE_LIST)
    Call<StateListResponse> getStateList(@Field("salesman_id") String salesman_id);

    @FormUrlEncoded
    @POST(ServerConfig.GET_CITY_LIST)
    Call<CityListResponse> getCityList(@Field("salesman_id") String salesman_id,
                                        @Field("state_id") String state_id);
}
