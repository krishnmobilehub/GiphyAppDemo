package com.giphytest.ui.adapter

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.giphytest.R
import com.giphytest.bean.GiphyImageInfo
import com.giphytest.ui.controller.MainController
import com.giphytest.ui.view.CustomImageView
import io.objectbox.Box
import java.util.*


class GiphyAdapter(private val giphyImageInfoBox: Box<GiphyImageInfo>) : RecyclerView.Adapter<GiphyAdapter.GiphyAdapterViewHolder>() {
    private val data = ArrayList<GiphyImageInfo>()
    private val searchData = ArrayList<GiphyImageInfo>()
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): GiphyAdapterViewHolder {
        return GiphyAdapterViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.giphy_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: GiphyAdapterViewHolder, position: Int) {
        val context = holder.gifImageView!!.context
        val model = getItem(position)

        holder.txtUpVote!!.text = model.totalUpVote
        holder.txtDownVote!!.text = model.totalDownVote

        Glide.with(context)
                .load(model.url)
                .asBitmap()
                .error(R.mipmap.ic_launcher)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        holder.gifImageView.setImageBitmap(resource)
                        holder.gifImageView.visibility = View.VISIBLE
                    }
                })

        holder.itemView.setOnClickListener { onItemClickListener!!.onUserItemClicked(model) }

        holder.btnUpVote!!.setOnClickListener {
            if (model.totalUpVote != null) {
                model.totalUpVote = "" + (Integer.parseInt(model.totalUpVote) + 1)
                holder.txtUpVote!!.text = model.totalUpVote
                giphyImageInfoBox.put(model)
            }
        }

        holder.btnDownVote!!.setOnClickListener {
            if (model.totalDownVote != null) {
                model.totalDownVote = "" + (Integer.parseInt(model.totalDownVote) + 1)
                holder.txtDownVote!!.text = model.totalDownVote
                giphyImageInfoBox.put(model)
            }
        }
    }

    override fun onViewRecycled(holder: GiphyAdapterViewHolder?) {
        super.onViewRecycled(holder)

        Glide.clear(holder!!.gifImageView!!)
        holder.gifImageView!!.setImageDrawable(null)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    val list: List<GiphyImageInfo>
        get() = data

    fun getItem(location: Int): GiphyImageInfo {
        return data[location]
    }

    fun getLocation(`object`: GiphyImageInfo): Int {
        return data.indexOf(`object`)
    }

    fun clear() {
        val size = data.size
        if (size > 0) {
            for (i in 0..size - 1) {
                data.removeAt(0)
            }

            notifyItemRangeRemoved(0, size)
        }
    }

    fun add(giphyImageInfoList: List<GiphyImageInfo>): Boolean {
        val added = data.addAll(giphyImageInfoList)
        notifyDataSetChanged()
        return added
    }

    interface OnItemClickListener {
        fun onUserItemClicked(giphyImageInfo: GiphyImageInfo)
    }

    inner class GiphyAdapterViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {

        var gifImageView: CustomImageView

        var txtUpVote: TextView

        var txtDownVote: TextView

        var btnUpVote: LinearLayout

        var btnDownVote: LinearLayout

        init {
            gifImageView = view.findViewById(R.id.gif_image) as CustomImageView
            txtUpVote = view.findViewById(R.id.txtUpVote) as TextView
            txtDownVote = view.findViewById(R.id.txtDownVote) as TextView
            btnUpVote = view.findViewById(R.id.btnUpVote) as LinearLayout
            btnDownVote = view.findViewById(R.id.btnDownVote) as LinearLayout
        }
    }

    companion object {

        internal val TAG = MainController::class.java.simpleName
        private val GIF_IMAGE_HEIGHT_PIXELS = 128
        private val GIF_IMAGE_WIDTH_PIXELS = GIF_IMAGE_HEIGHT_PIXELS
    }
}
