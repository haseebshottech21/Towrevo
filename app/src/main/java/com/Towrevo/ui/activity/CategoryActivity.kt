package com.Towrevo.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import com.Towrevo.R
import com.Towrevo.application.MyApp
import com.Towrevo.labels.*
import com.Towrevo.network.PARAM_DEVICE_TYPE
import com.Towrevo.network.PARAM_TOKEN
import com.Towrevo.network.PARAM_USER_ID
import com.Towrevo.network.PARAM_USER_TYPE
import com.Towrevo.preference.PREF_IS_LOGIN
import com.Towrevo.preference.PreferenceHelper
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.adapter.AdapterCategory
import com.Towrevo.ui.adapter.NavigationListAdapter
import com.Towrevo.ui.cms.PrivacyPolicyActivity
import com.Towrevo.ui.cms.TermConditionActivity
import com.Towrevo.ui.datamodel.CategoryDataModel
import com.Towrevo.ui.datamodel.NavigationModel
import com.Towrevo.ui.datamodel.SignInModel
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.CategoryLogoutViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.*
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_category.categoryTV
import kotlinx.android.synthetic.main.activity_category.editIV
import kotlinx.android.synthetic.main.activity_category.mainDrawer
import kotlinx.android.synthetic.main.activity_category.menuIV
import kotlinx.android.synthetic.main.activity_category.nameTV
import kotlinx.android.synthetic.main.activity_category.navLogoutTV
import kotlinx.android.synthetic.main.activity_category.profileDetailLL
import kotlinx.android.synthetic.main.activity_category.userEmailTV
import kotlinx.android.synthetic.main.activity_category.userProfileIV
import kotlinx.android.synthetic.main.popup_category.*
import java.io.IOException
import java.util.*

class CategoryActivity : AppCompatActivity(), View.OnClickListener, LocationListener {
    private lateinit var navigationListAdapter: NavigationListAdapter
    private var navList: MutableList<NavigationModel> = arrayListOf()

    //    private var categoryList: MutableList<String> = arrayListOf()
    private var categoryList: MutableList<CategoryDataModel.Data> = arrayListOf()
    private var selectedList: MutableList<CategoryDataModel.Data> = arrayListOf()
    private var tempCategoryList: MutableList<CategoryDataModel.Data> = arrayListOf()
    private var categoryId = ArrayList<String>()

    private var MonthList: MutableList<CategoryDataModel.Data> = arrayListOf()


    //for get current location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var placename: String = ""
    private var centerLatLng: LatLng? = null
    private var currentLat: String = ""
    private var currentLong: String = ""
    private var currentAddress = ""
    private var currentPlace = ""
    private var sourceLat = ""
    private var sourceLong = ""
    private var address = ""
    private var place_name = ""
    private lateinit var signinData: SignInModel.Data
    //   private var categoryId = ""

    private val categoryLogoutViewModel: CategoryLogoutViewModel by lazy {
        ViewModelProvider(
                this,
                BaseViewModelFactory { CategoryLogoutViewModel(this) }).get(
                CategoryLogoutViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()
        callCategoryApi()
        handlApi()
        setNavigation()
//        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )

//        coursesspinner.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//
//                override fun onItemSelected(
//                    parentview: AdapterView<*>?,
//                    selecteditemview: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    if(position == 0){
//                        return
//                    }
//                    categoryId = MonthList[position].categoryId
////                    toast(categoryId)
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//                }
//            }
//
//    }
    }

    private fun showDialog() {
        showAlertDialog(this@CategoryActivity) {
            setTitle(R.string.app_name)
            setMessage(
                    LabelUtils.getLabel(
                            this@CategoryActivity,
                            ARE_YOU_SURE_WANT_TO_EXIT,
                            R.string.are_you_sure_want_to_exit
                    )
            )
            positiveButton(LabelUtils.getLabel(this@CategoryActivity, YES, R.string.yes)) {
                callLogoutApi()
            }

            negativeButton(LabelUtils.getLabel(this@CategoryActivity, NO, R.string.no)) {
//                toast("no")
            }
        }

    }


    private fun setNavigation() {

        navList.clear()
        navList.add(

            NavigationModel(

                        LabelUtils.getLabel(this, CHANGE_PASSWORD, R.string.change_password)
                )
        )
//        navList.add(
//                NavigationModel(
//
//                        LabelUtils.getLabel(this, MY_INQUIRIES, R.string.my_inquiries)
//                )
//        )
        navList.add(
                NavigationModel(

                        LabelUtils.getLabel(this, ABOUT_US, R.string.about_us)
                )
        )
//        navList.add(
//                NavigationModel(
//
//                        LabelUtils.getLabel(this, TERMS_CONDITION, R.string.term_condition)
//                )
//        )
//        navList.add(
//                NavigationModel(
//
//                        LabelUtils.getLabel(this, PRIVACY_POLICY, R.string.privacy_policy)
//                )
//        )
        navList.add(
                NavigationModel(

                        LabelUtils.getLabel(this, CONTACT_US, R.string.contact_us)
                )
        )
        navList.add(
                NavigationModel(

                        LabelUtils.getLabel(this, FAQ, R.string.faq)
                )
        )

        navigationListAdapter = NavigationListAdapter(this, navList)
        navREC.apply {
            adapter = navigationListAdapter
        }


    }

    private fun setLabel() {
        pickupTV.text = LabelUtils.getLabel(this, PICKUP_LOCATION, R.string.pickup_location)
        categoryTV.text = LabelUtils.getLabel(this, CATEGORY, R.string.category)
        nextBT.text = LabelUtils.getLabel(this, NEXT, R.string.next)
        navLogoutTV.text = LabelUtils.getLabel(this, LOGOUT, R.string.logout)

    }

    private fun setData() {
        MyApp.preflogin[PREF_IS_LOGIN] = true

        nameTV.text = PreferenceHelper.getFirstName()
        userEmailTV.text = PreferenceHelper.getUserEmail()
        Glide.with(this).load(PreferenceHelper.getProfileImage())
                .placeholder(R.drawable.ic_group_image).into(userProfileIV)

        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.GOOGLE_MAP_KEY))
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        PermissionUtils.checkForPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                myCallBack = {
                    if (it) {
                        getLastKnownLocation()

                    }
                })

        menuIV.setOnClickListener(this)
        nextBT.setOnClickListener(this)
        editIV.setOnClickListener(this)
        categoryTV.setOnClickListener(this)
        customerLocationET.setOnClickListener(this)
        profileDetailLL.setOnClickListener(this)
        navLogoutTV.setOnClickListener(this)
