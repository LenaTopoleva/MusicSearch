package com.lenatopoleva.musicsearch.model.data

data class Media
    (val wrapperType : String,
     val artistId : Int = 0,
     val collectionId : Int,
     val artistName : String,
     val collectionName : String,
     val artistViewUrl : String = "",
     val artworkUrl60 : String = "",
     val artworkUrl100 : String,
     val collectionPrice : Double = 0.0,
     val trackCount : Int = 0,
     val country : String = "",
     val currency : String = "",
     val releaseDate : String = "",
     val primaryGenreName : String = "",
     val copyright : String? = "",

     val trackName: String? = "",
     val trackNumber: Int? = 0,
     val trackTimeMillis: Long? = 0,
     val trackPrice: Double? = 0.0
)