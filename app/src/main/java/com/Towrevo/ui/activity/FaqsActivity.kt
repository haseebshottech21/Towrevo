package com.Towrevo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.Towrevo.R
import com.Towrevo.labels.FAQ
import com.Towrevo.labels.LabelUtils
import com.Towrevo.network.PARAM_PAGE
import com.Towrevo.network.PARAM_TOKEN
import com.Towrevo.network.PARAM_USER_ID
import com.Towrevo.network.PARAM_USER_TYPE
import com.Towrevo.preference.PreferenceHelper
import com.Towrevo.ui.adapter.FaqExpandableListAdapter
import com.Towrevo.ui.datamodel.FaqModel
import com.Towrevo.ui.datamodel.FaqModel2
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.FaqViewModel
import com.Towrevo.util.ONE
import com.Towrevo.util.SUCCESS
import com.twoSecure.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_faqs.*
import kotlinx.android.synthetic.main.titlebar_layout.*
import org.jetbrains.anko.toast

class FaqsActivity : BaseActivity() {

    private val faqList by lazy { arrayListOf<FaqModel2>() }
    private var faqExpandableListAdapter: FaqExpandableListAdapter? = null

    private val faqViewModel: FaqViewModel by lazy {
        ViewModelProvider(
                this,
                BaseViewModelFactory { FaqViewModel(this) }).get(
                FaqViewModel::class.java
        )
    }


    override fun getLayoutResourceId(): Int {
        return R.layout.activity_faqs
    }

    override fun main() {

        setLabel()
        class FaqsActivity : AppCompatActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_faqs)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        backIV.setOnClickListener {finish()}
        callFaqApi()
        handelApi()
    }

    private fun setLabel() {
        titleBarTV.text = LabelUtils.getLabel(this, FAQ, R.string.faq)
    }

//    private fun initObjects() {
//
//        faqExpandableListAdapter = FaqExpandableListAdapter(
//            this,
//            faqList
//        )
//        faqExpandableList.setOnGroupExpandListener(object :
//            ExpandableListView.OnGroupExpandListener {
//            var previousGroup = -1
//
//            override fun onGroupExpand(groupPosition: Int) {
//                if (groupPosition != previousGroup)
//                    faqExpandableList.collapseGroup(previousGroup)
//                previousGroup = groupPosition
//            }
//        })
//
//        faqExpandableList.setAdapter(faqExpandableListAdapter)
//
//    }


    private fun callFaqApi() {

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap[PARAM_USER_ID] = PreferenceHelper.getUserID()
        hashMap[PARAM_TOKEN] = PreferenceHelper.getToken()
        hashMap[PARAM_USER_TYPE]=PreferenceHelper.getUserType()
        hashMap[PARAM_PAGE] = ONE
        faqViewModel.callFaqApi(hashMap)

    }

    private fun handelApi() {

        /**
         *Block user handle api response
         */
        faqViewModel.faqLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                   // setFaq(it[0].data[0])
                    for (i in it[0].data[0].indices) {
                        /*  val FaqShareTripList = ArrayList<String>()
                          FaqShareTripList.add(it[0].result[0][i].a)*/

                        faqList.add(
                            FaqModel2(
                                it[0].data[0][i].question,
                                arrayListOf(it[0].data[0][i].answer)
                            )
                        )
                    }
                    initObjects()


                }
