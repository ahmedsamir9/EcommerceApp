package com.example.ecommerce.ui.dashboard

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.CartItemBinding
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.model.OrderWithProduct
import com.example.ecommerce.utils.Status
import com.example.ecommerce.utils.UiDisapperAndAppearInActivity
import com.example.ecommerce.utils.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private lateinit var cartItemBinding:FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var products :MutableList<OrderWithProduct>
    private lateinit var uiDisapperAndAppearInActivity: UiDisapperAndAppearInActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiDisapperAndAppearInActivity =context as UiDisapperAndAppearInActivity
        uiDisapperAndAppearInActivity.showToolBar()
        uiDisapperAndAppearInActivity.showNav()
    }
    private var total = 0
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
       cartItemBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart,container,false)
        cartAdapter = CartAdapter(object :CartAdapter.Interaction{
            override fun onChangeValue(item: OrderWithProduct,type:String) {
               if (dashboardViewModel.isConnectedToInterNet()){
                    dashboardViewModel.changeQuantity(item.quantity,item)
                   if (type == "plus") {
                       total +=item.productPrice
                   }
                   else{
                       total -=item.productPrice
                   }
                   cartItemBinding.totalPrice.text= total.toString()
               }
                else{
                    dashboardViewModel.sentMessage("TRy Again Latter")
                }
            }

            override fun onDeleteItem(position: Int, item: OrderWithProduct) {
                if (dashboardViewModel.isConnectedToInterNet()){
                    MaterialAlertDialogBuilder(requireContext())
                            .setMessage("Are you Sure you Want to Delete this Item")
                            .setPositiveButton("Yes") { dialog, which ->
                                dashboardViewModel.removeProduct(item)
                                products.removeAt(position)
                                cartAdapter.submitList(products)
                                total -=(item.quantity * item.productPrice)
                                cartAdapter.notifyItemRemoved(position)
                            }.setNegativeButton("cancel"){_ ,_ ->}
                            .show()
                }
                else{
                    dashboardViewModel.sentMessage("TRy Again Latter")
                }
            }


        })
        cartItemBinding.materialButton2.setOnClickListener {
            if (dashboardViewModel.isConnectedToInterNet()){
                dashboardViewModel.sentMessage("You Make it")
            }
            else{
                dashboardViewModel.sentMessage("Try Again Latter")
            }
        }
            setUpRecyclerView()
        cartItemBinding.layouter.isVisible(false)
        return cartItemBinding.root
    }

    override fun onStart() {
        super.onStart()
        dashboardViewModel.getProducts()
        subscribeToLiveData()
    }
    private fun subscribeToLiveData(){
    dashboardViewModel.noInterNet.observe(viewLifecycleOwner,{
        Toast.makeText(context,it,Toast.LENGTH_LONG).show()
    })
        dashboardViewModel.products.observe(viewLifecycleOwner,{
        when(it.status){
            Status.SUCCESS->{
                cartItemBinding.layouter.isVisible(true)
                cartItemBinding.textInCart.isVisible(false)
                products= it.data as MutableList<OrderWithProduct>
                cartAdapter.submitList(products)
                for(item in products){
                    total += (item.quantity * item.productPrice)
                }
                cartItemBinding.totalPrice.text= total.toString()
            }
            Status.ERROR->{
                 cartItemBinding.layouter.isVisible(false)
                cartItemBinding.textInCart.isVisible(true)
                cartItemBinding.textInCart.text=it.message
            }
            Status.LOADING->{


            }
        }
        })

    }
    private fun setUpRecyclerView(){
        cartItemBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = cartAdapter
        }
    }
}