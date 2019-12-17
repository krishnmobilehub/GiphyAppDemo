package com.giphytest.bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Data {

    @SerializedName("id")
    @Expose
    var id: String? = null

    /**
     * @return The images
     */
    /**
     * @param images The images
     */
    @SerializedName("images")
    @Expose
    var images: Images? = null

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    constructor(images: Images) {
        this.images = images
    }
}
