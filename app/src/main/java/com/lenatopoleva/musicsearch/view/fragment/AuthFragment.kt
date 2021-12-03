package com.lenatopoleva.musicsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.databinding.FragmentAuthBinding
import com.lenatopoleva.musicsearch.utils.Event
import com.lenatopoleva.musicsearch.utils.ui.BackButtonListener
import com.lenatopoleva.musicsearch.utils.ui.MainActivityAlertDialogFragment
import com.lenatopoleva.musicsearch.utils.ui.TextValidator
import com.lenatopoleva.musicsearch.viewmodel.fragment.AuthViewModel
import org.koin.android.ext.android.getKoin

class AuthFragment: Fragment(), BackButtonListener {

    companion object {
        fun newInstance() = AuthFragment()
    }

    val model: AuthViewModel by lazy {
        ViewModelProvider(this, getKoin().get())[AuthViewModel::class.java]
    }

    private val emailObserver = Observer<Boolean> { changeEmailTextInputLayout(it) }
    private val passwordObserver = Observer<Boolean> { changePasswordTextInputLayout(it) }
    private val userNotRegistratedAlertDialogObserver =
        Observer<Event<String>> { event -> event.getContentIfNotHandled()?.let { showUserNotRegistratedAlerdDialog() }}
    private val errorAlertDialogObserver =
        Observer<Event<String>> { event -> event.getContentIfNotHandled()?.let { showAlertDialog(getString(R.string.error_stub), it) }}

    private var _binding: FragmentAuthBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.emailValidationLiveData.observe(viewLifecycleOwner, emailObserver)
        model.passwordValidationLiveData.observe(viewLifecycleOwner, passwordObserver)
        model.userNotRegistratedAlertDialogLiveData.observe(viewLifecycleOwner, userNotRegistratedAlertDialogObserver)
        model.errorAlertDialogLiveData.observe(viewLifecycleOwner, errorAlertDialogObserver)
    }

    override fun onResume() {
        super.onResume()
        addTextChangedListeners()
        setButtonClickListeners()
    }

    private fun addTextChangedListeners(){
        with(binding){
            email.editText?.let { it.addTextChangedListener(object : TextValidator(it) {
                override fun validate(text: String) {
                    model.validate(text, EMAIL)
                }
            }) }
            password.editText?.let { it.addTextChangedListener(object :TextValidator(it) {
                override fun validate(text: String) {
                    model.validate(text, PASSWORD)
                }
            }) }
        }
    }

    private fun setButtonClickListeners(){
        with(binding){
            loginBtn.setOnClickListener {
                val email = email.editText?.text.toString().trim()
                val password = password.editText?.text.toString().trim()
                model.btnLogInClicked(email, password)
            }
            registrationBtn.setOnClickListener { model.btnRegistrationClicked() }
        }
    }

    private fun showUserNotRegistratedAlerdDialog(){
        showAlertDialog(getString(R.string.error_stub), getString(R.string.user_is_not_registrated))
    }

    private fun showAlertDialog(title: String, message: String) {
        MainActivityAlertDialogFragment.newInstance(title, message).show(childFragmentManager, MainActivityAlertDialogFragment.DIALOG_TAG)
    }

    private fun changeEmailTextInputLayout(isValid: Boolean) {
        if(!isValid){
            binding.email.error = getString(R.string.email_error)
        } else binding.email.error = null
    }

    private fun changePasswordTextInputLayout(isValid: Boolean){
        if(!isValid){
            binding.password.error = getString(R.string.password_error)
        } else binding.password.error = null
    }

    override fun backPressed() = model.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}