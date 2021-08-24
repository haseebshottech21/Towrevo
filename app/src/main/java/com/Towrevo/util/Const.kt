package com.Towrevo.util

var LOGIN_TYPE = 0
const val REQUEST_APP_PERMISSIONS = 1001

const val BOTTOM_NAVIGATION_MENU_INDEX = "bottomNavigationMenuIndex"

const val PERMISSIONS_REQUEST_CODE = 100
const val DRAWABLE_RIGHT = 2

const val SPLASH_DELAY: Long = 3000 //3sec
const val IMAGE_CAPTURE_CODE = 1001
const val GALLERY_PICKER_CODE = 1002
const val REQUEST_PERMISSION_SETTING = 1003
const val EDIT_PROFILE_CODE = 1011

const val TOTAL_UPLOAD_IMAGE = 6

const val CUSTOMR_EMAIL = "customer@gmail.com"
const val COMPANY_EMAIL = "company@gmail.com"

//RESPONSE CODE
const val INVALID_TOKEN = -6
const val SUCCESS = 1
const val FAILURE = 0
const val ENTER_OTP_IS_INCORRECT = -8
const val NO_DATA_FOUND = -7
const val EMAIL_ALLREADY_EXISTS = -2
const val NO_INTERNET_FOUND = -10

const val SUCCESS_STRING = "1"
const val FAILURE_STRING = "0"


const val countryCode: String = "+1"


//Header details
const val USERNAME_KEY="username"
const val USERNAME_VALUE="instanttrolleys"
const val PASSWORD_KEY="password"
const val PASSWORD_VALUE="0b8c9977e7da0e76b1b084fed8977dd7"
//userType

const val CUSTOMER = "1"
const val COMPANY = "2"
const val OTP_CONTAINER="OTP"
const val OTP_ENUM_CONTAINER="OTP_ENUM"
const val COMPANAY_DETAIL_CONTAINER="companay_detail_container"
const val CUSTOMER_DETAIL_CONTAINER="customer_detail_container"
const val IMAGE_DETAIL_CONTAINER="image_detail_container"

const val MOBILE_CONTAINER="MOBILE"
const val TOAST_ENABLED = true
const val INVALID_PASSWORD = -2
const val USER_NOT_FOUND = -5
const val VERIFY_YOUR_ACCOUNT = -9
const val MOBILE_NUMBER_ALLREADY_EXISTS = -1
const val VERIFIED="1"
const val NOT_VERIFIED="0"

//data containers


const val ZERO = "0"
const val ONE = "1"
const val TWO = "2"
const val THREE = "3"
const val FOUR = "4"
const val FIVE = "5"
const val SIX = "6"
const val SEVEN = "7"
const val EIGHT = "8"
const val NINE = "9"
const val TEN = "10"
const val ELEVEN = "11"
const val TWELVE = "12"
const val THIRTEEN = "13"
const val FOURTEEN = "14"
const val FIFTEEN = "15"
const val SIXTEEN = "16"
const val SEVENTEEN = "17"

const val sender_text = 1
const val sender_img = 2
const val sender_doc = 3
const val reciver_text = 4
const val reciver_img = 5
const val reciver_doc = 6

const val S3BUCKET_KEY = "AKIAI6KTBB56VKAED2NA"
const val S3BUCKET_SECRET = "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"
const val S3BUCKET_NAME = "taylorsapp"
const val S3POOL_ID = "us-east-1:dd147203-1fde-4531-9b57-7afcd562f941"

//notification
const val CHANNEL_ID = "com.vrin.taylorenggpms"
const val CHANNEL_NAME = "Taylor Notification"

const val IMAGE_URLS = "image_urls"
const val IMAGE_POSITION = "image_position"
const val LEAVES = "leaves"
const val PROJECT = "project"
const val NOTIF_PROJECT = "notif_project"
const val TASK = "task"
const val LICENCE = "license"
const val INDUCTION = "induction"
const val EMERGENCY = "emergency"
const val CHAT = "chat"
const val ALARM = "alarm"
const val USER_ID_CONTAINER = "user_id_container"


/*************** Date Formats ************************/

const val DATE_FORMAT_3 = "yyyy-MM-dd"
//The output will be -: 2018-12-05

