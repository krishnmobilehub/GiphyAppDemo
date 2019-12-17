package com.giphytest.bean

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Images {

    /**
     * @return The fixedHeight
     */
    /**
     * @param original The fixed_height
     */
    @SerializedName("original")
    @Expose
    var fixedHeight: Original? = null


    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param fixedHeight
     */
    constructor(fixedHeight: Original) {
        this.fixedHeight = fixedHeight
    }

}
