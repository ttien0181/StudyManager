package com.example.studymanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val studentList: List<StudentModel>,
    val onItemClick: (Int, View) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        // tao view cho moi student trong recyclerView
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_student_item, parent, false)
        // khoi tao viewholder voi itemView
        return StudentViewHolder(itemView)
    }

    // tra ve so luong student trong list
    override fun getItemCount() = studentList.size


    // gan du lieu cho moi student trong recyclerView
    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        // lay du lieu cua student tai vi tri position
        val currentStudent = studentList[position]

        // gan du lieu vao viewholder
        holder.hoTen.text = currentStudent.hoTen
        holder.mSSV.text = currentStudent.mSSV
        holder.email.text = currentStudent.email
        holder.sDT.text = currentStudent.sDT

        holder.itemView.setOnLongClickListener {
            onItemClick(position, holder.itemView)
//            Toast.makeText(holder.itemView.context, "Long click", Toast.LENGTH_SHORT).show()
            true
        }
    }



    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val hoTen = itemView.findViewById<TextView>(R.id.hoTenEditText)
        val mSSV = itemView.findViewById<TextView>(R.id.mSSVText)
        val email = itemView.findViewById<TextView>(R.id.emailText)
        val sDT = itemView.findViewById<TextView>(R.id.sDTText)
    }
}

