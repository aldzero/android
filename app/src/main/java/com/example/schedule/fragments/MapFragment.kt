package com.example.schedule.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.schedule.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions



internal class MapFragment : Fragment() {

    private lateinit var mMap: GoogleMap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap : GoogleMap ->
            mMap = googleMap

            // Add a marker in Sydney and move the camera
            val krasnoyarsk = LatLng(55.994361, 92.797707)
            mMap.addMarker(MarkerOptions()
                .position(krasnoyarsk)
                .title("Marker in Krasnoyarsk"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(krasnoyarsk))
        }


        return view
    }



}

