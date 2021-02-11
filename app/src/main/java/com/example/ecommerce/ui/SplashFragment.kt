package com.example.ecommerce.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Interpolator
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentSplashBinding
import com.example.ecommerce.utils.UiDisapperAndAppearInActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var splashBinding: FragmentSplashBinding
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
        splashBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_splash,container,false)

        return splashBinding.root
    }

    override fun onStart() {
        super.onStart()
        uiDisapperAndAppearInActivity.hideNav()
        uiDisapperAndAppearInActivity.hideToolBar()
        splashBinding.appImg.visibility =View.VISIBLE

        val txtAnimator = ObjectAnimator.ofFloat(splashBinding.appName,View.TRANSLATION_X, -1000f,0.0f)
        txtAnimator.interpolator= BounceInterpolator()
        txtAnimator.duration =1000
        txtAnimator.start()


            lifecycleScope.launch {
                delay(2000)
                val firebaseAuth =FirebaseAuth.getInstance()
                if (firebaseAuth.currentUser == null) {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())

                }else{
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToNavigationHome())
                }
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