//                INACTIVE -> {
//                    toast(
//                            LabelUtils.getLabel(
//                                    this,
//                                    USER_INACTIVE_MSG,
//                                    R.string.inactive_user_message
//                            )
//                    )
//                    Util.logOut(this)
//
//                }
//                TOKEN_EXPIRED -> {
//                    toast(
//                            LabelUtils.getLabel(
//                                    this,
//                                    INVALID_TOKEN,
//                                    R.string.invalid_token
//                            )
//                    )
//                    Util.logOut(this)
//                }
//                FAILURE -> {
//                    toast(
//                            LabelUtils.getLabel(
//                                    this,
//                                    FAILURE_MSG,
//                                    R.string.failure
//                            )
//                    )
//                }
                else -> {
                    toast(it[0].message)
                }
            }

        })

    }
    private fun initObjects() {

        faqExpandableListAdapter = FaqExpandableListAdapter(
            this,
            faqList
        )
        faqExpandableList.setOnGroupExpandListener(object :
            ExpandableListView.OnGroupExpandListener {
            var previousGroup = -1

            override fun onGroupExpand(groupPosition: Int) {
                if (groupPosition != previousGroup)
                    faqExpandableList.collapseGroup(previousGroup)
                previousGroup = groupPosition
            }
        })

        faqExpandableList.setAdapter(faqExpandableListAdapter)

    }

