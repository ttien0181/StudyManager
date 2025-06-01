package com.example.studymanager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

// nho dung fragment thi phai la "Fragment()" (class) chu khong phai "Fragment" (interface)
class EditFragment : Fragment() {



    // ghi de phuong thuc onCreateView de tao ra view cho fragment
    override fun onCreateView(
        inflater: LayoutInflater,// tao ra view
        container: ViewGroup?,// chua view
        savedInstanceState: Bundle?// luu tru cac trang thai cua fragment
    ): View? {
        // inflate layout tu xml
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // lay cac view tu layout
        var pos = -1
        val hoTenEditText = view.findViewById<EditText>(R.id.hoTenEditText)
        val mSSVEditText = view.findViewById<EditText>(R.id.mSSVText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val sDTEditText = view.findViewById<EditText>(R.id.sDTEditText)
        val saveButton = view.findViewById<Button>(R.id.saveButton)
        val cancerButton = view.findViewById<Button>(R.id.cancerButton)

        // lay du lieu tu bundle(arguments) va gan vao cac EditText tuong ung
        arguments?.let {
            hoTenEditText.setText(it.getString("ARG_HO_TEN"))// lay du lieu tu bundle, key la ARG_HO_TEN
            mSSVEditText.setText(it.getString("ARG_MSSV"))
            emailEditText.setText(it.getString("ARG_EMAIL"))
            sDTEditText.setText(it.getString("ARG_SDT"))
            pos = it.getInt("ARG_POSITION")
        }

        // set listener cho button save
        saveButton.setOnClickListener {
            val hoTen = hoTenEditText.text.toString()
            val mSSV = mSSVEditText.text.toString()
            val email = emailEditText.text.toString()
            val sdt = sDTEditText.text.toString()
            if(hoTen.isNotEmpty() && mSSV.isNotEmpty() && email.isNotEmpty() && sdt.isNotEmpty()){
                // tao bundle de dua du lieu ve cho activity
                val result = Bundle().apply {
                    putString("ARG_HO_TEN", hoTen)
                    putString("ARG_MSSV", mSSV)
                    putString("ARG_EMAIL", email)
                    putString("ARG_SDT", sdt)
                    putInt("ARG_POSITION", pos)
                }

                // gui du lieu ve cho activity thong qua fragmentManager (FragmentResultAPI)
                parentFragmentManager.setFragmentResult("editStudent", result)
                Log.v("TAG", "setFragmentResult")
                // quay ve fragment truoc do
                findNavController().popBackStack()
            }
        }

        // set listener cho button cancer
        cancerButton.setOnClickListener {
            Toast.makeText(requireContext(), "Cancer", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    // tao instance cho EditFragment
    companion object {
        fun newInstance(hoTen: String, mSSV: String, email: String, sdt: String): EditFragment {
            val editFragment = EditFragment()

            // dua du lieu vao bundle
            val args = Bundle()
            args.putString("ARG_HO_TEN", hoTen)// key, value
            args.putString("ARG_MSSV", mSSV)
            args.putString("ARG_EMAIL", email)
            args.putString("ARG_SDT", sdt)
            editFragment.arguments = args
            return editFragment
        }
    }
}