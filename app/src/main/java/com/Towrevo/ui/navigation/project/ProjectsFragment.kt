package com.Towrevo.ui.navigation.project


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_projects.*
import android.content.Intent
import android.net.Uri
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.Towrevo.R
import kotlinx.android.synthetic.main.bottom_sheet.*


class ProjectsFragment : Fragment() {
    lateinit var bottomView: View
    lateinit var bottomSheetDialog: BottomSheetDialog
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setBottomView()
        shareBT.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/jpeg"

            share.putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse("file:///sdcard/DCIM/Camera/myPic.jpg")
            )

            startActivity(Intent.createChooser(share, "Share Image"))
        }
        bottomSheetBT.setOnClickListener {
            bottomSheetDialog.show()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.Towrevo.R.layout.fragment_projects, container, false)
    }
    private fun setBottomView() {

        bottomView = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet, null)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomView)

        bottomSheetDialog.closeBT.setOnClickListener {bottomSheetDialog.hide()}
    }
}
