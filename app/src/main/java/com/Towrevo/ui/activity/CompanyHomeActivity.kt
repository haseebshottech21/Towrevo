package com.Towrevo.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
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
import com.Towrevo.ui.adapter.AdapterInquiryManagement
import com.Towrevo.ui.adapter.CompanyNavigationAdapter
import com.Towrevo.ui.cms.PrivacyPolicyActivity
import com.Towrevo.ui.cms.TermConditionActivity
import com.Towrevo.ui.datamodel.CompanyInquiryListModel
import com.Towrevo.ui.datamodel.NavigationModel
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.CategoryLogoutViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.*
import kotlinx.android.synthetic.main.activity_company_home.*
import kotlinx.android.synthetic.main.activity_company_home.editIV
import kotlinx.android.synthetic.main.activity_company_home.mainDrawer
import kotlinx.android.synthetic.main.activity_company_home.menuIV
import kotlinx.android.synthetic.main.activity_company_home.nameTV
import kotlinx.android.synthetic.main.activity_company_home.navLogoutTV
import kotlinx.android.synthetic.main.activity_company_home.profileDetailLL
import kotlinx.android.synthetic.main.activity_company_home.userEmailTV
import java.util.HashMap

class CompanyHomeActivity : AppCompatActivity(), View.OnClickListener {
    private var companyList: MutableList<CompanyInquiryListModel.Data> = arrayListOf()
    private var googleMap: GoogleMap? = null
    private var source = ""
    private var address = ""
    private var sourceLat = ""
    private var sourceLong = ""
    private var categoryId = ""
    private lateinit var navigationListAdapter: CompanyNavigationAdapter
    private var navList: MutableList<NavigationModel> = arrayListOf()

//    private var MonthList: MutableList<String> = arrayListOf()


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
        setContentView(R.layout.activity_company_home)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()
        handlApi()
        callCompanyInquiryListApi()
//        setScanlist()
        setNavigation()
//        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        if (intent.hasExtra(ADDRESS_CONTAINER)) {
            address = intent.getStringExtra(ADDRESS_CONTAINER) as String
            sourceLat = intent.getStringExtra(LAT_CONTAINER) as String
            sourceLong = intent.getStringExtra(LONG_CONTAINER) as String
            categoryId = intent.getStringExtra(CATEGORY_CONTAINER) as String


        }

    }

    private fun handlApi() {
        categoryLogoutViewModel.logOutResponseLiveData.observe(this, androidx.lifecycle.Observer {
            when (it[0].code) {
                SUCCESS -> {

                    Util.logOut(this)
                }
//                FAILURE -> {
//                    toast(it[0].message)
//                    //openActivity(LoginActivity::class.java)
//                }
                INVALID_TOKEN -> {
                    openActivity(SignInActivity::class.java)

                    toast(it[0].message)
                }
            }
        })
        categoryLogoutViewModel.companyInquiryListResponseLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                when (it[0].code) {
                    SUCCESS -> {
                        inquiryManagementREC.visible()

                        companyList.addAll(it[0].data)
                        setScanList()

                    }
                    FAILURE -> {
                        toast(it[0].message)
                    }
                    NO_DATA_FOUND -> {
                        noDataFoundTv.visible()
//                        toast(it[0].message)
                    }

                }
            })
    }


    private fun setScanList() {

        val scanListAdapter = AdapterInquiryManagement(this, companyList, callCompanysCallback = {

        }, itemClickedCallback = {
            openActivity(CustomerDetailActivity::class.java) {
                putSerializable(COMPANY_CONTAINER, companyList[it])
                putString(ADDRESS_CONTAINER, address)
                putString(LAT_CONTAINER, sourceLat)
                putString(LONG_CONTAINER, sourceLong)
                putString(CATEGORY_CONTAINER, categoryId)
            }
        })
        inquiryManagementREC.adapter = scanListAdapter
    }

    private fun callLogoutApi() {

        val map: HashMap<String, Any> = HashMap()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        map[PARAM_DEVICE_TYPE] = "1"
        categoryLogoutViewModel.callLogoutApi(map)
    }

    private fun callCompanyInquiryListApi() {

        val map: HashMap<String, Any> = HashMap()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        categoryLogoutViewModel.callCompanyInquiryListApi(map)
    }

