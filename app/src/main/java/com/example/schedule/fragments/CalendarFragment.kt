package com.example.schedule.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.CalendarView
import com.example.schedule.R
import com.example.schedule.classes.Schedule
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment() {

    lateinit var currentDate: String
    private lateinit var results : RealmResults<Schedule>
    lateinit var backgroundThreadRealm : Realm

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val header: TextView? = requireActivity().findViewById(R.id.back_toolbar_header)
        val realmName = "Schedule"
        Realm.init(requireContext())
        val config = RealmConfiguration.Builder().name(realmName).build()
        backgroundThreadRealm  = Realm.getInstance(config)
        results  = backgroundThreadRealm.where<Schedule>().findAll()
        header?.text = "Календарь"
        Thread {

            val calendarView: CalendarView? = view?.findViewById(R.id.calendarView)
            val calendars: ArrayList<Calendar> = ArrayList()
            for (i in results) {

                 val calendar = Calendar.getInstance()
                 calendar.time = SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(i.date)
                 calendars.add(calendar)
                 Log.e(TAG, calendars.size.toString())
            }

            calendarView?.setHighlightedDays(calendars.toList())
            calendarView?.showCurrentMonthPage()

        }.run()

        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    fun onClickCalendarToday(view: View){

        val calendarView = this.view?.findViewById<CalendarView>(R.id.calendarView)
        calendarView?.setDate(Calendar.getInstance())

    }

    fun onClickSelectDate(view: View) : String?{
        val calendarView = this.view?.findViewById<CalendarView>(R.id.calendarView)
        val calendar = calendarView?.firstSelectedDate
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")
        return dateFormat.format(calendar!!.time)
    }
}