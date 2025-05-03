package com.example.studymanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val studentList: List<StudentModel>,
    val onItemClick: (Int, View) -> Unit
) : RecyclerView.Adapter<StudentAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount() = studentList.size


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = studentList[position]
        holder.hoTen.text = currentItem.hoTen
        holder.mSSV.text = currentItem.mSSV
        holder.email.text = currentItem.email
        holder.sDT.text = currentItem.sDT

        holder.itemView.setOnLongClickListener {
            onItemClick(position, holder.itemView)
            Toast.makeText(holder.itemView.context, "Long click", Toast.LENGTH_SHORT).show()
            true
        }
    }



    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val hoTen = itemView.findViewById<TextView>(R.id.hoTenEditText)
        val mSSV = itemView.findViewById<TextView>(R.id.mSSVText)
        val email = itemView.findViewById<TextView>(R.id.emailText)
        val sDT = itemView.findViewById<TextView>(R.id.sDTText)
    }
}

