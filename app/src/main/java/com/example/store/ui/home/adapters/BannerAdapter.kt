package com.example.store.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.store.data.models.home.ResponseBanners.*
import com.example.store.databinding.ItemBannersBinding
import com.example.store.utils.BASE_URL_IMAGE_WITH_STORAGE
import com.example.store.utils.PRODUCT
import com.example.store.utils.base.BaseDiffUtils
import com.example.store.utils.extensions.loadImage
import javax.inject.Inject

class BannerAdapter @Inject constructor() : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    private var items = emptyList<ResponseBannersItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerAdapter.ViewHolder {
        val binding = ItemBannersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerAdapter.ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemBannersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseBannersItem) {
            binding.apply {
                itemTitle.text = item.title
                //Image
                val imageUrl = "$BASE_URL_IMAGE_WITH_STORAGE${item.image}"
                itemImg.loadImage(imageUrl)
                //Click
                root.setOnClickListener {
                    val sendData = if (item.link!! == PRODUCT) {
                        item.linkId
                    } else {
                        item.urlLink?.slug
                    }
                    onItemClickListener?.let {
                        it(sendData!!, item.link)
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((String, String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String, String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<ResponseBannersItem>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}