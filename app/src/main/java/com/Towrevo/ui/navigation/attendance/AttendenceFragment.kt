package com.Towrevo.ui.navigation.attendance



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.Towrevo.R
import com.Towrevo.ui.activity.MultipleImageGridActivity
import com.Towrevo.util.extension.openActivity
import kotlinx.android.synthetic.main.fragment_attendance.*


class AttendenceFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        multipleImageBT.setOnClickListener {activity!!.openActivity(MultipleImageGridActivity::class.java)}
    }

}