//        MonthList.add("")
//        MonthList.add("Xuv")
//        MonthList.add("Mini")
//        MonthList.add("Luxary")
//        MonthList.add("Bus")
//        MonthList.add("Truck")

    }

    private fun handlApi() {
        categoryLogoutViewModel.logOutResponseLiveData.observe(this, androidx.lifecycle.Observer {
            when (it[0].code) {
                SUCCESS -> {
                    Util.logOut(this)
                }
                FAILURE -> {
                    toast(it[0].message)
                    //openActivity(LoginActivity::class.java)
                }
                INVALID_TOKEN -> {
                    openActivity(SignInActivity::class.java)
                    toast(it[0].message)
                }
            }
        })

        categoryLogoutViewModel.categoryDataResponseLiveData.observe(this, androidx.lifecycle.Observer {
            when (it[0].code) {
                SUCCESS -> {
                    categoryList.addAll(it[0].data)

                }
                FAILURE -> {
                    toast(it[0].message)
                }
            }
        })
    }
//        categoryLogoutViewModel.categoryDataResponseLiveData.observe(
//            this,
//            androidx.lifecycle.Observer {
//                when (it[0].code) {
//                    SUCCESS -> {
//                        MonthList.clear()
//                        categoryList.clear()
//                        MonthList.addAll(it[0].data)
//                        MonthList.add(0, CategoryDataModel.Data("0", "select category"))
//                        for (i in MonthList.indices) {
//                            categoryList.add(MonthList[i].categoryName)
//                        }
//                        val monthadapter = ArrayAdapter(
//                            this,
//                            R.layout.item_spinner, categoryList
//                        )
//
////                        monthadapter.setDropDownViewResource(R.layout.item_spinner)
////                        coursesspinner.adapter = monthadapter
//                    }
//                    FAILURE -> {
//                        toast(it[0].message)
//                    }
//                }
//            })

    private fun callLogoutApi() {

        val map: HashMap<String, Any> = HashMap()

        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        map[PARAM_DEVICE_TYPE] = "1"
        categoryLogoutViewModel.callLogoutApi(map)

    }

    private fun callCategoryApi() {

        val map: HashMap<String, Any> = HashMap()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        categoryLogoutViewModel.callCategoryDataApi(map)
    }


    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
