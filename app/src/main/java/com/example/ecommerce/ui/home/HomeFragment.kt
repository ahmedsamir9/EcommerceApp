package com.example.ecommerce.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentHomeBinding
import com.example.ecommerce.model.Category
import com.example.ecommerce.ui.home.adapters.CategoryListAdapter

import com.example.ecommerce.ui.home.adapters.ProductAdapter
import com.example.ecommerce.ui.home.adapters.adsAdapter
import com.example.ecommerce.utils.OnClickOnItem
import com.example.ecommerce.utils.Status
import com.example.ecommerce.utils.UiDisapperAndAppearInActivity
import com.example.ecommerce.utils.isVisible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var uiDisapperAndAppearInActivity: UiDisapperAndAppearInActivity
    private lateinit var homeBinding:FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    lateinit var categoryAdapter :CategoryListAdapter
    lateinit var productAdapter : ProductAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiDisapperAndAppearInActivity =context as UiDisapperAndAppearInActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        categoryAdapter = CategoryListAdapter(object :CategoryListAdapter.Interaction{
            override fun onItemSelected(item: Category) {
              val action =HomeFragmentDirections.actionNavigationHomeToCategoryFragment(item)
                findNavController().navigate(action)
            }
        })

        productAdapter = ProductAdapter()
        viewLifecycleOwner.lifecycle.addObserver(homeViewModel)
        setUpRecyclerView()

        return homeBinding.root
    }

    override fun onStart() {
        super.onStart()
        uiDisapperAndAppearInActivity.showNav()

        subscribeToLiveData()
    }
    private fun subscribeToLiveData(){
        homeViewModel.categories.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    homeBinding.progessStateCate.isVisible(false)
                    homeBinding.retryBtnCate.isVisible(false)
                    homeBinding.errorMsgCate.isVisible(false)
                    categoryAdapter.submitList(it.data)
                }
                Status.ERROR -> {
                    homeBinding.progessStateCate.isVisible(false)
                    homeBinding.retryBtnCate.isVisible(true)
                    homeBinding.errorMsgCate.isVisible(true)
                    homeBinding.errorMsgCate.text = it.message
                }
                Status.LOADING -> {
                    homeBinding.progessStateCate.isVisible(true)
                }
            }
        })
        homeViewModel.products.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    homeBinding.progessStateProduct.isVisible(false)
                    homeBinding.retryBtnProduct.isVisible(false)
                    homeBinding.errorMsgProduct.isVisible(false)
                    productAdapter.submitList(it.data)
                }
                Status.ERROR -> {
                    homeBinding.progessStateProduct.isVisible(false)
                    homeBinding.retryBtnProduct.isVisible(true)
                    homeBinding.errorMsgProduct.isVisible(true)
                    homeBinding.errorMsgProduct.text = it.message
                }
                Status.LOADING -> {
                    homeBinding.progessStateProduct.isVisible(true)
                }
            }
        })

    }
    private fun setUpRecyclerView(){
        homeBinding.categoryRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = categoryAdapter
        }
        homeBinding.adsRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            val adsAdapter = adsAdapter(interaction = object : OnClickOnItem {
                override fun onClickItem(id: Int) {
                    val Getintent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://egypt.souq.com/eg-en/"))
                    startActivity(Getintent)
                }

            })
            val adsArray = listOf<Int>(
                R.drawable.firstad,
                R.drawable.secondad,
                R.drawable.thirdad,
                R.drawable.fourthad
            )
            adsAdapter.submitList(adsArray)
            adapter = adsAdapter
        }
        homeBinding.productRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = productAdapter
        }
    }
}