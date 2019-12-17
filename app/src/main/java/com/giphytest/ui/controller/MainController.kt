package com.giphytest.ui.controller

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import butterknife.OnClick
import com.bluelinelabs.conductor.RouterTransaction
import com.giphytest.App
import com.giphytest.R
import com.giphytest.bean.GiphyImageInfo
import com.giphytest.bean.GiphyImageInfo_
import com.giphytest.rest.GiphyService
import com.giphytest.ui.adapter.GiphyAdapter
import com.giphytest.ui.adapter.ItemOffsetDecoration
import com.giphytest.ui.base.ActionBarProvider
import com.giphytest.ui.base.BaseController
import io.objectbox.Box
import io.objectbox.query.Query


class MainController : BaseController(), GiphyListView, SwipeRefreshLayout.OnRefreshListener {

    lateinit var rv_users: RecyclerView
    lateinit var rl_progress: RelativeLayout
    lateinit var rl_retry: RelativeLayout
    lateinit var bt_retry: Button
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    lateinit var itemOffsetDecoration: ItemOffsetDecoration
    lateinit var userListPresenter: UserListPresenter
    lateinit var usersAdapter: GiphyAdapter
    lateinit var giphyImageInfoBox: Box<GiphyImageInfo>
    lateinit var giphyImageInfoQuery: Query<GiphyImageInfo>
    lateinit var searchMenuItem: MenuItem
    var searchGifs = "Search Gifs"
    var hasSearched: Boolean = false

    init {
        setHasOptionsMenu(true)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        (activity as ActionBarProvider).supportActionBar().show()
        (activity as ActionBarProvider).supportActionBar().setDisplayHomeAsUpEnabled(false)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.giphylist_controller, container, false)
    }

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout) as SwipeRefreshLayout
        rl_retry = view.findViewById(R.id.rl_retry) as RelativeLayout
        rl_progress = view.findViewById(R.id.rl_progress) as RelativeLayout
        rv_users = view.findViewById(R.id.rv_users) as RecyclerView
        bt_retry = view.findViewById(R.id.bt_retry) as Button

        giphyImageInfoBox = (applicationContext as App).boxStore.boxFor(GiphyImageInfo::class.java)
        giphyImageInfoQuery = giphyImageInfoBox.query().order(GiphyImageInfo_.id).build()
        usersAdapter = GiphyAdapter(giphyImageInfoBox)
        userListPresenter = UserListPresenter(giphyImageInfoBox as Box<GiphyImageInfo>, giphyImageInfoQuery as Query<GiphyImageInfo>)
        itemOffsetDecoration = ItemOffsetDecoration(activity!!, R.dimen.gif_adapter_item_offset)
        this.userListPresenter.setView(this)
        mSwipeRefreshLayout.setOnRefreshListener(this)

        setupRecyclerView()
        this.loadGiphyList()
    }

    override val title: String?
        get() = "Giphy Demo"

    @OnClick(R.id.bt_retry)
    internal fun onRetryButtonClick() {
        this.loadGiphyList()
    }

    private fun loadGiphyList() {
        this.userListPresenter.initialize()
    }

    override fun showLoading() {
        mSwipeRefreshLayout.isRefreshing = true
        this.activity!!.setProgressBarIndeterminateVisibility(true)
    }

    override fun hideLoading() {
        mSwipeRefreshLayout.isRefreshing = false
        this.rl_progress.visibility = View.GONE
        this.activity!!.setProgressBarIndeterminateVisibility(false)
    }

    override fun showRetry() {
        this.rl_retry.visibility = View.VISIBLE
    }

    override fun hideRetry() {
        this.rl_retry.visibility = View.GONE
    }

    override fun showError(message: String) {
        mSwipeRefreshLayout.isRefreshing = true
        this.showToastMessage(message)
    }

    /*  override fun context(): Context {
          return activity
      }*/

    private fun setupRecyclerView() {
        this.usersAdapter.setOnItemClickListener(onItemClickListener)
        if (rv_users != null) {
            this.rv_users.layoutManager = GridLayoutManager(activity, PORTRAIT_COLUMNS)
            this.rv_users.addItemDecoration(itemOffsetDecoration)
            this.rv_users.itemAnimator = DefaultItemAnimator()
            this.rv_users.recycledViewPool.setMaxRecycledViews(0, PORTRAIT_COLUMNS + PORTRAIT_COLUMNS)
            this.rv_users.setHasFixedSize(true)
            this.rv_users.setItemViewCacheSize(GiphyService.DEFAULT_RESULTS_COUNT)
            this.rv_users.isDrawingCacheEnabled = true
            this.rv_users.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            this.rv_users.adapter = usersAdapter
        }
    }

    protected fun showToastMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private val onItemClickListener = object : GiphyAdapter.OnItemClickListener {
        override fun onUserItemClicked(giphyImageInfo: GiphyImageInfo) {
            if (userListPresenter != null && giphyImageInfo != null) {
                userListPresenter.onUserClicked(giphyImageInfo)
            }
        }
    }

    override fun renderGiphyList(giphyImageInfoList: List<GiphyImageInfo>) {
        if (giphyImageInfoList != null) {
            this.usersAdapter.clear()
            mSwipeRefreshLayout.isRefreshing = false
            this.usersAdapter.add(giphyImageInfoList)
        }
    }

    override fun viewGiphy(giphyImageInfo: GiphyImageInfo) {
        val bundle = Bundle()
        bundle.putSerializable(VideoPlayerController.ARG, giphyImageInfo)
        router.pushController(RouterTransaction.with(VideoPlayerController(bundle)))
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_fragment_main, menu)

        searchMenuItem = menu.findItem(R.id.menu_search)

        val searchView = MenuItemCompat.getActionView(searchMenuItem) as SearchView
        searchView.queryHint = searchGifs

        // Set contextual action on search icon click
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // When search is closed, go back to trending results
                if (hasSearched) {
                    // loadGiphyList()

                    hasSearched = false
                }
                return true
            }
        })

        // Query listener for search bar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                // Search on type

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (!TextUtils.isEmpty(query)) {
                    loadSearchImages(query)
                    hasSearched = true
                }
                return false
            }
        })
    }


    private fun loadSearchImages(searchString: String) {
        this.userListPresenter.searchgiphy(searchString)
    }

    override fun onRefresh() {
        this.loadGiphyList()
        searchMenuItem.collapseActionView()
    }

    companion object {

        private val PORTRAIT_COLUMNS = 2
    }
}
