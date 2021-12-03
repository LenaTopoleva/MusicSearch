package com.lenatopoleva.musicsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.databinding.FragmentAlbumsBottomSheetBinding
import com.lenatopoleva.musicsearch.model.data.entity.User
import com.lenatopoleva.musicsearch.viewmodel.fragment.AlbumsViewModel
import org.koin.android.ext.android.getKoin

class BottomSheetFragment: BottomSheetDialogFragment() {

    companion object{
        const val BOTTOM_SHEET_TAG = "bottomsheettag"
        fun newInstance() = BottomSheetFragment()
    }

    private var _binding: FragmentAlbumsBottomSheetBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!

    val model: AlbumsViewModel by lazy {
        ViewModelProvider(requireParentFragment(), getKoin().get())[AlbumsViewModel::class.java]
    }

    private val authUserObserver = Observer<User> { setupUi(it) }
    private val userNotFoundObserver = Observer<Boolean> { showError(getString(R.string.user_not_found)) }
    private val bottomSheetErrorObserver = Observer<String?> {
        val message = it ?: getString(R.string.error_stub)
        showError(message)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumsBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.authUserLiveData.observe(viewLifecycleOwner, authUserObserver)
        model.authUserNotFoundLiveData.observe(viewLifecycleOwner, userNotFoundObserver)
        model.bottomSheetErrorLiveData.observe(viewLifecycleOwner, bottomSheetErrorObserver)
    }

    override fun onResume() {
        super.onResume()
        model.getAuthUser()
    }

    private fun setupUi(user: User){
        showUserInfoViews()
        with(binding){
            fullNameTv.text = (user.name + " " + user.surname)
            emailTv.text = user.email
            phoneTv.text = user.phone
            birthdayTv.text = user.birthday
            logoutIv.setOnClickListener { model.logoutBtnClicked(user.email) }
        }
    }

    private fun showError(message: String){
        with(binding){
            userInfoGroup.visibility = View.INVISIBLE
            errorTv.visibility = View.VISIBLE
            errorTv.text = message
        }
    }

    private fun showUserInfoViews(){
        with(binding) {
            if (userInfoGroup.visibility == View.INVISIBLE) {
                userInfoGroup.visibility = View.VISIBLE
                errorTv.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}