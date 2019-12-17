package com.giphytest.ui.controller

import com.giphytest.Presenter.Presenter
import com.giphytest.bean.Data
import com.giphytest.bean.GiphyImageInfo
import com.giphytest.rest.GiphyResponse
import com.giphytest.rest.GiphyService

import io.objectbox.Box
import io.objectbox.query.Query
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


class UserListPresenter(private val giphyImageInfoBox: Box<GiphyImageInfo>, private val giphyImageInfoQuery: Query<GiphyImageInfo>) : Presenter {

    private var viewListView: GiphyListView? = null
    internal var giphyImageInfoList: MutableList<GiphyImageInfo>? = null

    init {
        giphyImageInfoList = this.giphyImageInfoQuery.find()
    }

    fun setView(view: GiphyListView) {
        this.viewListView = view
    }

    override fun resume() {}

    override fun pause() {}

    override fun destroy() {
        this.viewListView = null
    }

    internal fun initialize() {
        this.loadGiphyList()
    }

    private fun loadGiphyList() {
        this.hideViewRetry()
        this.showViewLoading()
        val execute = this.giphyList

        execute.subscribe({ giphyResponse -> onDataManipulate(giphyResponse) }) {
            this@UserListPresenter.hideViewLoading()
            this@UserListPresenter.showViewRetry()
        }
    }

    internal fun onUserClicked(giphyImageInfo: GiphyImageInfo) {
        this.viewListView!!.viewGiphy(giphyImageInfo)
    }

    private fun showViewLoading() {
        this.viewListView!!.showLoading()
    }

    private fun hideViewLoading() {
        this.viewListView!!.hideLoading()
    }

    private fun showViewRetry() {
        this.viewListView!!.showRetry()
    }

    private fun hideViewRetry() {
        this.viewListView!!.hideRetry()
    }

    private val giphyList: Observable<GiphyResponse>
        get() = GiphyService.getInstance().trendingResults.share()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

    private fun getGiphyBySearchList(searchString: String): Observable<GiphyResponse> {
        return GiphyService.getInstance().getSearchResults(searchString).share()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun searchgiphy(searchString: String) {
        val execute = this.getGiphyBySearchList(searchString)
        execute.subscribe({ giphyResponse ->
            giphyImageInfoList!!.clear()
            for (datum in giphyResponse.data) {
                val id = datum.id
                val url = datum.images!!.fixedHeight!!.url
                val videoUrl = datum.images!!.fixedHeight!!.videoUrl

                val giphyImageInfo = GiphyImageInfo()
                giphyImageInfo.giphyId = id
                giphyImageInfo.url = url
                giphyImageInfo.videoUrl = videoUrl
                giphyImageInfo.totalUpVote = "0"
                giphyImageInfo.totalDownVote = "0"
                giphyImageInfoList!!.add(giphyImageInfo)
            }

            this@UserListPresenter.hideViewLoading()
            this@UserListPresenter.viewListView!!.renderGiphyList(giphyImageInfoList!!)
        }) {
            this@UserListPresenter.hideViewLoading()
            this@UserListPresenter.showViewRetry()
        }
    }

    private fun onDataManipulate(giphyResponse: GiphyResponse) {
        for (datum in giphyResponse.data) {
            val id = datum.id
            val url = datum.images!!.fixedHeight!!.url
            val videoUrl = datum.images!!.fixedHeight!!.videoUrl

            var isGiphyExist = false
            if (giphyImageInfoList != null && giphyImageInfoList!!.size > 0) {
                for (i in giphyImageInfoList!!.indices) {
                    if (id == giphyImageInfoList!![i].giphyId) {
                        isGiphyExist = true
                        break
                    }
                }
            }

            if (!isGiphyExist) {
                val giphyImageInfo = GiphyImageInfo()
                giphyImageInfo.giphyId = id
                giphyImageInfo.url = url
                giphyImageInfo.videoUrl = videoUrl
                giphyImageInfo.totalUpVote = "0"
                giphyImageInfo.totalDownVote = "0"
                giphyImageInfoBox.put(giphyImageInfo)
            }
        }
        giphyImageInfoList = this.giphyImageInfoQuery.find()
        this@UserListPresenter.hideViewLoading()
        this@UserListPresenter.viewListView!!.renderGiphyList(giphyImageInfoList!!)
    }
}