//    private fun initListData() {
//        val FaqShareTripList = ArrayList<String>()
//        FaqShareTripList.add("Eleifend eget suspendisse nec ridiculus dis sociosqu netus lacinia a. Tincidunt senectus adipiscing suspendisse sodales a adipiscing.Imperdiet feugiat lacinia adipiscing conubia in rhoncus consectetur commodo id a fames nisl mus fringilla hendrerit. A egestas quisque cras habitant leo enim ridiculus ac ut ad torquent ligula ligula condimentum libero tristique turpis a velit duis mattis")
//        faqList.add(
//            FaqModel(
//                getString(com.kotlinbaseapplication.R.string.how_can_i_share_my_trips_to_my_friend),
//                FaqShareTripList
//            )
//        )
//        val FaqObtainEssentialList = ArrayList<String>()
//        FaqObtainEssentialList.add("Eleifend eget suspendisse nec ridiculus dis sociosqu netus lacinia a. Tincidunt senectus adipiscing suspendisse sodales a adipiscing.Imperdiet feugiat lacinia adipiscing conubia in rhoncus consectetur commodo id a fames nisl mus fringilla hendrerit. A egestas quisque cras habitant leo enim ridiculus ac ut ad torquent ligula ligula condimentum libero tristique turpis a velit duis mattis")
//        faqList.add(
//            FaqModel(
//                getString(R.string.how_do_i_obtain_essential_trip_information_with_full_details_of_my_trip),
//                FaqObtainEssentialList
//            )
//        )
//        val FaqEssentialDocumentList = ArrayList<String>()
//        FaqEssentialDocumentList.add("Eleifend eget suspendisse nec ridiculus dis sociosqu netus lacinia a. Tincidunt senectus adipiscing suspendisse sodales a adipiscing.Imperdiet feugiat lacinia adipiscing conubia in rhoncus consectetur commodo id a fames nisl mus fringilla hendrerit. A egestas quisque cras habitant leo enim ridiculus ac ut ad torquent ligula ligula condimentum libero tristique turpis a velit duis mattis")
//        faqList.add(
//            FaqModel(
//                getString(R.string.What_essential_documents_do_I_need_to_take),
//                FaqEssentialDocumentList
//            )
//        )
//        val FaqSaphineList = ArrayList<String>()
//        FaqSaphineList.add("Eleifend eget suspendisse nec ridiculus dis sociosqu netus lacinia a. Tincidunt senectus adipiscing suspendisse sodales a adipiscing.Imperdiet feugiat lacinia adipiscing conubia in rhoncus consectetur commodo id a fames nisl mus fringilla hendrerit. A egestas quisque cras habitant leo enim ridiculus ac ut ad torquent ligula ligula condimentum libero tristique turpis a velit duis mattis")
//        faqList.add(
//            FaqModel(
//                getString(R.string.sapien_ultricies_hac_netus_ullamcorper_sapien_tempor_aliquam_ultrices_senectus_ac_venenatis),
//                FaqSaphineList
//            )
//        )
//        val FaqCongueList = ArrayList<String>()
//        FaqCongueList.add("Eleifend eget suspendisse nec ridiculus dis sociosqu netus lacinia a. Tincidunt senectus adipiscing suspendisse sodales a adipiscing.Imperdiet feugiat lacinia adipiscing conubia in rhoncus consectetur commodo id a fames nisl mus fringilla hendrerit. A egestas quisque cras habitant leo enim ridiculus ac ut ad torquent ligula ligula condimentum libero tristique turpis a velit duis mattis")
//        faqList.add(
//            FaqModel(
//                getString(R.string.congue_a_adipiscing_primis_vestibulum_ante_eros),
//                FaqCongueList
//            )
//        )
//        val FaqCongueeList = ArrayList<String>()
//        FaqCongueeList.add("Eleifend eget suspendisse nec ridiculus dis sociosqu netus lacinia a. Tincidunt senectus adipiscing suspendisse sodales a adipiscing.Imperdiet feugiat lacinia adipiscing conubia in rhoncus consectetur commodo id a fames nisl mus fringilla hendrerit. A egestas quisque cras habitant leo enim ridiculus ac ut ad torquent ligula ligula condimentum libero tristique turpis a velit duis mattis")
//        faqList.add(
//            FaqModel(
//                getString(R.string.nunc_porttitor_ac_lorem_fames_morbi_gravida_vestibulum),
//                FaqCongueeList
//            )
//        )
//        val FaqConguueList = ArrayList<String>()
//        FaqConguueList.add("Eleifend eget suspendisse nec ridiculus dis sociosqu netus lacinia a. Tincidunt senectus adipiscing suspendisse sodales a adipiscing.Imperdiet feugiat lacinia adipiscing conubia in rhoncus consectetur commodo id a fames nisl mus fringilla hendrerit. A egestas quisque cras habitant leo enim ridiculus ac ut ad torquent ligula ligula condimentum libero tristique turpis a velit duis mattis")
//        faqList.add(
//            FaqModel(
//                getString(R.string.congue_a_adipiscing_primis_vestibulum_ante_eros),
//                FaqConguueList
//            )
//        )
//        val FaqConggueList = ArrayList<String>()
//        FaqConggueList.add("Eleifend eget suspendisse nec ridiculus dis sociosqu netus lacinia a. Tincidunt senectus adipiscing suspendisse sodales a adipiscing.Imperdiet feugiat lacinia adipiscing conubia in rhoncus consectetur commodo id a fames nisl mus fringilla hendrerit. A egestas quisque cras habitant leo enim ridiculus ac ut ad torquent ligula ligula condimentum libero tristique turpis a velit duis mattis")
//        faqList.add(
//            FaqModel(
//                getString(R.string.nunc_porttitor_ac_lorem_fames_morbi_gravida_vestibulum),
//                FaqConggueList
//            )
//        )
//        val FaqConngueList = ArrayList<String>()
//        FaqConngueList.add("Eleifend eget suspendisse nec ridiculus dis sociosqu netus lacinia a. Tincidunt senectus adipiscing suspendisse sodales a adipiscing.Imperdiet feugiat lacinia adipiscing conubia in rhoncus consectetur commodo id a fames nisl mus fringilla hendrerit. A egestas quisque cras habitant leo enim ridiculus ac ut ad torquent ligula ligula condimentum libero tristique turpis a velit duis mattis")
//        faqList.add(
//            FaqModel(
//                getString(R.string.congue_a_adipiscing_primis_vestibulum_ante_eros),
//                FaqConngueList
//            )
//        )
//
//        // notify the adapter
//        faqExpandableListAdapter!!.notifyDataSetChanged()
//    }

/*
    private fun setFaq(data: List<FaqModel.Data>) {

        faqExpandableListAdapter = FaqExpandableListAdapter(
                this,
                data
        )
        faqExpandableList.setOnGroupExpandListener(object :
                ExpandableListView.OnGroupExpandListener {
            var previousGroup = -1

            override fun onGroupExpand(groupPosition: Int) {
                if (groupPosition != previousGroup)
                    faqExpandableList.collapseGroup(previousGroup)
                previousGroup = groupPosition
            }
        })
    }
*/
}