package com.lenatopoleva.musicsearch.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lenatopoleva.musicsearch.databinding.ItemAlbumBinding
import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.data.entity.Album
import com.lenatopoleva.musicsearch.model.imageloader.IImageLoader
import com.lenatopoleva.musicsearch.utils.COLLECTION
import com.lenatopoleva.musicsearch.utils.mapToAlbum

class AlbumListAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var data: List<Media>,
    private val imageLoader: IImageLoader<ImageView>
) : RecyclerView.Adapter<AlbumListAdapter.RecyclerItemViewHolder>() {

    fun setData(data: List<Media>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumListAdapter.RecyclerItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAlbumBinding.inflate(inflater, parent, false)
        return RecyclerItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumListAdapter.RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class RecyclerItemViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Media) {
            val album: Album
            if(data.wrapperType == COLLECTION) album = data.mapToAlbum()
            else return
            album.let {
                with(binding) {
                    artistNameTv.text = it.artistName
                    collectionNameTv.text = it.collectionName
                    trackCountTv.text = trackCountTv.text.toString().plus(it.trackCount.toString())
                    imageLoader.loadInto(it.artworkUrl100, artworkIv)
                    root.setOnClickListener{onItemClicked(album.collectionId)}
                }
            }
        }
    }

    private fun onItemClicked(albumId: Int){
        onListItemClickListener.onItemClick(albumId)
    }

    interface OnListItemClickListener {
        fun onItemClick(albumId: Int)

    }
}