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
import com.lenatopoleva.musicsearch.utils.Event
import com.lenatopoleva.musicsearch.utils.ui.*
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

    private val regFailObserver = Observer<Event<String>> { event-> event.getContentIfNotHandled()?.let { showAlertDialog(getString(R.string.oops), getString(R.string.reg_error_email_exists)) } }
    private val errorObserver = Observer<Event<String>> { event-> event.getContentIfNotHandled()?.let { showAlertDialog(getString(R.string.error_stub), it) } }
    private val loadingObserver = Observer<Event<String>> { event-> event.getContentIfNotHandled()?.let { requireContext().toast(getString(R.string.saving)) } }

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

        model.regFailAlertDialogLiveData.observe(viewLifecycleOwner, regFailObserver)
        model.errorAlertDialogLiveData.observe(viewLifecycleOwner, errorObserver)
        model.loadingLiveData.observe(viewLifecycleOwner, loadingObserver)
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
                val name = name.editText?.text.toString().trim()
                val surname = surname.editText?.text.toString().trim()
                val age = age.editText?.text.toString().trim()
                val phone = phone.editText?.text.toString().trim()
                val email = email.editText?.text.toString().trim()
                val password = password.editText?.text.toString().trim()
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

    private fun showAlertDialog(title: String, message: String) {
        MainActivityAlertDialogFragment.newInstance(title, message).show(childFragmentManager,
            MainActivityAlertDialogFragment.DIALOG_TAG)
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

