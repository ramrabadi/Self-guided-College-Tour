package com.tw.ouguidedtour.Data

class Location {
    // Id of the Location, never seen or used by the user
    private var id: String

    // The name of the Location(stop), (i.e., the room number or location name)
    private var name: String

    // The id of the next location
    private var next_location_id: String

    // The url of the video
    private var video_url: String

    // The text description of the location
    private var description: String

    // List of images of this location
    private var picture: String

    // The id of the tour the location is apart of
    private var id_of_tour: String

    private var navigationData: NavigationData

    init {
        id = "None"
        name = "None"
        next_location_id = "None"
        video_url = "None"
        description = "None"
        picture = "None"
        id_of_tour = "None"
        navigationData = NavigationData()
    }
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
        picture = temp
    }
    fun setNavigationData(temp: NavigationData) {
        navigationData = temp
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
    fun getPicture(): String {
        return picture
    }
    fun getNavData(): NavigationData {
        return  navigationData
    }

}