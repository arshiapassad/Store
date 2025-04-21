package com.example.store.ui.home.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.models.home.ResponseProducts
import com.example.store.data.models.home.ResponseProducts.*
import com.example.store.data.models.home.ResponseProducts.SubCategory.*
import com.example.store.data.models.home.ResponseProducts.SubCategory.Products.*
import com.example.store.databinding.ItemProductsBinding
import com.example.store.utils.BASE_URL_IMAGE
import com.example.store.utils.base.BaseDiffUtils
import com.example.store.utils.extensions.loadImage
import com.example.store.utils.extensions.moneySeparating
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductsAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private var items = emptyList<Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapter.ViewHolder {
        val binding = ItemProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsAdapter.ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemProductsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Data) {
            binding.apply {
                itemTitle.text = item.title
                //Image
                val imageUrl = "$BASE_URL_IMAGE${item.image}"
                itemImg.loadImage(imageUrl)
                //Quantity
                itemProgress.progress = item.quantity.toString().toInt()
                //Discount
                if (item.discountedPrice!! > 0) {
                    itemDiscount.apply {
                        isVisible = true
                        text = item.discountedPrice.toString().toInt().moneySeparating()
                    }
                    itemPrice.apply {
                        text = item.productPrice.toString().toInt().moneySeparating()
                        paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        setTextColor(ContextCompat.getColor(context, R.color.salmon))
                    }
                    itemPriceDiscount.apply {
                        isVisible = true
                        text = item.finalPrice.toString().toInt().moneySeparating()
                    }
                } else {
                    itemDiscount.isVisible = false
                    //itemPriceDiscount.isVisible = false
                    itemPriceDiscount.apply {
                        text = item.productPrice.toString().toInt().moneySeparating()
                        setTextColor(ContextCompat.getColor(context, R.color.darkTurquoise))
                    }
                }
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item.id!!) }
                }
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<Data>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}