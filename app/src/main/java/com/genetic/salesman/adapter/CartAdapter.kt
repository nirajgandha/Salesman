package com.genetic.salesman.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.genetic.salesman.DealerApplication
import com.genetic.salesman.R
import com.genetic.salesman.databinding.CartItemRecyclerLayoutBinding
import com.genetic.salesman.databinding.ProductRemoveDialogBinding
import com.genetic.salesman.interfaces.CartProductListener
import com.genetic.salesman.utils.GlideApp

class CartAdapter(private val cartProductListener: CartProductListener, private val context: Context) : RecyclerView.Adapter<CartAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val appBinding = CartItemRecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(appBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val key = (context.applicationContext as DealerApplication).getKeyArrayList()[position]
            cartItemRecyclerLayoutBinding.productTitle.text = (context.applicationContext as DealerApplication)
                .getProductCartList().getValue(key).productOption.productName
            cartItemRecyclerLayoutBinding.productDescription.text = (context.applicationContext as DealerApplication)
                .getProductCartList().getValue(key).productOption.categoryName
            cartItemRecyclerLayoutBinding.productAmount.text = context.resources.getString(R.string.amount_s,
                (context.applicationContext as DealerApplication).getProductCartList().getValue(key).productOption.optionAmount.toString())
            cartItemRecyclerLayoutBinding.qty.text = (context.applicationContext as DealerApplication)
                .getProductCartList().getValue(key).quantity.toString()
            cartItemRecyclerLayoutBinding.btnMinus.setOnClickListener {
                if ((context.applicationContext as DealerApplication).getProductCartList().getValue(key).quantity == 1) {
                    val builder = AlertDialog.Builder(context)
                    val dialogBinding = ProductRemoveDialogBinding.inflate(LayoutInflater.from(context))
                    builder.setView(dialogBinding.root)
                    val dialog = builder.create()
                    dialog.setCanceledOnTouchOutside(true)
                    dialog.setCancelable(true)
                    dialogBinding.message.text = context.resources.getString(R.string.remove_from_cart, "${(context.applicationContext as DealerApplication).getProductCartList().getValue(key).productOption.productName} ${(context.applicationContext as DealerApplication).getProductCartList().getValue(key).productOption.optionTitle}")
                    dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
                    dialogBinding.btnOk.setOnClickListener {
                        removeItems(key)
                        cartProductListener.cartProductUpdates()
                        dialog.dismiss()
                    }
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.show()
                } else {
                    (context.applicationContext as DealerApplication).getProductCartList().getValue(key).quantity -= 1
                    cartItemRecyclerLayoutBinding.qty.text = (context.applicationContext as DealerApplication).getProductCartList().getValue(key).quantity.toString()
                    cartProductListener.cartProductUpdates()
                }
            }
            cartItemRecyclerLayoutBinding.btnPlus.setOnClickListener {
                (context.applicationContext as DealerApplication).getProductCartList().getValue(key).quantity += 1
                cartItemRecyclerLayoutBinding.qty.text = (context.applicationContext as DealerApplication).getProductCartList().getValue(key).quantity.toString()
                cartProductListener.cartProductUpdates()
            }

            cartItemRecyclerLayoutBinding.delete.setOnClickListener {
                removeItems(key)
                cartProductListener.cartProductUpdates()
            }
            GlideApp.with(context).load(
                (context.applicationContext as DealerApplication).getProductCartList().getValue(key).imageUrl)
                .into(cartItemRecyclerLayoutBinding.roundImgLayout.roundedImageView)
                .onLoadFailed(ResourcesCompat.getDrawable(context.resources, R.drawable.logo, context.theme))

        }
    }

    private fun removeItems(key: String) {
        (context.applicationContext as DealerApplication).getProductCartList().remove(key)
        (context.applicationContext as DealerApplication).getKeyArrayList().remove(key)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return (context.applicationContext as DealerApplication).getKeyArrayList().size
    }

    class ViewHolder(val cartItemRecyclerLayoutBinding: CartItemRecyclerLayoutBinding) : RecyclerView.ViewHolder(cartItemRecyclerLayoutBinding.root)

}