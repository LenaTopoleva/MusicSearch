package com.lenatopoleva.musicsearch.model.data.entity

data class Track
    (val wrapperType : String = "",
     val artistId : Int,
     val collectionId : Int,
     val artistName : String,
     val collectionName : String,
     val artistViewUrl : String = "",
     val artworkUrl60 : String = "",
     val artworkUrl100 : String = "",
     val collectionPrice : Double = 0.0,
     val trackCount : Int = 0,
     val country : String = "",
     val currency : String = "",
     val releaseDate : String = "",
     val primaryGenreName : String = "",

     val trackName: String,
     val trackNumber: Int,
     val trackTimeMillis: Long = 0,
     val trackPrice: Double = 0.0
)

