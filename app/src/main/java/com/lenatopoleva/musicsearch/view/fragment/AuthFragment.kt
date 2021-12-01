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
import com.lenatopoleva.musicsearch.model.data.AuthState
import com.lenatopoleva.musicsearch.utils.ui.BackButtonListener
import com.lenatopoleva.musicsearch.utils.ui.AlertDialogFragment
import com.lenatopoleva.musicsearch.utils.ui.TextValidator
import com.lenatopoleva.musicsearch.utils.ui.alertdialog.AuthAlertDialogFragment
import com.lenatopoleva.musicsearch.viewmodel.fragment.AuthViewModel
import org.koin.android.ext.android.getKoin

class AuthFragment: Fragment(), BackButtonListener {

    companion object {
        fun newInstance() = AuthFragment()
    }

    val model: AuthViewModel by lazy {
        ViewModelProvider(this, getKoin().get())[AuthViewModel::class.java]
    }

    private val authStateObserver = Observer<AuthState> { renderData(it)  }
    private val emailObserver = Observer<Boolean> { changeEmailTextInputLayout(it) }
    private val passwordObserver = Observer<Boolean> { changePasswordTextInputLayout(it) }

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
        model.authStateLiveData.observe(viewLifecycleOwner, authStateObserver)
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
                val email = email.editText.toString().trim()
                val password = password.editText.toString().trim()
                model.btnLogInClicked(email, password)
            }
            registrationBtn.setOnClickListener { model.btnRegistrationClicked() }
        }
    }


    private fun renderData(authState: AuthState?) {
        when(authState){
            is AuthState.Success -> {
                val user = authState.data
                user?.let { model.userIsAuth() } ?: showAlertDialog(getString(R.string.oops),
                    getString(R.string.user_is_not_registrated))
            }
            is AuthState.Error -> showAlertDialog(getString(R.string.error_stub),
        authState.error.message ?: "")
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        AuthAlertDialogFragment.newInstance(title, message).show(childFragmentManager, AlertDialogFragment.DIALOG_TAG)
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