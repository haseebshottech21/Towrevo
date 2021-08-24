package com.example.demomvvm.model
import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2019 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class LoginBase (

	@SerializedName("code") val code : Int,
	@SerializedName("message") val message : String,
	@SerializedName("result") val result : LoginResult
)
data class LoginResult (

	@SerializedName("general_info") val general_info : GeneralInfo,
	@SerializedName("licence_info") val licence_info : List<LicenceInfo>,
	@SerializedName("induction_info") val induction_info : List<InductionInfo>
)
data class InductionInfo (

	@SerializedName("title") val title : String,
	@SerializedName("date") val date : String,
	@SerializedName("value") val value : String
)
data class GeneralInfo (
	@SerializedName("user_id") val user_id : String,
	@SerializedName("firstname") val firstname : String,
	@SerializedName("lastname") val lastname : String,
	@SerializedName("email") val email : String,
	@SerializedName("country_id") val country_id : Int,
	@SerializedName("phone") val phone : String,
	@SerializedName("profile_image") val profile_image : String,
	@SerializedName("token") val token : String,
	@SerializedName("password") val password : String,
	@SerializedName("address") val address : String,
	@SerializedName("state") val state : String,
	@SerializedName("city") val city : String,
	@SerializedName("country") val country : String,
	@SerializedName("full_name") val full_name : String
)
data class LicenceInfo (

	@SerializedName("title") val title : String,
	@SerializedName("date") val date : String,
	@SerializedName("value") val value : String
)
