package com.lenatopoleva.musicsearch.model.data.entity

data class Album
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
     val copyright : String? = "",
     val country : String = "",
     val currency : String = "",
     val releaseDate : String = "",
     val primaryGenreName : String = "",
)
