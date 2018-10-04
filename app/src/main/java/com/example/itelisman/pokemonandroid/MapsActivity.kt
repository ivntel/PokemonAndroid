package com.example.itelisman.pokemonandroid

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var ACCESSLOCATION = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        loadPokemon()
    }

    fun checkPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESSLOCATION)
                return
            }
        }

        getUserLocation()
    }

    fun getUserLocation(){
        Toast.makeText(this, "Access to user location granted", Toast.LENGTH_LONG).show()

        var myLocation = MyLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)

        var myThread = myThread()
        myThread.start()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            ACCESSLOCATION->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

                } else {
                    Toast.makeText(this, "Cannot access your location", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    var location:Location?=null

    //Get user location
    inner class MyLocationListener:LocationListener{

        constructor(){
            location= Location("Start")
            location!!.longitude = -0.0
            location!!.latitude = 0.0
        }
        override fun onLocationChanged(p0: Location?) {
            location=p0
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    var oldLocation:Location?=null
    inner class myThread:Thread{

        constructor():super(){
            oldLocation = Location("Start")
            oldLocation!!.longitude = 0.0
            oldLocation!!.latitude = 0.0
        }

        override fun run() {
            while(true){
                try {

                    if(oldLocation!!.distanceTo(location)==0f){
                        continue
                    }

                    oldLocation = location
                    runOnUiThread {
                        mMap!!.clear()
                    val mario = LatLng(location!!.latitude, location!!.longitude)
                    mMap!!.addMarker(MarkerOptions()
                            .position(mario)
                            .title("Me")
                            .snippet(" Here is my location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(mario, 7f))

                        //show pokemons

                        for (i in 0..listPokemons.size-1){

                            var newPokemon = listPokemons[i]

                            if(newPokemon.isCaught==false){

                                val pokemonLoc = LatLng(newPokemon.location!!.latitude!!, newPokemon.location!!.longitude!!)
                                mMap!!.addMarker(MarkerOptions()
                                        .position(pokemonLoc)
                                        .title(newPokemon.name)
                                        .snippet(newPokemon.des!! + ", Power: " + newPokemon!!.power)
                                        .icon(BitmapDescriptorFactory.fromResource(newPokemon.image!!)))

                                if(location!!.distanceTo(newPokemon.location)<2){
                                    newPokemon.isCaught = true
                                    listPokemons[i] = newPokemon
                                    playerPower += newPokemon.power!!
                                    Toast.makeText(applicationContext, "You caught a new Pokemon your new power is" + playerPower, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }

                    Thread.sleep(1000)
                } catch (ex:Exception){

                }
            }
        }
    }

    var playerPower = 0.0
    var listPokemons = ArrayList<Pokemon>()

    fun loadPokemon(){
        listPokemons.add(Pokemon("Charmander", "He is from Japan", R.drawable.charmander, 55.0, 37.77899, -121.0))
        listPokemons.add(Pokemon("Bulbasaur", "He is from India", R.drawable.bulbasaur, 90.5, 37.79494, -122.0))
        listPokemons.add(Pokemon("Squirtle", "He is from USA", R.drawable.squirtle, 33.5, 37.78166, -120.0))
    }
}
