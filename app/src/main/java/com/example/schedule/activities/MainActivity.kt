package com.example.schedule.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.applandeo.materialcalendarview.CalendarView
import com.example.schedule.R
import com.example.schedule.fragments.*
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


open class MainActivity : AppCompatActivity() {

    private lateinit var drawerResult: Drawer
    private lateinit var toolbar: Toolbar
    private lateinit var mapFragment : MapFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var weatherFragment: WeatherFragment
    private lateinit var calendarFragment: CalendarFragment
    lateinit var selectedDate: String
    private lateinit var dateView: TextView
    private lateinit var backToolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        dateView = findViewById(R.id.main_toolbar_date)
        backToolbar = findViewById(R.id.back_toolbar)
        setSupportActionBar(toolbar)
        initDrawer(toolbar)
        homeFragment = HomeFragment()
        mapFragment = MapFragment()
        weatherFragment = WeatherFragment()
        calendarFragment = CalendarFragment()
        selectedDate = currentDate()
        dateView.text = selectedDate

        supportFragmentManager
            .beginTransaction()
            .add(R.id.activity_main_frame, homeFragment)
            .commit()

    }



    private fun currentDate () : String{
        val date: Date = Calendar.getInstance().time
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        return dateFormat.format(date)
    }



    override fun onBackPressed() {
        if (drawerResult.isDrawerOpen) {
            drawerResult.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }


    fun onClickAdd(view: View){
        homeFragment.onClickAdd(view)
    }

    fun onClickCalendar(view: View){

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_main_frame, calendarFragment)
            .commit()
        toolbar.visibility = View.INVISIBLE

        backToolbar.visibility = View.VISIBLE
        setSupportActionBar(backToolbar)
        backToolbar.setNavigationOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_frame, homeFragment)
                .commit()
            backToolbar.visibility = View.INVISIBLE
            toolbar.visibility = View.VISIBLE
            setSupportActionBar(toolbar)
            drawerResult.setToolbar(this, toolbar)
        }

    }

    fun onClickCalendarToday(view: View){

        calendarFragment.onClickCalendarToday(view)

    }

    fun onClickToday(view: View){
        selectedDate = currentDate()
        dateView.text = selectedDate
    }

    fun onClickSelectDate(view: View){

        dateView.text = calendarFragment.onClickSelectDate(view)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_main_frame, homeFragment)
            .commit()
        backToolbar.visibility = View.INVISIBLE
        toolbar.visibility = View.VISIBLE
        setSupportActionBar(toolbar)
        drawerResult.setToolbar(this, toolbar)
    }


    private fun initDrawer(toolbar: Toolbar){


        val home = PrimaryDrawerItem().withIdentifier(1).withName(R.string.home)
            .withSelected(false).withSelectedColorRes(R.color.green)
            .withSelectedTextColorRes(R.color.black)

        val map = PrimaryDrawerItem().withIdentifier(2).withName(R.string.map)
            .withSelected(false).withSelectedColorRes(R.color.green)
            .withSelectedTextColorRes(R.color.black)

        val weather = PrimaryDrawerItem().withIdentifier(3).withName(R.string.weather)
            .withSelected(false).withSelectedColorRes(R.color.green)
            .withSelectedTextColorRes(R.color.black)


        this.drawerResult = DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.drawer_header)
                .withToolbar(toolbar)
                .addDrawerItems(
                    home,
                    map,
                    weather
                )
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(
                        view: View?,
                        position: Int,
                        drawerItem: IDrawerItem<*>
                    ): Boolean {

                        val toolbarL = findViewById<ConstraintLayout>(R.id.layout_toolbar)
                        when (drawerItem.identifier.toInt()) {

                            1 -> {
                                supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.activity_main_frame, homeFragment)
                                    .commit()
                                toolbarL.visibility = View.VISIBLE
                            }


                            2 -> {

                                supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.activity_main_frame, mapFragment)
                                    .commit()

                                toolbarL.visibility = View.INVISIBLE
                            }

                            3 -> {

                                supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.activity_main_frame, weatherFragment)
                                    .commit()

                                toolbarL.visibility = View.INVISIBLE

                            }

                        }
                        return false
                    }
                }).build()
    }
   /* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.calendar_menu, menu);
        return true
    }
*/
}