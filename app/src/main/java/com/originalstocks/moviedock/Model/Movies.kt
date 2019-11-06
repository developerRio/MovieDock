package com.originalstocks.moviedock.Model

class Movies(private val position: Int) {

    var year: String? = null
    var tittle: String? = null
    var imageUrl: String? = null
    var releaseDate: String? = null
    var plotDescription: String? = null
    var rank: String? = null
    var rating: String? = null
    var duration: String? = null
    var directors: List<String>? = null
    var genres: List<String>? = null
    var actors: List<String>? = null
}
