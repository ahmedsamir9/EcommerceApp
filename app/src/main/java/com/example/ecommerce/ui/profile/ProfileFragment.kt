package com.example.ecommerce.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentProfileBinding
import com.example.ecommerce.utils.Status
import com.example.ecommerce.utils.isVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()
private lateinit var profileBinding: FragmentProfileBinding
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        profileBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false)
        profileBinding.signOutBtn.setOnClickListener {
            viewModel.signOut()
           findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToLoginFragment())
        }
        return profileBinding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUserData()
        subscribeToLiveData()
    }
    private fun subscribeToLiveData(){
        viewModel.user.observe(viewLifecycleOwner,{
            when(it.status){
                Status.LOADING->{ profileBinding.profilePro.isVisible(true)}
                Status.SUCCESS->{
                    profileBinding.profilePro.isVisible(false)
                    profileBinding.user = it.data
                }
                Status.ERROR->{ profileBinding.profilePro.isVisible(true)
                Toast.makeText(context,"Try Again Latter",Toast.LENGTH_LONG).show()
                }


            }
        })
    }

}