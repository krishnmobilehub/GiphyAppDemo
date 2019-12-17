package com.giphytest.ui.controller

import com.giphytest.bean.GiphyImageInfo


interface GiphyListView : LoadDataView {

    fun renderGiphyList(giphyImageInfoList: List<GiphyImageInfo>)

    fun viewGiphy(giphyImageInfo: GiphyImageInfo)
}
