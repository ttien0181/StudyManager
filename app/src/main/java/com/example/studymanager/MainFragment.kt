package com.example.studymanager

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment() {

    private var actionMode: ActionMode? = null
    private var selectedPosition = -1
    private lateinit var db: SQLiteDatabase
    private lateinit var adapter: StudentAdapter
    private lateinit var viewModel: StudentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v("TAG","onCreateView")
        return inflater.inflate(R.layout.fragment_main,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("TAG","onViewCreated")

        // thong bao rang fragment co menu
        setHasOptionsMenu(true)

        // gan toolbar lam action bar
        val toolbar = view.findViewById<Toolbar>(R.id.main_menu)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)



        // khoi tao viewModel dung de luu du lieu
        viewModel =  ViewModelProvider(this).get(StudentViewModel::class.java)



        // khai bao recyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.listStudentView)



        // gan gia tri cho adapter
        adapter = StudentAdapter(viewModel.studentModelList) { position, view->
            // xu ly khi item duoc click
            showPopUpMenu(position, view)
        }
        // set layout manager cho recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // gan adapter cho recyclerView
        recyclerView.adapter = adapter


        // khoi tao database
        db = requireContext().openOrCreateDatabase("studenDB", MODE_PRIVATE, null)


        // tao bang neu chua ton tai
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    hoTen TEXT,
                    mssv TEXT,
                    email TEXT,
                    sdt TEXT
                );
        """.trimIndent())
        loadStudentsFromDatabase()
        // lang nghe su kien tu EditFragment
        parentFragmentManager.setFragmentResultListener(
            "editStudent", viewLifecycleOwner
        ) { _, result ->
            // Lấy dữ liệu từ Bundle kết quả
            val hoTen = result.getString("ARG_HO_TEN") ?: return@setFragmentResultListener
            val mssv  = result.getString("ARG_MSSV")  ?: return@setFragmentResultListener
            val email = result.getString("ARG_EMAIL") ?: return@setFragmentResultListener
            val sdt   = result.getString("ARG_SDT")   ?: return@setFragmentResultListener
            val pos   = result.getInt("ARG_POSITION") ?: return@setFragmentResultListener

            //  bat dau giao dich voi database
            db.beginTransaction()
            try{
                // tao doi tuong ContentValues de chua du lieu can update
                val values = ContentValues().apply {
                    put("hoTen", hoTen)
                    put("mssv", mssv)
                    put("email", email)
                    put("sdt", sdt)
                }

                if (pos == -1){// neu la them moi
                    val rowId = db.insert("students", null, values)
                    if (rowId > 0 ){// neu them thanh cong
                        viewModel.studentModelList.add(StudentModel(hoTen, mssv, email, sdt))
                        adapter.notifyItemInserted(viewModel.studentModelList.size -1)
                        Toast.makeText(requireContext(), "Them thanh cong", Toast.LENGTH_SHORT).show()
                    }
                    else {// neu them that bai
                        Toast.makeText(requireContext(), "Them that bai", Toast.LENGTH_SHORT).show()
                    }
                }

                else{// neu la sua
                    val rowId = db.update("students", values, "id = ?", arrayOf((pos+1).toString()))
                    if(rowId > 0){// neu sua thanh cong
                        viewModel.studentModelList[pos] = StudentModel(hoTen, mssv, email, sdt)
                        adapter.notifyItemChanged(pos)
                        Toast.makeText(requireContext(), "Sua thanh cong", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(requireContext(), "Sua that bai", Toast.LENGTH_SHORT).show()
                    }
                }
                db.setTransactionSuccessful()
            } catch (e: Exception){
                Log.e("TAG", "Error: ${e.message}")
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                db.endTransaction()
            }
        }
    }

    private fun loadStudentsFromDatabase() {
        viewModel.studentModelList.clear()
        val cursor = db.rawQuery("SELECT * FROM students", null)
        if(cursor.moveToFirst()){
            do{
                val hoTen = cursor.getString(cursor.getColumnIndexOrThrow("hoTen"))
                val mssv = cursor.getString(cursor.getColumnIndexOrThrow("mssv"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val sdt = cursor.getString(cursor.getColumnIndexOrThrow("sdt"))
                viewModel.studentModelList.add(StudentModel(hoTen, mssv, email, sdt))
            }while(cursor.moveToNext())
            cursor.close()
            adapter.notifyDataSetChanged()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                //chuyen sang editFragment ma khong truyen du lieu
                findNavController().navigate(R.id.action_mainFragment_to_editFragment)
                true
            }
            R.id.action_quit ->{
                requireActivity().finish()
                true
            }
            R.id.action_reset -> {
                db.beginTransaction()
                try{
                    db.execSQL("DELETE FROM students")
                    viewModel.studentModelList.clear()
                    adapter.notifyDataSetChanged()
                    db.setTransactionSuccessful()
                    Toast.makeText(requireContext(), "Reset thanh cong", Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Log.e("TAG", "Error: ${e.message}")
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    db.endTransaction()
                }
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        db.close()
        Log.v("TAG", "onDestroyView")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.v("TAG","onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("TAG","onCreate")
    }


    override fun onStart() {
        super.onStart()
        Log.v("TAG","onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.v("TAG", "onStop")
    }

    private fun showPopUpMenu( position: Int, view: View){
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.popup_menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId){
                // xu ly nut edit
                R.id.action_edit -> {
                    // lay thong tin sinh vien hien tai
                    val selectionStudent = viewModel.studentModelList[position]


                    // 2. Chuẩn bị Bundle thủ công
                    val bundle = Bundle().apply {
                        putString("ARG_HO_TEN", selectionStudent.hoTen)
                        putString("ARG_MSSV", selectionStudent.mSSV)
                        putString("ARG_EMAIL", selectionStudent.email)
                        putString("ARG_SDT",   selectionStudent.sDT)
                        putInt("ARG_POSITION", position)
                    }
                    // 3. Navigate sang EditFragment kèm Bundle
                    findNavController().navigate(
                        R.id.action_mainFragment_to_editFragment,
                        bundle
                    )
                    true
                }

                R.id.action_delete -> {
                    db.beginTransaction()
                    try{
                        val rowId = db.delete("students", "mssv = ?", arrayOf(viewModel.studentModelList[position].mSSV))
                        if(rowId > 0){
                            viewModel.studentModelList.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            Toast.makeText(requireContext(), "Xoa thanh cong", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(requireContext(), "Xoa that bai", Toast.LENGTH_SHORT).show()
                        }
                        db.setTransactionSuccessful()
                    } catch (e: Exception){
                        Log.e("TAG", "Error: ${e.message}")
                    } finally {
                        db.endTransaction()
                    }
                    true
                }

                R.id.action_call -> {
                    val phoneNumber = viewModel.studentModelList[position].sDT
                    val dialIntent = Intent(Intent.ACTION_DIAL, "tel:$phoneNumber".toUri())
                    startActivity(dialIntent)
                    true
                }

                R.id.action_email -> {
                    val email = viewModel.studentModelList[position].email
                    val emailIntent = Intent(Intent.ACTION_SENDTO, "mailto:$email".toUri())
                    startActivity(emailIntent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()

    }
}