package com.example.schedule.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.schedule.R
import com.example.schedule.classes.Schedule
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where
import java.util.*

class AddActivity : AppCompatActivity() {

    lateinit var name : EditText
    lateinit var description : EditText
    lateinit var date : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val header = findViewById<TextView>(R.id.back_toolbar_header)
        name = findViewById(R.id.editName)
        description = findViewById(R.id.editDescription)
        date = findViewById(R.id.editTextDate)
        date.inputType = InputType.TYPE_NULL;
        header.text = "Добавить событие"
        onClickDate()
        val toolbar = findViewById<Toolbar>(R.id.back_toolbar)
        setSupportActionBar(toolbar)





        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun onClickAddSchedule(view: View){
        val realmName = "Schedule"
        Realm.init(this)
        val config = RealmConfiguration.Builder().
        allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .name(realmName).build()

        val backgroundThreadRealm = Realm.getInstance(config)

        val schedule = Schedule()
        schedule.name = name.text.toString()
        schedule.description = description.text.toString()
        schedule.date = date.text.toString()
        if (schedule.name != "" && schedule.description != "" && schedule.date != "") {

            backgroundThreadRealm.beginTransaction()
            backgroundThreadRealm.insert(schedule)
            backgroundThreadRealm.commitTransaction()
            finish()
            }

        else{
          val alert =  AlertDialog.Builder(this)
                    .setTitle("Ошибка")
                    .setMessage("Имеются пустые поля!")
                    .setPositiveButton("ОК") { _: DialogInterface, _: Int ->
                    }.create()
            alert.show()
        }

    }

    private fun onClickDate() {

        date.setOnFocusChangeListener { v, hasFocus ->

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

        val picker = DatePickerDialog(this,
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