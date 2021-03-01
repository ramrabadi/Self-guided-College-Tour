package com.tw.ouguidedtour.Data

class Location {
    // Id of the Location, never seen or used by the user
    private lateinit var id: String

    // The name of the Location(stop), (i.e., the room number or location name)
    private lateinit var name: String

    // The id of the next location
    private lateinit var next_location_id: String

    // The url of the video
    private lateinit var video_url: String

    // The text description of the location
    private lateinit var description: String

    // List of images of this location
    private lateinit var pictures: MutableList<String>

    // The id of the tour the location is apart of
    private lateinit var id_of_tour: String

    //Setters
    fun setId(temp: String) {
        id = temp
    }
    fun setName(temp: String) {
        name = temp
    }
    fun setNextLocationId(temp: String) {
        next_location_id = temp
    }
    fun setVideoUrl(temp: String) {
        video_url = temp
    }
    fun setDescription(temp: String) {
        description = temp
    }
    fun setIdOfTour(temp: String) {
        id_of_tour = temp
    }
    fun setPicture(temp: String) {
        pictures.add(temp)
    }

    //Getters
    fun getId(): String {
        return id
    }
    fun getName(): String {
        return name
    }
    fun getNextLocationId(): String {
        return next_location_id
    }
    fun getVideoUrl(): String {
        return video_url
    }
    fun getDescription(): String {
        return description
    }
    fun getIdOfTour(): String {
        return  id_of_tour
    }
    fun getPictures(): List<String> {
        return pictures
    }
}