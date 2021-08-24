package com.Towrevo.network

import com.Towrevo.ui.datamodel.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

//    @GET(LOGIN)
//    fun doGetLogin(
//        @Query(EMAIL) email: String,
//        @Query(PASSWORD) password: String,
//        @Query(IS_PHONE) is_phone: String,
//        @Query(DEVICE_TOKEN) device_token: String
//    ): Call<List<LoginBase>>

    @FormUrlEncoded
    @POST(LOGIN)
    fun doGetSignIn(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<SignInModel>>

    @Multipart
    @POST(USER_REGISTER)
    fun doGetUserRegister(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part files: MultipartBody.Part
    ): Call<ArrayList<UserRegisterModel>>

 /*   @Multipart
    @POST(COMPANY_REGISTER)
    fun doGetCompanyRegister(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<CompanyRegisterModel>>*/

    @Multipart
    @POST(COMPANY_REGISTER)
    fun doGetCompanyRegister(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part files: MultipartBody.Part
    ): Call<ArrayList<CompanyRegisterModel>>

    @FormUrlEncoded
    @POST(FORGOT_PASSWORD)
    fun doGetForgotPassword(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<UserRegisterModel>>

    @FormUrlEncoded
    @POST(API_SEND_OTP)
    fun doGetOtp(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<SignInModel>>

    @FormUrlEncoded
    @POST(API_VERIFY_OTP)
    fun doGetVerifyOtp(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<SignInModel>>

    @FormUrlEncoded
    @POST(API_COMPANY_INQUIRY_LIST)
    fun doGetCompanyInquiryList(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<CompanyInquiryListModel>>


    @FormUrlEncoded
    @POST(API_CREATE_PASSWORD)
    fun doGetCreatePassword(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<ChangePasswordModel>>

    @FormUrlEncoded
    @POST(API_UPDATE_PASSWORD)
    fun doGetUpdatePassword(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<ChangePasswordModel>>




    @FormUrlEncoded
    @POST(API_LABEL)
    fun doGetlabel(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<LabelModel>>

    @FormUrlEncoded
    @POST(API_SETTING)
    fun doGetSetting(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<generalModel>>

    @FormUrlEncoded
    @POST(API_LOGOUT)
    fun doGetLogout(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<SignInModel>>

    @FormUrlEncoded
    @POST(API_CATEGORY)
    fun doGetCategory(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<CategoryDataModel>>

    @FormUrlEncoded
    @POST(API_NEARBY_COMPANY)
    fun doGetNearByCompany(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<NearByCompanyModel>>

    @FormUrlEncoded
    @POST(API_VIEWPROFILE)
    fun doGetViewProfile(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<SignInModel>>

    @FormUrlEncoded
    @POST(API_CUSTOMER_DETAILS)
    fun doGetCustomerDetails(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<CustomerDetailsModel>>


    @FormUrlEncoded
    @POST(API_CONTACT)
    fun doGetContact(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<CustomerDetailsModel>>

    @FormUrlEncoded
    @POST(API_COMPANY_DETAILS)
    fun doGetCompanyDetails(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<CustomerInquiryListModel>>

    @FormUrlEncoded
    @POST(API_CAR_TOWS)
    fun doGetCarTows(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<CarTowsModel>>

    @FormUrlEncoded
    @POST(API_CMS)
    fun doGetCms(
        @FieldMap map: HashMap<String, Any>
    ): Call<ArrayList<CmsDataModel>>

    @Multipart
    @POST(API_USER_INQUIRY)
    fun doGetUserInquiry(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part files: MultipartBody.Part
    ): Call<ArrayList<UserInquiryModel>>

    @FormUrlEncoded
    @POST(API_FAQ)
    fun getFaqList(
            @FieldMap hashMap: HashMap<String, Any>
    ): Call<ArrayList<FaqModel>>

    @Multipart
    @POST(API_COMPANY_PROFILE)
    fun doGetCompanyEditProfile(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part files: MultipartBody.Part
    ): Call<ArrayList<SignInModel>>

    @Multipart
    @POST(API_CUSTOMER_PROFILE)
    fun doGetCustomerEditProfile(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part files: MultipartBody.Part
    ): Call<ArrayList<SignInModel>>

}