const val DATE_FORMAT_4 = "dd-MMMM-yyyy"
//The output will be -: 05-December-2018

const val DATE_FORMAT_5 = "dd/MM/yyyy"
//The output will be -: 05 December 2018

const val DATE_FORMAT_7 = "EEE, MMM d, ''yy"
//The output will be -: Wed, Dec 5, '18

const val DATE_FORMAT_8 = "yyyyMMddHHmmss"
//The Output will be -: 2018-12-05 10:37:43

const val DATE_FORMAT_19 = "dd-MMM-yyyy"
//The output will be -: 05-Dec-2018

const val DATE_FORMAT_20 = "dd MMM, yyyy"
//The output will be -: 05th December 2018


const val LOCATION_PICKER_CODE = 1004

const val ADDRESS_CONTAINER = "address_container"
const val LAT_CONTAINER = "lat_container"
const val LONG_CONTAINER = "long_container"
const val PLACE_NAME_CONTAINER = "place_name_container"
const val PLACE_MODEL_CONTAINER = "place_model_container"
const val CATEGORY_CONTAINER = "cateogory_container"
const val COMPANY_CONTAINER = "company_container"




//Containers
const val SELECTED_CATEGORIES = "selected_categories"
const val TRIP_ID_CONTAINER = "trip_id_container"
const val TRIP_ROUTE_CONTAINER = "trip_route_container"
const val TRIP_MEMBER_CONTAINER = "trip_member_container"
const val IMAGE_INDEX = "image_index"
const val POST_TYPE = "post_type"
const val POST_MODEL_CONTAINER = "post_model_container"
const val TRIP_DETAIL_MODEL_CONTAINER = "trip_detail_model_container"
const val TRIP_NAVIGATION_CONTAINER = "trip_navigation_container"
const val TRIP_LIST_MODEL_CONTAINER = "trip_detail_model_container"
const val REPORT_CONTAINER = "report_container"
const val POST_ID_CONTAINER = "post_id_container"
const val TAGGED_TRIP_NAME = "tagged_trip_name"
const val TAGGED_TRIP_ID = "tagged_trip_id"
const val TAGGED_TRIP_ENUM_CONTAINER = "tagged_trip_id"
const val PROFILE_ENUM_CONTAINER = "profile_enum_container"
const val EXPLORE_TRIP_ENUM_CONTAINER = "explore_trip_enum_container"
const val TRIP_DETAIL_ENUM_CONTAINER = "trip_detail_enum_container"
const val INVITED_USERS_CONTAINER = "invited_users_container"
const val PLACE_CONTAINER = "place_container"
const val PLACE_ID_CONTAINER = "place_id_container"
const val PLACE_IMAGE_CONTAINER = "place_image_container"
const val MEDIA_LIST_CONTAINER = "media_list_container"
const val REPORT_MEDIA_ENABLED_CONTAINER = "report_media_enabled_container"
const val MEDIA_MODEL_CONTAINER = "media_model_container"
const val SEARCH_ACTIVITY_BOOLEAN_CONTAINER = "search_activity_boolean_container"
const val USER_NAME_CONTAINER = "username_container"
const val CREATED_DATE_CONTAINER = "created_date_container"
const val FULL_IMAGE_ENUM_CONTAINER = "full_image_enum_container"
const val ATTACHED_ID_CONTAINER = "attached_id_container"
const val NOTIFICATION_TYPE_CONTAINER = "notification_type_container"
const val GROUP_TYPE_CONTAINER = "notification_type_container"
const val MAPBOX_PLACE_CONTAINER = "mapbox_place_container"
const val REQUEST_COUNT_CONTAINER = "request_count_container"
const val REQUEST_IMAGE_CONTAINER = "request_image_container"
const val GROUP_ID_CONTAINER = "group_id_container"
const val GROUP_MODEL_CONTAINER = "group_model_container"
const val EVENT_ID_CONTAINER = "event_id_container"
const val EVENT_MODEL_CONTAINER = "event_model_container"
const val REPORT_MEDIA_ID_CONTAINER = "report_media_id_container"
const val DEEP_LINK_CONTAINER = "deep_link_container"
const val FRIENDS_ENUM_CONTAINER = "deep_link_container"

/********************************************************/








