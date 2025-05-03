package com.example.studymanager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {

    private var actionMode: ActionMode? = null
    private var selectedPosition = -1
    private lateinit var adapter: StudentAdapter
    private val studentModelList = mutableListOf<StudentModel>()

//
//    val actionModeCallBack = object : ActionMode.Callback {
//        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
//            menuInflater.inflate(R.menu.context_menu, menu)
//            return true
//        }
//
//        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
//            return false
//        }
//
//        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
//            return when (item?.itemId) {
//                R.id.action_edit -> {
//                    val selectedStudent = studentModelList[selectedPosition]
//                    val editDialog = EditDialogFragment.newInstance(
//                        selectedStudent.hoTen,
//                        selectedStudent.mSSV,
//                        selectedStudent.email,
//                        selectedStudent.sDT
//                    )
//
//                    editDialog.setonEditDialogListener(object : EditDialogFragment.onEditDialogListener{
//                        override fun onEditDialog(hoTen: String, mSSV: String, email: String, sdt: String) {
//                            Toast.makeText(this@MainActivity, "Edit", Toast.LENGTH_SHORT).show()
//                            studentModelList[selectedPosition] = StudentModel(hoTen, mSSV, email, sdt)
//                            adapter.notifyItemChanged(selectedPosition)
//                            actionMode?.finish()
//                        }
//
//                    })
//                    editDialog.show(supportFragmentManager, "editDialog")
//                    true
//
//                }
//
//
//                R.id.action_call -> {
//                    val dialIntent = Intent(Intent.ACTION_DIAL)
//                    if (dialIntent.resolveActivity(packageManager) != null) {
//                        startActivity(dialIntent)
//                    } else {
//                        Toast.makeText(this@MainActivity, "Dialer app not found", Toast.LENGTH_SHORT).show()
//                    }
//                    true
//                }
//
//                R.id.action_delete -> {
//                    studentModelList.removeAt(selectedPosition)
//                    adapter.notifyItemRemoved(selectedPosition)
//                    actionMode?.finish()
//                    true
//                }
//                else ->  false
//            }
//        }
//
//        override fun onDestroyActionMode(mode: ActionMode?) {
//            actionMode = null
//            selectedPosition = -1
//        }
//    }

    private fun showPopUpMenu( position: Int, view: View){
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.popup_menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.action_edit -> {
                    val selectionStudent = studentModelList[position]
                    val editDialog = EditDialogFragment.newInstance(
                        selectionStudent.hoTen,
                        selectionStudent.mSSV,
                        selectionStudent.email,
                        selectionStudent.sDT
                    )
                    editDialog.setonEditDialogListener(object : EditDialogFragment.onEditDialogListener{
                        override fun onEditDialog(hoTen: String, mSSV: String, email: String, sdt: String) {
                            Toast.makeText(this@MainActivity, "Edit", Toast.LENGTH_SHORT).show()
                            studentModelList[position] = StudentModel(hoTen, mSSV, email, sdt)
                            adapter.notifyItemChanged(position)
                        }
                    })
                    editDialog.show(supportFragmentManager, "editDialog")
                    true
                }

                R.id.action_delete -> {
                    studentModelList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    true
                }

                R.id.action_call -> {
                    val phoneNumber = studentModelList[position].sDT
                    val dialIntent = Intent(Intent.ACTION_DIAL, "tel:$phoneNumber".toUri())
                    startActivity(dialIntent)
                    true
                }

                R.id.action_email -> {
                    val email = studentModelList[position].email
                    val emailIntent = Intent(Intent.ACTION_SENDTO, "mailto:$email".toUri())
                    startActivity(emailIntent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.main_menu))


        studentModelList.addAll(
            listOf(
                StudentModel("Hoang Anh Tu", "20220055", "tu.ha220055@sis.hust.edu.vn", "0123456789"),
                StudentModel("Hoang Van A", "20220056", "a.ha220056@sis.hust.edu.vn", "0123456789"),
                StudentModel("Hoang Van B", "20220057", "b.ha220057@sis.hust.edu.vn", "0123456789"),
                StudentModel("Hoang Van C", "20220058", "c.ha220058@sis.hust.edu.vn", "0123456789"),
                StudentModel("Hoang Van D", "20220059", "d.ha220059@sis.hust.edu.vn", "0123456789"),
                StudentModel("Hoang Van E", "20220060", "e.ha220060@sis.hust.edu.vn", "0123456789"),
                StudentModel("Hoang Van F", "20220061", "f.ha220061@sis.hust.edu.vn", "0123456789"),
                StudentModel("Hoang Van G", "20220062", "g.ha220062@sis.hust.edu.vn", "0123456789")
            )
        )

        val recyclerView = findViewById<RecyclerView>(R.id.listStudentView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(studentModelList) { position, view->
            showPopUpMenu(position, view)
        }
        recyclerView.adapter = adapter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                val editDialog = EditDialogFragment.newInstance(
                    "",
                    "",
                    "",
                    ""
                )
                editDialog.setonEditDialogListener(object : EditDialogFragment.onEditDialogListener {
                    override fun onEditDialog(hoTen: String, mSSV: String, email: String, sdt: String) {
                        val studentModel = StudentModel(hoTen, mSSV, email, sdt)
                        studentModelList.add(studentModel)
                        adapter.notifyItemInserted(studentModelList.size - 1)
                    }
                })
                editDialog.show(supportFragmentManager, "addDialog")
                true
            }
            else -> false
        }
    }
}