//        toast("location")
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        currentLat = location.latitude.toString()
                        currentLong = location.longitude.toString()
                        currentAddress = getlocation()
                        currentPlace = getlocation()

                        MyApp.currentAddress = getlocation()
                        MyApp.currentLat = currentLat
                        MyApp.currentLong = currentLong
                        customerLocationET.setText(currentAddress)
                        MyApp.myCurrentLocation = location
                        //for select default address for search category
                        sourceLat = location.latitude.toString()
                        sourceLong = location.longitude.toString()
                        address = getlocation()
                    }

                }
    }

    private fun getlocation(): String {
        val result = StringBuilder()
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> =
                    geocoder.getFromLocation(currentLat.toDouble(), currentLong.toDouble(), 1)
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                result.append(address.getAddressLine(0))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result.toString()
    }

    fun openDrawer() {
        mainDrawer.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        mainDrawer.closeDrawer(GravityCompat.START)
    }

    private fun isValidDetail(): String {
        var msg = ""

        when {
            selectedList.isEmpty() -> {
                msg = LabelUtils.getLabel(this, PLEASE_SELECT_CATEGORY, R.string.please_select_category)

            }

        }
        return msg
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.menuIV -> {
                mainDrawer.openDrawer(GravityCompat.START)
//                mainDrawer.openDrawer(GravityCompat.START)
            }
            R.id.nextBT -> {
                val msg = isValidDetail()
                if (msg.isEmpty()) {
                    openActivity(TowingCompaniesActivity::class.java) {
                        putString(ADDRESS_CONTAINER, address)
                        putString(LAT_CONTAINER, sourceLat)
                        putString(LONG_CONTAINER, sourceLong)
                        putString(CATEGORY_CONTAINER, TextUtils.join(",", categoryId))
                    }

                } else {

                    toast(msg)
                }
            }
            R.id.categoryTV -> {
                val dialog = Dialog(this)
                //set layout custom
                //set layout custom
                dialog.setContentView(R.layout.popup_category)
                selectedList.clear()

                val rvcaddy =
                        dialog.findViewById<View>(R.id.categoryREC) as RecyclerView

                val adapter = AdapterCategory(this, categoryList, onItemClicked = {
                    if (!tempCategoryList.contains(categoryList[it])) {
                        tempCategoryList.add(categoryList[it])
                    }
                    categoryId.add(categoryList[it].categoryId)
                }, onRemoveItemClick = {
                    if (tempCategoryList.contains(categoryList[it])) {
                        tempCategoryList.remove(categoryList[it])
                    }
                    categoryId.remove(categoryList[it].categoryId)

                }, onItemCheckedCallback = { i: Int, data: CategoryDataModel.Data ->

                    if (data.isChecked!!) {
                        selectedList.add(
                                CategoryDataModel.Data(
                                        data.categoryId,
                                        data.categoryName,
                                        data.isChecked
                                )
                        )
                        Log.e("list", Gson().toJson(selectedList))
                    } else {

                        for (i in selectedList.indices) {
                            if (data.categoryId == selectedList.get(i).categoryId) {
                                selectedList.removeAt(i)
                                break
                            }
                        }
                        Log.e("list1111", Gson().toJson(selectedList))

                    }
                })
                // Add some item here to show the list.
                // Add some item here to show the list.
                rvcaddy.adapter = adapter
                val mLayoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(applicationContext)
                rvcaddy.layoutManager = mLayoutManager
                dialog.continuueBtn.setOnClickListener {
                    /* paramCategoryId=categoryId.joinToString()

  //                    for( )*/
                    var categoryName = arrayListOf<String>()
                    categoryIdTV.text = ""
                    var selectTitle: String = ""
                    for (i in selectedList) {
                        if (selectTitle.equals("")) {
                            selectTitle = i.categoryName
                        } else {
                            selectTitle = selectTitle + ", " + i.categoryName
                        }
                    }

                    categoryId.clear()
                    for (i in selectedList.indices) {

                        categoryId.add(selectedList[i].categoryId)
                        //toast(selectedList[i].categoryId)
                    }
                    categoryIdTV.text = selectTitle
                    dialog.hide()
                }
                dialog.show()


            }
            R.id.customerLocationET -> {
                openActivityForResult(LocationPickerActivity::class.java, LOCATION_PICKER_CODE)
            }

            R.id.profileDetailLL -> {
                openActivity(UserEditProfileActivity::class.java)
            }
            R.id.navLogoutTV -> showDialog()
        }
    }

//    private fun putString(categoryContainer: String, categoryId: ArrayList<String>) {
//
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOCATION_PICKER_CODE && resultCode == RESULT_OK && data != null) {

            address = data.getSerializableExtra(ADDRESS_CONTAINER) as String
            sourceLat = data.getSerializableExtra(LAT_CONTAINER) as String
            sourceLong = data.getSerializableExtra(LONG_CONTAINER) as String

            //categoryId = data.getStringArrayListExtra(CATEGORY_CONTAINER) as String

            place_name = address

            customerLocationET.setText(address)


        }

    }

    override fun onLocationChanged(p0: Location?) {
        MyApp.myCurrentLocation = p0
    }

    public fun openTermConditionPage(view: View) {
        startActivity(Intent(this, TermConditionActivity::class.java));
    }

    public fun openPrivicyPolicyPage(view: View) {
        startActivity(Intent(this, PrivacyPolicyActivity::class.java));
    }
}
