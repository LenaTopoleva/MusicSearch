package com.lenatopoleva.musicsearch.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.databinding.ItemSongBinding
import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.data.entity.Track
import com.lenatopoleva.musicsearch.utils.TRACK
import com.lenatopoleva.musicsearch.utils.mapToTrack

class TrackListAdapter(private var data: List<Media>) : RecyclerView.Adapter<TrackListAdapter.RecyclerItemViewHolder>() {

    fun setData(data: List<Media>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackListAdapter.RecyclerItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSongBinding.inflate(inflater, parent, false)
        return RecyclerItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackListAdapter.RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount() = data.size

    inner class RecyclerItemViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Media, position: Int) {
            val track: Track
            if(data.wrapperType == TRACK) track = data.mapToTrack()
            else return
            track.let {
                with(binding) {
                    trackNumberTv.text = it.trackNumber.toString()
                    trackNameTv.text = it.trackName
                    if (position % 2 == 0) binding.root.setBackgroundColor(binding.root.context.getColor(R.color.grey))
                    else binding.root.setBackgroundColor(binding.root.context.getColor(R.color.white))
                }
            }
        }
    }

}