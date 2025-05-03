package com.example.studymanager

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment


class EditDialogFragment : DialogFragment() {
    private lateinit var hoTenEditText: EditText
    private lateinit var mSSVEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var sDTEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancerButton: Button
    private var listener: onEditDialogListener? = null


    interface onEditDialogListener{
        fun onEditDialog(hoTen: String, mSSV: String, email: String, sdt: String)
    }

    fun setonEditDialogListener(listener: onEditDialogListener){
        this.listener = listener
    }

    companion object{
        private const val ARG_HO_TEN = "hoTen"
        private const val ARG_MSSV = "mssv"
        private const val ARG_EMAIL = "email"
        private const val ARG_SDT = "sdt"

        fun newInstance(hoTen: String, mssv: String, email: String, sdt: String): EditDialogFragment{
            val fragment = EditDialogFragment()
            val args = Bundle()
            args.putString(ARG_HO_TEN, hoTen)
            args.putString(ARG_MSSV, mssv)
            args.putString(ARG_EMAIL, email)
            args.putString(ARG_SDT, sdt)
            fragment.arguments = args
            return fragment
        }

    }




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = layoutInflater.inflate(R.layout.fragment_edit_dialog, null)

        val editHoTen = dialogView.findViewById<EditText>(R.id.hoTenEditText)
        val editMSSV = dialogView.findViewById<EditText>(R.id.mSSVText)
        val editEmail = dialogView.findViewById<EditText>(R.id.emailEditText)
        val editSDT = dialogView.findViewById<EditText>(R.id.sDTEditText)

        arguments?.let {
            editHoTen.setText(it.getString(ARG_HO_TEN))
            editMSSV.setText(it.getString(ARG_MSSV))
            editEmail.setText(it.getString(ARG_EMAIL))
            editSDT.setText(it.getString(ARG_SDT))
        }

        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogView)


        dialog.findViewById<Button>(R.id.saveButton).setOnClickListener {
            val hoTen = editHoTen.text.toString()
            val mssv = editMSSV.text.toString()
            val email = editEmail.text.toString()
            val sdt = editSDT.text.toString()
            if(hoTen.isNotEmpty() && mssv.isNotEmpty() && email.isNotEmpty() && sdt.isNotEmpty()){
                listener?.onEditDialog(hoTen, mssv, email, sdt)
                Toast.makeText(requireContext(), "Save", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.cancerButton).setOnClickListener{
            dialog.dismiss()
        }

        return dialog
    }
}