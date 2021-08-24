package com.towrevo.ui.datamodel

import com.Towrevo.ui.datamodel.ImageModel
import java.io.Serializable

data class CreateTripModel(
    var estimated_distance: String,
    var estimated_duration: String,
    var schedule_date_time: String,
    var trip_route: List<Places?>,
    var vehicle_size: String,
    var files: String,
    var estimated_fare: String,
    var trip_type: String,
    var multiple_trip_type: String,
    var base_fare: String,
    var multipletripFiles: MutableList<ImageModel>,
    var triptitle: String,
    var vehicleimage: String,
    var vehiclesizeName: String,
    var multple_trip_type_name: String,
    var delivery_type:String,
    var delivery_sub_type:String


) : Serializable {
    data class Places(
        var place_name: String,
        var lat: String,
        var long: String,
        var address: String

    ) : Serializable {
        constructor() : this(
            "", "",
            "", ""
        )
    }

    constructor() : this(
        "", "", "", arrayListOf()
        , "", "", "", "", "", "", arrayListOf(), "", "", "", "",
        "",""
    )
}