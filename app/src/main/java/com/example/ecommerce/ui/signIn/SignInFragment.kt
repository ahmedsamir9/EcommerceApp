package com.example.ecommerce.ui.signIn

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.SignInFragmentBinding
import com.example.ecommerce.model.User
import com.example.ecommerce.utils.UiDisapperAndAppearInActivity
import com.example.ecommerce.utils.isVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


@AndroidEntryPoint
class SignInFragment : Fragment() ,View.OnClickListener{
    private lateinit var uiDisapperAndAppearInActivity: UiDisapperAndAppearInActivity
    private val viewModel: SignInViewModel by viewModels()
    private lateinit var signInFragmentBinding: SignInFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
      signInFragmentBinding  =DataBindingUtil.inflate(inflater, R.layout.sign_in_fragment, container, false)
        signInFragmentBinding.signBrithDay.setOnClickListener(this)
        signInFragmentBinding.signBtn.setOnClickListener(this)
        return signInFragmentBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiDisapperAndAppearInActivity =context as UiDisapperAndAppearInActivity
        uiDisapperAndAppearInActivity.hideNav()
        uiDisapperAndAppearInActivity.hideToolBar()
    }

    override fun onStart() {
        super.onStart()
        subscribeToLiveData()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.sign_btn -> {
                vaildation()
            }
            R.id.sign_BrithDay -> {

                val cal: Calendar = Calendar.getInstance()
                val year = cal.get(Calendar.YEAR);
                val month = cal.get(Calendar.MONTH);
                val day = cal.get(Calendar.DAY_OF_MONTH);
                val mDateSetListener = OnDateSetListener { datePicker, year, month, day ->
                    var month = month
                    month = month + 1

                    val date = "$day/$month/$year"
                    signInFragmentBinding.signBrithDay.setText(date)
                }
                val dialog = DatePickerDialog(
                        requireContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        }

    }
    private fun subscribeToLiveData(){
        viewModel.message.observe(viewLifecycleOwner,{
            when(it){
                "Done"->{
                    signInFragmentBinding.proSign.isVisible(false)
                    val action= SignInFragmentDirections.actionSignInFragmentToNavigationHome()
                    findNavController().navigate(action)
                }
                "loading"->{
                    signInFragmentBinding.proSign.isVisible(true)
                }
                else ->{
                    signInFragmentBinding.proSign.isVisible(false)
                    Toast.makeText(context, "Try Again Latter", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun vaildation(){
        if(!isEmailValid(signInFragmentBinding.signEmailFiled.text.toString())){
            signInFragmentBinding.signEmailFiled.error = getString(R.string.shr_error_email)
        }
        else{
            signInFragmentBinding.signEmailFiled.error = null
            vaildationPassword()
        }
    }
    private  fun vaildationPassword(){
        if(!isPasswordValid(signInFragmentBinding.signPasswordFiled.text.toString())){
            signInFragmentBinding.signPasswordFiled.error = getString(R.string.shr_error_email)
        }
        else{
            signInFragmentBinding.signPasswordFiled.error = null
            vaildationName()
        }
    }
    private  fun vaildationName(){
        if(!textVaildation(signInFragmentBinding.signNameFiled.text.toString())){
            signInFragmentBinding.signNameFiled.error = "Error"
        }
        else{
            signInFragmentBinding.signNameFiled.error = null
            vaildationAddress()
        }
    }
    private   fun vaildationAddress(){
        if(!textVaildation(signInFragmentBinding.signAddress.text.toString())){
            signInFragmentBinding.signAddress.error = "Error"
        }
        else{
            signInFragmentBinding.signAddress.error = null
            vaildationJob()
        }
    }
    private  fun vaildationJob(){
        if(!textVaildation(signInFragmentBinding.signWork.text.toString())){
            signInFragmentBinding.signWork.error = "Error"
        }
        else{
            signInFragmentBinding.signWork.error = null
            vaildationBrithDay()
        }
    }
    private fun vaildationBrithDay(){
        if(!textVaildation(signInFragmentBinding.signBrithDay.text.toString())){
            signInFragmentBinding.signBrithDay.error = "Error"
        }
        else{
            signInFragmentBinding.signBrithDay.error = null
            vaildationGender()
        }
    }
    private fun vaildationGender(){
      val checked = signInFragmentBinding.radio.checkedRadioButtonId
        when(checked){
            R.id.radioMale->{
                uploadData("male")
            }
            R.id.radioFemale->{
                uploadData("female")
            }
            else->{
                Toast.makeText(context, "please Choose gender ", Toast.LENGTH_LONG).show()
            }

        }
    }
    private   fun uploadData(gender :String){
     viewModel.signNewUser(signInFragmentBinding.signEmailFiled.text.toString()
     ,signInFragmentBinding.signPasswordFiled.text.toString()
     ,signInFragmentBinding.signWork.text.toString()
     ,signInFragmentBinding.signNameFiled.text.toString()
     ,gender
     ,signInFragmentBinding.signAddress.text.toString()
     ,signInFragmentBinding.signBrithDay.text.toString()
     )
    }
    private  fun textVaildation(text: String?): Boolean {
        return text != null
    }
    private fun isPasswordValid(text: String?): Boolean {
        return text != null && text.length > 8
    }
    private fun isEmailValid(text: String?): Boolean {
        return text != null && text.contains('@')
    }


}