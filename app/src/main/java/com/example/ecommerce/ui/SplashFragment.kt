package com.example.ecommerce.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.utils.UiDisapperAndAppearInActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var uiDisapperAndAppearInActivity: UiDisapperAndAppearInActivity
   // @Inject private lateinit var firebaseAuth :FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)

    }

    override fun onStart() {
        super.onStart()
        val firebaseAuth =FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser == null) {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())

        }else{
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToNavigationHome())
        }
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiDisapperAndAppearInActivity =context as UiDisapperAndAppearInActivity

    }

}