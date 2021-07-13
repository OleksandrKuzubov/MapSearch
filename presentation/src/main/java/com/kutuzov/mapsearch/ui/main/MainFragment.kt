package com.kutuzov.mapsearch.ui.main

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.kutuzov.mapsearch.BuildConfig
import com.kutuzov.mapsearch.R
import com.tomtom.online.sdk.location.LocationUpdateListener
import com.tomtom.online.sdk.map.*
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails


class MainFragment : Fragment(), OnMapReadyCallback, LocationUpdateListener {

    companion object {
        const val SEARCH_RESULT = "SEARCH_RESULT"
    }

    private var markerBuilder: MarkerBuilder? = null
    private var cameraPosition: CameraPosition? = null
    private val viewModel: MainViewModel by navGraphViewModels(R.id.map_search_nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initMap(savedInstanceState)

        val currentBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = currentBackStackEntry?.savedStateHandle

        savedStateHandle?.getLiveData<FuzzySearchDetails>(SEARCH_RESULT)
            ?.observe(currentBackStackEntry, Observer { result ->
                markerBuilder = MarkerBuilder(result.position)
                    .icon(Icon.Factory.fromResources(requireContext(), R.drawable.ic_favourites))
                    .markerBalloon(SimpleMarkerBalloon(result.info))
                    .tag("more information in tag").iconAnchor(MarkerAnchor.Bottom)
                    .decal(true) //By default is false

                cameraPosition = CameraPosition.builder()
                    .focusPosition(result.position)
                    .zoom(10.0)
                    .build()
            })
    }

    private lateinit var tomtomMap: TomtomMap
    private lateinit var mapView: MapView

    private fun initMap(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val keysMap = mapOf(
                ApiKeyType.MAPS_API_KEY to BuildConfig.API_KEY
            )

            val properties = MapProperties.Builder()
                .keys(keysMap)
                .build()

            val mapFragment = MapFragment.newInstance(properties)
            mapView = MapView(requireContext())
            childFragmentManager.beginTransaction()
                .replace(R.id.container, mapFragment)
                .commitNow()

            mapFragment.getAsyncMap(this)
        }
    }


    override fun onMapReady(tomtomMap: TomtomMap) {
        this.tomtomMap = tomtomMap
        if (markerBuilder != null) {
            tomtomMap.removeMarkers()
            tomtomMap.addMarker(markerBuilder)
            cameraPosition?.let { tomtomMap.centerOn(it) }
            markerBuilder = null
            cameraPosition = null
        } else {
            tomtomMap.addLocationUpdateListener(this)
            tomtomMap.isMyLocationEnabled = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
        return when (item.itemId) {
            R.id.map_fragment_search -> {
                toSearchScreen()
                true
            }
            R.id.map_fragment_favorites -> {
                toFavoritesScreen()
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

    private fun toFavoritesScreen() {
        findNavController().navigate(
            MainFragmentDirections.actionMapFragmentToFavorites()
        )
    }
}