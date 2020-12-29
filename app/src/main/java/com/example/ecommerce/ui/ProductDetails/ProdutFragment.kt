package com.example.ecommerce.ui.ProductDetails

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ProdutFragmentBinding
import com.example.ecommerce.model.Product
import com.example.ecommerce.utils.Status
import com.example.ecommerce.utils.UiDisapperAndAppearInActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProdutFragment : Fragment(),View.OnClickListener {
    private val args:ProdutFragmentArgs by navArgs()
    private lateinit var uiDisapperAndAppearInActivity: UiDisapperAndAppearInActivity
    private val viewModel: ProdutViewModel by viewModels()
    private lateinit var product :Product
    private lateinit var produtFragmentBinding:ProdutFragmentBinding
    private lateinit var progDialog: ProgressDialog

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        produtFragmentBinding= DataBindingUtil.inflate(layoutInflater, R.layout.produt_fragment, container, false)
        product = args.product
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        produtFragmentBinding.product =product
        progDialog = ProgressDialog(context)
        produtFragmentBinding.plusBtn.setOnClickListener(this)
        produtFragmentBinding.minusBtn.setOnClickListener(this)
        produtFragmentBinding.orderBtn.setOnClickListener(this)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = product.itemName

        return produtFragmentBinding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiDisapperAndAppearInActivity =context as UiDisapperAndAppearInActivity

    }

    override fun onStart() {
        super.onStart()
        uiDisapperAndAppearInActivity.hideNav()

        subscribeToLiveData()
    }

    override fun onClick(v: View?) {
        var num = produtFragmentBinding.productQuantity.text.toString().toInt()
                when(v?.id){
                    R.id.plus_btn -> {
                        num += 1
                        product.itemQuantity = num.toString()
                        produtFragmentBinding.productQuantity.text = product.itemQuantity
                    }
                    R.id.minus_btn -> {
                        if (num > 1) {
                            num -= 1
                            product.itemQuantity = num.toString()
                            produtFragmentBinding.productQuantity.text = product.itemQuantity
                        } else
                            Toast.makeText(context, "you have reached miniMuim quantity", Toast.LENGTH_SHORT).show()
                    }
                    R.id.order_btn -> {
                        viewModel.addProductToOrder(product)
                    }
        }
    }
    private fun subscribeToLiveData(){
        viewModel.reqState.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    progDialog.setTitle("loading")
                    progDialog.show()
                }
                Status.SUCCESS -> {
                    progDialog.dismiss()
                    Toast.makeText(context, "Added In Cart ", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    progDialog.dismiss()
                    MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Error")
                            .setMessage("Can't Upload try again later")
                            .setPositiveButton("Ok") { dialog, which ->

                            }
                            .show()
                }
            }
        })
    }
}