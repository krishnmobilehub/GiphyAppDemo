package com.giphytest.rest

import com.giphytest.bean.Data
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList


class GiphyResponse {
    /**
     * @return The data
     */
    /**
     * @param data The data
     */
    @SerializedName("data")
    @Expose
    var data: List<Data> = ArrayList()

    constructor() {}

    /**
     * @param data
     */
    constructor(data: List<Data>) {
        this.data = data
    }
}
