package com.example.ecommerce.ui.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.SearchFragmentBinding
import com.example.ecommerce.model.Product
import com.example.ecommerce.ui.home.HomeFragmentDirections
import com.example.ecommerce.ui.home.adapters.ProductAdapter
import com.example.ecommerce.utils.CONSTANTS
import com.example.ecommerce.utils.Status
import com.example.ecommerce.utils.UiDisapperAndAppearInActivity
import com.example.ecommerce.utils.isVisible
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class SearchFragment : Fragment() {

   private lateinit var productAdapter : ProductAdapter
   private lateinit var searchFragmentBinding: SearchFragmentBinding
    private lateinit var uiDisapperAndAppearInActivity: UiDisapperAndAppearInActivity

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.search_fragment,
            container,
            false
        )
        productAdapter = ProductAdapter(object : ProductAdapter.Interaction {
            override fun onItemSelected(item: Product, imageView: ImageView) {
                val extras = FragmentNavigatorExtras(
                    imageView to item.itemImageUrl
                )
                val action =
                    SearchFragmentDirections.actionSearchFragmentToProdutFragment(product = item)
                findNavController().navigate(action)
            }

        })
        setUpRecyclerView()

        viewLifecycleOwner.lifecycle.addObserver(viewModel)
        return searchFragmentBinding.root
    }

    override fun onStart() {
        super.onStart()
        searchFragmentBinding.micBtn.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Product Name")
            try {
                startActivityForResult(intent, CONSTANTS.REQUEST_TO_VOICE)
            }
            catch (excep: Exception){
                Toast.makeText(context, "Sorry you can't use Voice NOW", Toast.LENGTH_SHORT).show()
            }
        }

        val listner = object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.getSpecificProduct(newText)
                return false
            }
        }
        searchFragmentBinding.searchView1.setOnQueryTextListener(listner)
        searchFragmentBinding.retryBtnProduct1.setOnClickListener{
            viewModel.getProducts()
        }
        subscribeToLiveData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiDisapperAndAppearInActivity =context as UiDisapperAndAppearInActivity
        uiDisapperAndAppearInActivity.hideNav()
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Search"
    }
    private fun setUpRecyclerView(){
       searchFragmentBinding.productRv1.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = productAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONSTANTS.REQUEST_TO_VOICE){
            if(resultCode == Activity.RESULT_OK || null != data){
                val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
               searchFragmentBinding.searchView1.setQuery(result?.get(0), true)
            }

        }
    }
    private fun subscribeToLiveData(){
        viewModel.products.observe(viewLifecycleOwner,{
            when(it.status){
                Status.SUCCESS->{
                    searchFragmentBinding.progessStateProduct1.isVisible(false)
                    searchFragmentBinding .retryBtnProduct1.isVisible(false)
                    searchFragmentBinding.errorMsgProduct1.isVisible(false)
                    productAdapter.submitList(it.data)
                }
                Status.ERROR->{
                    searchFragmentBinding.progessStateProduct1.isVisible(false)
                    searchFragmentBinding .retryBtnProduct1.isVisible(true)
                    searchFragmentBinding.errorMsgProduct1.isVisible(true)
                    searchFragmentBinding.errorMsgProduct1.text = it.message
                }
                Status.LOADING->{
                    searchFragmentBinding.progessStateProduct1.isVisible(true)
                }
            }
        })
    }
}