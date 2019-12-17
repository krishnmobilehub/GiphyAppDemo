package com.giphytest.bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Original {

    /**
     * @return The url
     */
    /**
     * @param url The url
     */
    @SerializedName("url")
    @Expose
    var url: String? = null
    /**
     * @return The mp4
     */
    /**
     * @param 'mp4' The mp4
     */
    @SerializedName("mp4")
    @Expose
    var videoUrl: String? = null

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param url
     */
    constructor(url: String) {
        this.url = url
    }
}