//    private fun setScanlist() {
//        firmlist.add(
//            MyInquiriesDataModel(
//                "Car towing Service"
//            )
//        )
//        firmlist.add(
//            MyInquiriesDataModel(
//                "Towing Car"
//            )
//        )
//        firmlist.add(
//            MyInquiriesDataModel(
//                "Tow Truck"
//            )
//        )
//        firmlist.add(
//            MyInquiriesDataModel(
//                "Car towing Service"
//            )
//        )
//        firmlist.add(
//            MyInquiriesDataModel(
//                "Towing Car"
//            )
//        )
//        firmlist.add(
//            MyInquiriesDataModel(
//                "Tow Truck"
//            )
//        )
//        firmlist.add(
//            MyInquiriesDataModel(
//                "Car towing Service"
//            )
//        )
//        firmlist.add(
//            MyInquiriesDataModel(
//                "Car towing Service"
//            )
//        )
//        firmlist.add(
//            MyInquiriesDataModel(
//                "Car towing Service"
//            )
//        )
//        firmlist.add(
//            MyInquiriesDataModel(
//                "Car towing Service"
//            )
//        )
//        val scanListAdapter = AdapterInquiryManagement(this, firmlist)
//        inquiryManagementREC.adapter = scanListAdapter
//    }

    private fun showDialog() {
        showAlertDialog(this@CompanyHomeActivity) {
            setTitle(R.string.app_name)
            setMessage(
                LabelUtils.getLabel(
                    this@CompanyHomeActivity,
                    ARE_YOU_SURE_WANT_TO_EXIT,
                    R.string.are_you_sure_want_to_exit
                )
            )
            positiveButton(LabelUtils.getLabel(this@CompanyHomeActivity, YES, R.string.yes)) {
                callLogoutApi()
            }

            negativeButton(LabelUtils.getLabel(this@CompanyHomeActivity, NO, R.string.no)) {
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
//            NavigationModel(
//                LabelUtils.getLabel(this, INQUIRIE_MANAGEMENT, R.string.inquiries_management)
//            )
//        )
        navList.add(
            NavigationModel(

                LabelUtils.getLabel(this, ABOUT_US, R.string.about_us)
            )
        )
//        navList.add(
//            NavigationModel(
//
//                LabelUtils.getLabel(this, TERMS_CONDITION, R.string.term_condition)
//            )
//        )
//        navList.add(
//            NavigationModel(
//
//                LabelUtils.getLabel(this, PRIVACY_POLICY, R.string.privacy_policy)
//            )
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




        navigationListAdapter = CompanyNavigationAdapter(this, navList)
        navCompanyREC.apply {
            adapter = navigationListAdapter
        }

//        val monthadapter = ArrayAdapter(
//            this,
//            R.layout.item_spinner, MonthList
//        )

//        monthadapter.setDropDownViewResource(R.layout.item_spinner)
//        coursesspinner.adapter = monthadapter
    }

    private fun setLabel() {
        inquiryManagementTV.text =
            LabelUtils.getLabel(this, INQUIRIE_MANAGEMENT, R.string.inquiries_management)


    }

    private fun setData() {
        MyApp.preflogin[PREF_IS_LOGIN] = true

        nameTV.text = PreferenceHelper.getCompanyName()
        userEmailTV.text = PreferenceHelper.getUserEmail()
        Glide.with(this).load(PreferenceHelper.getProfileImage())
            .placeholder(R.drawable.ic_company_image).into(userProfileIV)
        menuIV.setOnClickListener(this)
//        nextBT.setOnClickListener(this)
        editIV.setOnClickListener(this)
        profileDetailLL.setOnClickListener(this)
        navLogoutTV.setOnClickListener(this)
//        MonthList.add("Sedan")
//        MonthList.add("Xuv")
//        MonthList.add("Mini")
//        MonthList.add("Luxary")
//        MonthList.add("Bus")
//        MonthList.add("Truck")

    }

    fun openDrawer() {
        mainDrawer.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        mainDrawer.closeDrawer(GravityCompat.START)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.menuIV -> {
                mainDrawer.openDrawer(GravityCompat.START)
//                mainDrawer.openDrawer(GravityCompat.START)
            }
//            R.id.nextBT -> {openActivity(TowingCompaniesActivity::class.java)}
            R.id.profileDetailLL -> {
                openActivity(CompanyEditProfileActivity::class.java)
            }
            R.id.navLogoutTV -> showDialog()
        }
    }


    override fun onResume() {
        super.onResume()
        Glide.with(this).load(PreferenceHelper.getProfileImage())
            .placeholder(R.drawable.ic_company_image).into(userProfileIV)

    }

    public fun openTermConditionPage(view: View) {
        startActivity(Intent(this, TermConditionActivity::class.java));
    }

    public fun openPrivicyPolicyPage(view: View) {
        startActivity(Intent(this, PrivacyPolicyActivity::class.java));
    }
}