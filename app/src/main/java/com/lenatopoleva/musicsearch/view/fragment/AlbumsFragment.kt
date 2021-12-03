package com.lenatopoleva.musicsearch.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.databinding.FragmentAlbumsBinding
import com.lenatopoleva.musicsearch.model.data.Media
import com.lenatopoleva.musicsearch.model.imageloader.IImageLoader
import com.lenatopoleva.musicsearch.utils.Event
import com.lenatopoleva.musicsearch.utils.network.isOnline
import com.lenatopoleva.musicsearch.utils.ui.BackButtonListener
import com.lenatopoleva.musicsearch.utils.ui.toast
import com.lenatopoleva.musicsearch.view.adapters.AlbumListAdapter
import com.lenatopoleva.musicsearch.view.base.BaseFragment
import com.lenatopoleva.musicsearch.viewmodel.fragment.AlbumsViewModel
import org.koin.android.ext.android.getKoin


class AlbumsFragment(): BaseFragment(), BackButtonListener {

    companion object {
        fun newInstance() = AlbumsFragment()
    }

    override val model: AlbumsViewModel by lazy {
        ViewModelProvider(this, getKoin().get())[AlbumsViewModel::class.java]
    }

    private val imageLoader: IImageLoader<ImageView> by lazy {
        getKoin().get()
    }

    private val loadingObserver = Observer<Boolean> { isLoading -> if(isLoading) showLoader() else hideLoader() }
    private val accountBtnObserver = Observer<Event<Boolean>> { it.getContentIfNotHandled()?.let { showBottomSheet() } }
    private val mediaDataObserver = Observer<List<Media>> { setDataToAdapter(it) }
    private val logoutFailObserver = Observer<Event<String>> { event -> event.getContentIfNotHandled()?.let { showLogoutFailAlertDialog(it) } }
    private val noMediaAlertDialogObserver = Observer<Event<String>> { event -> event.getContentIfNotHandled()?.let { showNoMediaAlertDialog(it) }}
    private val errorObserver = Observer<Event<String>> { event -> event.getContentIfNotHandled()?.let { showAlertDialog(getString(R.string.error_stub), it) }}
    private val logoutProgressObserver = Observer<Event<String>> { event -> event.getContentIfNotHandled()?.let {  requireContext().toast(getString(R.string.loging_out)) }}

    private var _binding: FragmentAlbumsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

    private var adapter: AlbumListAdapter? = null
    private val onListItemClickListener: AlbumListAdapter.OnListItemClickListener =
        object : AlbumListAdapter.OnListItemClickListener {
            override fun onItemClick(albumId: Int) {
                model.albumClicked(albumId)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumsBinding.inflate(inflater, container, false)
        val view = binding.root
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.albums_fragment_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Subscribe on appState changes
        model.isAccountBtnClickedLiveData.observe(viewLifecycleOwner, accountBtnObserver)
        model.loaderLiveData.observe(viewLifecycleOwner, loadingObserver)
        model.mediaListLiveData.observe(viewLifecycleOwner, mediaDataObserver)
        model.noMediaAlertDialogLiveData.observe(viewLifecycleOwner, noMediaAlertDialogObserver)
        model.logoutFailAlertDialogLiveData.observe(viewLifecycleOwner, logoutFailObserver)
        model.errorLiveData.observe(viewLifecycleOwner, errorObserver)
        model.logoutProgressLiveData.observe(viewLifecycleOwner, logoutProgressObserver)
    }

    override fun onResume() {
        super.onResume()
        setOnBtnClickListeners()
        initSearch()
    }

    private fun initSearch(){
        val searchView = binding.topAppBar.menu.findItem(R.id.icon_search)
            .actionView.findViewById<androidx.appcompat.widget.SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    isNetworkAvailable = isOnline(requireActivity().applicationContext)
                    model.getData(query.trim(), isNetworkAvailable)
                }
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean = true
        })
    }

    private fun setOnBtnClickListeners(){
        binding.topAppBar.setNavigationOnClickListener{
            model.accountBtnClicked()
        }
    }

    private fun showBottomSheet(){
        BottomSheetFragment.newInstance().show(childFragmentManager, BottomSheetFragment.BOTTOM_SHEET_TAG )
    }

    override fun hideLoader() {
        binding.progressBar.visibility = View.GONE
        binding.albumsListRv.visibility = View.VISIBLE
    }

    override fun showLoader() {
        binding.progressBar.visibility = View.VISIBLE
        binding.albumsListRv.visibility = View.GONE
    }

    override fun handleData(data: List<Media>) {
        setDataToAdapter(data)
    }

    private fun setDataToAdapter(data: List<Media>){
        if (adapter == null){
            binding.albumsListRv.layoutManager = GridLayoutManager(context, 3)
            binding.albumsListRv.adapter = AlbumListAdapter(onListItemClickListener, data, imageLoader)
        } else adapter!!.setData(data)
    }

   private fun showLogoutFailAlertDialog(message: String){
       val mess = if (message == "") getString(R.string.logout_error) else message
       showAlertDialog(getString(R.string.error_stub), mess)
   }

    private fun showNoMediaAlertDialog(message: String){
        val mess = if (message == "") getString(R.string.no_media_found_error) else message
        showAlertDialog(getString(R.string.error_stub), mess)
    }

    override fun backPressed() =  model.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}