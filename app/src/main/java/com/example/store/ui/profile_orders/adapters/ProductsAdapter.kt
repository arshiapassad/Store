package com.example.store.ui.profile_orders.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.store.data.models.profile_order.ResponseProfileOrdersList.Data.OrderItem
import com.example.store.databinding.ItemOrderProductBinding
import com.example.store.utils.BASE_URL_IMAGE
import com.example.store.utils.base.BaseDiffUtils
import com.example.store.utils.extensions.loadImage
import javax.inject.Inject

class ProductsAdapter @Inject constructor() : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private var items = emptyList<OrderItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapter.ViewHolder {
        val binding = ItemOrderProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsAdapter.ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemOrderProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.apply {
                item.extras?.let {
                    itemTitle.text = it.title
                    //Image
                    val imageUrl = "$BASE_URL_IMAGE${it.image}"
                    itemImg.loadImage(imageUrl)
                }
            }
        }
    }

    fun setData(data: List<OrderItem>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}
