package com.example.ecommerce.ui.Category

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.CategoryFragmentBinding
import com.example.ecommerce.model.Category
import com.example.ecommerce.model.Product
import com.example.ecommerce.ui.home.HomeFragmentDirections
import com.example.ecommerce.utils.Status
import com.example.ecommerce.utils.UiDisapperAndAppearInActivity
import com.example.ecommerce.utils.isVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private lateinit var uiDisapperAndAppearInActivity: UiDisapperAndAppearInActivity
    private lateinit var categoryFragmentBinding:CategoryFragmentBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var catAdapter :CategoryProductsAdapter
    private lateinit var category :Category
    val args:  CategoryFragmentArgs by navArgs()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiDisapperAndAppearInActivity =context as UiDisapperAndAppearInActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      categoryFragmentBinding= DataBindingUtil.inflate(
          layoutInflater,
          R.layout.category_fragment,
          container,
          false
      )
        category = args.category
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = category.name

        catAdapter = CategoryProductsAdapter(object :CategoryProductsAdapter.Interaction{
            override fun onItemSelected(imageView: ImageView, item: Product) {
                val extras = FragmentNavigatorExtras(
                        imageView to item.itemImageUrl
                )
                val action = CategoryFragmentDirections.actionCategoryFragmentToProdutFragment2(product = item)
                findNavController().navigate(action)
            }
        })
        setUpRecyclerView()
        return categoryFragmentBinding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.home) {
            Toast.makeText(context,"lol",Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        categoryViewModel.getProducts(category.id)
        uiDisapperAndAppearInActivity.hideNav()
        uiDisapperAndAppearInActivity.showToolBar()
        categoryFragmentBinding.retryBtnProduct.setOnClickListener {
            categoryViewModel.getProducts(category.id)
        }
        subscribeToLiveData()

    }
    private fun subscribeToLiveData(){

       categoryViewModel.products.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    categoryFragmentBinding.progessStateProduct.isVisible(false)
                    categoryFragmentBinding.retryBtnProduct.isVisible(false)
                    categoryFragmentBinding.errorMsgProduct.isVisible(false)
                    catAdapter.submitList(it.data)
                }
                Status.ERROR -> {
                    categoryFragmentBinding.progessStateProduct.isVisible(false)
                    categoryFragmentBinding.retryBtnProduct.isVisible(true)
                    categoryFragmentBinding.errorMsgProduct.isVisible(true)
                    categoryFragmentBinding.errorMsgProduct.text = it.message
                }
                Status.LOADING -> {
                    categoryFragmentBinding.progessStateProduct.isVisible(true)
                }
            }
        })

    }

    private fun setUpRecyclerView(){
        categoryFragmentBinding.productRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = catAdapter
        }
    }


}