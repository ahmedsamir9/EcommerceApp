package com.example.ecommerce.ui.Login

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.AdsItemBinding
import com.example.ecommerce.databinding.LoginFragmentFragmentBinding
import com.example.ecommerce.utils.LoaderDialog
import com.example.ecommerce.utils.OnClickOnItem
import com.example.ecommerce.utils.UiDisapperAndAppearInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(),View.OnClickListener {
    private val viewModel: LoginFragmentViewModel by viewModels()
    private lateinit var loginFragmentBinding: LoginFragmentFragmentBinding
    private lateinit var uiDisapperAndAppearInActivity: UiDisapperAndAppearInActivity
    private lateinit var loadingDailog :LoaderDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginFragmentBinding =DataBindingUtil.inflate(inflater,R.layout.login_fragment_fragment,container,false)
        loginFragmentBinding.forgetPasswordTv.setOnClickListener(this)
        loginFragmentBinding.logBtn.setOnClickListener(this)
        loginFragmentBinding.goToSignTv.setOnClickListener(this)
        loadingDailog = LoaderDialog(requireActivity())
        return loginFragmentBinding.root
    }

    override fun onStart() {
        super.onStart()
        subcribeToLiveData()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiDisapperAndAppearInActivity =context as UiDisapperAndAppearInActivity
        uiDisapperAndAppearInActivity.hideNav()
        uiDisapperAndAppearInActivity.hideToolBar()
    }



    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.log_btn-> {
                if (!isPasswordValid( loginFragmentBinding.loginPassworfFiled.text.toString())) {
                    loginFragmentBinding.loginPassworfFiled.error = getString(R.string.shr_error_password)
                } else {
                    loginFragmentBinding.loginPassworfFiled.error = null // Clear the error
                    if(!isEmailValid(loginFragmentBinding.loginEmailFiled.text.toString())){
                        loginFragmentBinding.loginEmailFiled.error = getString(R.string.shr_error_email)
                        Toast.makeText(context,loginFragmentBinding.loginEmailFiled.text.toString(),Toast.LENGTH_LONG).show()
                    }
                    else{
                        loginFragmentBinding.loginEmailFiled.error = null
                       viewModel.logUser(loginFragmentBinding.loginEmailFiled.text.toString(),loginFragmentBinding.loginPassworfFiled.text.toString())
                        val checkedRemembered =loginFragmentBinding.checkedBtn.isChecked
                        viewModel.saveRememberOption(checkedRemembered)
                        loadingDailog.startDialog()                    }
                }
            }
            R.id.forget_password_tv->{
                if(!isEmailValid(loginFragmentBinding.loginEmailFiled.text.toString())){
                    loginFragmentBinding.loginEmailFiled.error = getString(R.string.shr_error_email)
                }
                else{
                    loginFragmentBinding.loginEmailFiled.error = null
                  viewModel.resetPassword(loginFragmentBinding.loginEmailFiled.text.toString())
                }
            }
            R.id.go_to_sign_tv->{
                val action = LoginFragmentDirections.actionLoginFragmentToSignInFragment()
                findNavController().navigate(action)

            }
        }
    }


    private fun isPasswordValid(text: String?): Boolean {
        return text != null && text.length > 8
    }
    private fun isEmailValid(text: String?): Boolean {
        return text != null && text.contains('@')
    }
        private fun loggedGoToHome(isLoged :Boolean){
            if (isLoged){
                loadingDailog.hideprogress()
                val action = LoginFragmentDirections.actionLoginFragmentToNavigationHome()
                findNavController().navigate(action)
            }
            else{
                Toast.makeText(context,"Please try Again Latter ",Toast.LENGTH_LONG).show()
            }
        }
    private fun subcribeToLiveData(){
        viewModel.message.observe(viewLifecycleOwner,{
            if (it == "done")   loggedGoToHome(true)
                else{
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
                }

        })
    }
    }

