package com.example.schedule.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.schedule.R
import com.example.schedule.activities.AddActivity
import com.example.schedule.activities.MainActivity
import com.example.schedule.classes.Schedule
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where
import java.util.*


class HomeFragment : Fragment() {
    lateinit var list :ListView
    lateinit var dialog : Dialog
    lateinit var names :ArrayList<String>
    lateinit var name : EditText
    lateinit var description: EditText
    lateinit var date : EditText
    lateinit var dateField : TextView
    lateinit var currentDate: String
    private lateinit var results : RealmResults<Schedule>
    lateinit var backgroundThreadRealm : Realm
    private var position : Int = 0
    lateinit var emptyList : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        list = view.findViewById(R.id.event_list)
        dateField = requireActivity().findViewById(R.id.main_toolbar_date)
        emptyList = view.findViewById(R.id.empty_message)

        initDialog()
        itemDialog()
        onClickDate()



        return view
    }


    override fun onResume() {
        currentDate = dateField.text.toString()
        val realmName = "Schedule"
        Realm.init(requireContext())
        val config = RealmConfiguration.Builder().name(realmName).build()
        backgroundThreadRealm  = Realm.getInstance(config)
        results  = backgroundThreadRealm.where<Schedule>().equalTo("date", currentDate).findAll()

        if (results.size == 0){
            emptyList.visibility = View.VISIBLE
        }
        else {
            emptyList.visibility = View.INVISIBLE
            names = ArrayList<String>()
            for (i in results) {
                names.add(i.name)
            }

            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.list_item, names)
            list.adapter = arrayAdapter
        }
        super.onResume()
    }


    fun onClickAdd(_view: View){
        val intent  = Intent(requireContext(), AddActivity::class.java)
        startActivity(intent)
    }

    private fun itemDialog(){
        list.onItemClickListener = AdapterView.OnItemClickListener{ _: AdapterView<*>, _: View, i: Int, _: Long ->
            position = i
            name.setText(results[i]?.name)
            description.setText(results[i]?.description)
            date.setText(results[i]?.date)

            dialog.show()
        }
    }

    private fun initDialog(){

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_edit_del)
        date = dialog.findViewById(R.id.editTextDate)
        name = dialog.findViewById(R.id.editName)
        description = dialog.findViewById(R.id.editDescription)
        date.inputType = InputType.TYPE_NULL;

        val acceptButton = dialog.findViewById<Button>(R.id.accept_button)
        val delButton = dialog.findViewById<Button>(R.id.del_button)
        acceptButton.setOnClickListener{
            backgroundThreadRealm.beginTransaction()
            results[position]?.name = name.text.toString()
            results[position]?.description = description.text.toString()
            results[position]?.date = date.text.toString()
            backgroundThreadRealm.commitTransaction()
            dialog.dismiss()
            onResume()
        }
        delButton.setOnClickListener{
            backgroundThreadRealm.beginTransaction()
            results[position]?.deleteFromRealm()
            backgroundThreadRealm.commitTransaction()
            dialog.dismiss()
            onResume()
        }

    }

    private fun onClickDate() {

        date.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                initDatePicker()
            }
        }
        date.setOnClickListener {
            initDatePicker()
        }
    }



    private fun initDatePicker(){
        val calendar: Calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        // date picker dialog

        val picker = DatePickerDialog(requireContext(),
                { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val str =
                            if (dayOfMonth < 10 && monthOfYear < 10) {
                                "0" + dayOfMonth.toString() + "." + "0" + (monthOfYear + 1) + "." + year
                            } else if (dayOfMonth < 10) {
                                "0" + dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year
                            } else if (monthOfYear < 10) {
                                dayOfMonth.toString() + "." + "0" + (monthOfYear + 1) + "." + year
                            } else {
                                dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year
                            }
                    date.setText(str)

                }, year, month, day)
        picker.show()
    }
}