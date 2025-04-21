package com.example.store.ui.profile_favorite

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.data.models.profile_favorite.ResponseProfileFavorites.Data
import com.example.store.databinding.ItemMyFavoritesBinding
import com.example.store.utils.BASE_URL_IMAGE
import com.example.store.utils.base.BaseDiffUtils
import com.example.store.utils.extensions.loadImage
import com.example.store.utils.extensions.moneySeparating
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FavoritesAdapter @Inject constructor(@ApplicationContext private val context: Context) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private var items = emptyList<Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.ViewHolder {
        val binding = ItemMyFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesAdapter.ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemMyFavoritesBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Data) {
            binding.apply {
                item.likeable?.let {
                    //Text
                    itemTitle.text = it.title
                    quantityTxt.text = "${it.quantity} ${context.getString(R.string.item)}"
                    priceTxt.text = it.finalPrice!!.moneySeparating()
                    //Image
                    val imageUrl = "$BASE_URL_IMAGE${it.image}"
                    itemImg.loadImage(imageUrl)
                }
                //Click
                itemTrashImg.setOnClickListener {
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