package com.kutuzov.mapsearch.ui.main

import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.kutuzov.mapsearch.BuildConfig
import com.kutuzov.mapsearch.R
import com.tomtom.online.sdk.location.LocationUpdateListener
import com.tomtom.online.sdk.map.*


class MainFragment : Fragment(), OnMapReadyCallback, LocationUpdateListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by navGraphViewModels(R.id.map_search_nav_graph)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initMap(savedInstanceState)
    }

    private lateinit var tomtomMap: TomtomMap
    private lateinit var mapView: MapView

    private fun initMap(savedInstanceState: Bundle?) {
        val keysMap = mapOf(
            ApiKeyType.MAPS_API_KEY to BuildConfig.API_KEY
        )

        val properties = MapProperties.Builder()
            .keys(keysMap)
            .build()

        val mapFragment = MapFragment.newInstance(properties)
        mapView = MapView(requireContext())

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.container, mapFragment)
                .commitNow()
        }

        mapFragment.getAsyncMap(this)
    }


    override fun onMapReady(tomtomMap: TomtomMap) {
        this.tomtomMap = tomtomMap
        tomtomMap.addLocationUpdateListener (this)
        tomtomMap.isMyLocationEnabled = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        tomtomMap.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onHostSaveInstanceState()
    }

    override fun onLocationChanged(location: Location?) {
        tomtomMap.userLocation?.let {
            tomtomMap.centerOnMyLocationWithNorthUp()
            tomtomMap.removeLocationUpdateListener(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.map_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.map_fragment_search -> {
                toSearchScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toSearchScreen() {
        findNavController().navigate(
            MainFragmentDirections.actionMapFragmentToSearchFragment()
        )
    }
}