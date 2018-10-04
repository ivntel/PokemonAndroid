package com.example.itelisman.pokemonandroid

import android.location.Location
import android.media.Image
import com.google.android.gms.maps.model.LatLng

class Pokemon{
    var name:String?=null
    var des:String?=null
    var image:Int?=null
    var power:Double?=null
    var location:Location? = null
    var isCaught:Boolean?=false

    constructor(name: String, des: String, image: Int, power: Double, lat: Double, lng: Double) {
        this.name = name
        this.des = des
        this.image = image
        this.power = power
        location = Location(name)
        this.location!!.latitude = lat
        this.location!!.longitude = lng
        this.isCaught = false
    }
}