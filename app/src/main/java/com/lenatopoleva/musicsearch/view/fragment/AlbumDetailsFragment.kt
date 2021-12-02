package com.lenatopoleva.musicsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.databinding.FragmentAlbumDetailsBinding
import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.data.entity.Album
import com.lenatopoleva.musicsearch.model.imageloader.IImageLoader
import com.lenatopoleva.musicsearch.utils.COLLECTION
import com.lenatopoleva.musicsearch.utils.Event
import com.lenatopoleva.musicsearch.utils.filterFromCollections
import com.lenatopoleva.musicsearch.utils.mapToAlbum
import com.lenatopoleva.musicsearch.utils.network.isOnline
import com.lenatopoleva.musicsearch.utils.ui.BackButtonListener
import com.lenatopoleva.musicsearch.view.adapters.TrackListAdapter
import com.lenatopoleva.musicsearch.view.base.BaseFragment
import com.lenatopoleva.musicsearch.viewmodel.fragment.AlbumDetailsViewModel
import org.koin.android.ext.android.getKoin

class AlbumDetailsFragment: BaseFragment(), BackButtonListener {

    companion object{
        private const val ALBUM_ID = "albumId"
        fun newInstance(albumId: Int) =
            AlbumDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ALBUM_ID, albumId)
                }
            }
    }

    override val model: AlbumDetailsViewModel by lazy {
        ViewModelProvider(this, getKoin().get()).get(AlbumDetailsViewModel::class.java)
    }

    val imageLoader: IImageLoader<ImageView> by lazy {
        getKoin().get()
    }

    private var _binding: FragmentAlbumDetailsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

    private var adapter: TrackListAdapter? = null

    private val loadingObserver = Observer<Boolean> { isLoading -> if(isLoading) showLoader() else hideLoader() }
    private val mediaDataObserver = Observer<List<Media>> { handleData(it) }
    private val noMediaAlertDialogObserver = Observer<Event<String>> { event -> event.getContentIfNotHandled()?.let { showNoMediaAlertDialog(it) }}
    private val errorObserver = Observer<Event<String>> { event -> event.getContentIfNotHandled()?.let { showAlertDialog(getString(
        R.string.error_stub), it) }}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Subscribe on appState changes
        model.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        model.loaderLiveData.observe(viewLifecycleOwner, loadingObserver)
        model.mediaListLiveData.observe(viewLifecycleOwner, mediaDataObserver)
        model.noMediaAlertDialogLiveData.observe(viewLifecycleOwner, noMediaAlertDialogObserver)
    }

    override fun onResume() {
        super.onResume()
        val albumId = this.arguments?.getInt(ALBUM_ID)
        albumId?.let {
            isNetworkAvailable = isOnline(requireActivity().applicationContext)
            model.getData(it, isNetworkAvailable)
        }
    }

    override fun hideLoader() {
        binding.views.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun showLoader() {
        binding.views.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showNoMediaAlertDialog(message: String){
        val mess = if (message == "") getString(R.string.no_media_found_error) else message
        showAlertDialog(getString(R.string.error_stub), mess)
    }

    override fun handleData(data: List<Media>) {
        updateUi(data)
        setDataToAdapter(data)
    }

    private fun updateUi(data: List<Media>){
        val album: Album
        if (data.isNotEmpty() && data.first().wrapperType == COLLECTION) {
            album = data.first().mapToAlbum()
            with(binding){
                albumInfoTv.text = album.collectionName
                artistInfoTv.text = album.artistName
                genreInfoTv.text = album.primaryGenreName
                countryInfoTv.text = album.country
                releaseDateInfoTv.text = album.releaseDate.substring(0, 10)
                imageLoader.loadInto(album.artworkUrl100, artworkIv)
            }
        }
    }

    private fun setDataToAdapter(data: List<Media>){
        if (adapter == null){
            binding.trackListRv.layoutManager = LinearLayoutManager(context)
            binding.trackListRv.adapter = TrackListAdapter(data.filterFromCollections())
        } else adapter!!.setData(data.filterFromCollections())
    }

    override fun backPressed() = model.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}