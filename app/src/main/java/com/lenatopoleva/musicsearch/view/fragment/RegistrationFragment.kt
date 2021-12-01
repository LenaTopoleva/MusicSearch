package com.lenatopoleva.musicsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.lenatopoleva.musicsearch.R
import com.lenatopoleva.musicsearch.databinding.FragmentRegistrationBinding
import com.lenatopoleva.musicsearch.model.data.RegistrationState
import com.lenatopoleva.musicsearch.utils.ui.AlertDialogFragment
import com.lenatopoleva.musicsearch.utils.ui.BackButtonListener
import com.lenatopoleva.musicsearch.utils.ui.InputHelper
import com.lenatopoleva.musicsearch.utils.ui.TextValidator
import com.lenatopoleva.musicsearch.utils.ui.alertdialog.AuthAlertDialogFragment
import com.lenatopoleva.musicsearch.viewmodel.fragment.RegistrationViewModel
import org.koin.android.ext.android.getKoin
import java.text.SimpleDateFormat
import java.util.*

class RegistrationFragment : Fragment(), BackButtonListener {

    companion object {
        const val DATE_PICKER_TAG = "datepickertag"
        fun newInstance() = RegistrationFragment()
    }

    val model: RegistrationViewModel by lazy {
        ViewModelProvider(this, getKoin().get())[RegistrationViewModel::class.java]
    }

    private val nameObserver = Observer<Boolean> { changeTextInputLayout(it, binding.name, getString(R.string.name_error)) }
    private val surnameObserver = Observer<Boolean> { changeTextInputLayout(it, binding.surname, getString(R.string.surname_error)) }
    private val ageObserver = Observer<Boolean> { changeTextInputLayout(it, binding.age, getString(R.string.year_error)) }
    private val phoneObserver = Observer<Boolean> { changeTextInputLayout(it, binding.phone, getString(R.string.phone_error)) }
    private val emailObserver = Observer<Boolean> { changeTextInputLayout(it, binding.email, getString(R.string.email_error)) }
    private val passwordObserver = Observer<Boolean> { changeTextInputLayout(it, binding.password, getString(R.string.password_error)) }
    private val birthDateObserver = Observer<String> { changeAgeTextInputEditText(it) }

    private val registrationStateObserver = Observer<RegistrationState> { renderData(it)  }

    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
            .build()
    }

    private var _binding: FragmentRegistrationBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding
    get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.nameValidationLiveData.observe(viewLifecycleOwner, nameObserver)
        model.surnameValidationLiveData.observe(viewLifecycleOwner, surnameObserver)
        model.ageValidationLiveData.observe(viewLifecycleOwner, ageObserver)
        model.phoneValidationLiveData.observe(viewLifecycleOwner, phoneObserver)
        model.emailValidationLiveData.observe(viewLifecycleOwner, emailObserver)
        model.passwordValidationLiveData.observe(viewLifecycleOwner, passwordObserver)
        model.birthDateLiveData.observe(viewLifecycleOwner, birthDateObserver)

        model.registrationStateLiveData.observe(viewLifecycleOwner, registrationStateObserver)
    }

    override fun onResume() {
        super.onResume()
        addTextChangedListeners()
        setButtonClickListeners()
    }

    private fun addTextChangedListeners(){
        with(binding){
            name.editText?.let { addValidationListenerTo(it, TextValidator.NAME)}
            surname.editText?.let { addValidationListenerTo(it, TextValidator.SURNAME)}
            age.editText?.let { addValidationListenerTo(it, TextValidator.AGE)}
            phone.editText?.let {
                it.hint = InputHelper.getInstance(it).placeholder()
                addValidationListenerTo(it, TextValidator.PHONE)}
            email.editText?.let { addValidationListenerTo(it, TextValidator.EMAIL)}
            password.editText?.let { addValidationListenerTo(it, TextValidator.PASSWORD)}
        }
    }

    private fun addValidationListenerTo(text: EditText, type: String){
        text.addTextChangedListener(object : TextValidator(text) {
            override fun validate(text: String) {
                model.validate(text, type)
            }
        })
    }

    private fun setButtonClickListeners(){
        with(binding){
            age.setStartIconOnClickListener {
                openDatePicker()
            }
            signUpBtn.setOnClickListener {
                val name = name.editText.toString().trim()
                val surname = surname.editText.toString().trim()
                val age = age.editText.toString().trim()
                val phone = phone.editText.toString().trim()
                val email = email.editText.toString().trim()
                val password = password.editText.toString().trim()
                model.btnSignUpClicked(name, surname, age, phone, email, password)
            }
        }
        datePicker.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            model.datePickerPositiveBtnClicked(sdf.format(datePicker.selection))
        }
    }

    private fun openDatePicker(){
        datePicker.show(childFragmentManager, DATE_PICKER_TAG)
    }


    private fun renderData(regState: RegistrationState?) {
        when(regState){
            is RegistrationState.Success -> {
                if (regState.isRegistrated) model.userIsRegistrated()
                else showAlertDialog(getString(R.string.oops), getString(R.string.registration_failed))
            }
            is RegistrationState.Error -> showAlertDialog(getString(R.string.error_stub),
                regState.error.message ?: "")
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        AuthAlertDialogFragment.newInstance(title, message).show(childFragmentManager,
            AlertDialogFragment.DIALOG_TAG)
    }

    private fun changeTextInputLayout(isValid: Boolean, til: TextInputLayout, errorMessage: String) {
        if(!isValid){
            til.error = errorMessage
        } else til.error = null
    }

    private fun changeAgeTextInputEditText(dateBirth: String) {
        binding.age.editText?.setText(dateBirth)
    }

    override fun backPressed() = model.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

