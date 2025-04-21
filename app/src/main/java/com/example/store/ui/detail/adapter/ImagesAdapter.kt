package com.example.store.ui.detail.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.store.databinding.ItemProductImagesBinding
import com.example.store.utils.BASE_URL_IMAGE
import com.example.store.utils.base.BaseDiffUtils
import com.example.store.utils.extensions.loadImageWithGlide
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImagesAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    private var items = emptyList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemProductImagesBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: String) {
            binding.apply {
                val image = "$BASE_URL_IMAGE$item"
                itemImg.loadImageWithGlide(image)
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item) }
                }
            }
        }
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<String>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}