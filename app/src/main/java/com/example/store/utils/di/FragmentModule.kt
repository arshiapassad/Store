package com.example.store.utils.di

import androidx.fragment.app.Fragment
import com.example.store.data.models.address.BodySetAddressForShipping
import com.example.store.data.models.address.BodySubmitAddress
import com.example.store.data.models.cart.BodyAddToCart
import com.example.store.data.models.comments.BodySendComment
import com.example.store.data.models.login.BodyLogin
import com.example.store.data.models.profile.BodyUpdateProfile
import com.example.store.data.models.shipping.BodyCoupon
import com.example.store.data.models.wallet.BodyIncreaseWallet
import com.example.store.ui.detail.adapter.PagerAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    fun provideFragment(fragment: Fragment) = PagerAdapter(fragment.parentFragmentManager, fragment.lifecycle)

    @Provides
    fun bodyLogin() = BodyLogin()

    @Provides
    fun bodyUpdateProfile() = BodyUpdateProfile()

    @Provides
    fun bodyIncreaseWallet() = BodyIncreaseWallet()

    @Provides
    fun bodySubmitAddress() = BodySubmitAddress()

    @Provides
    fun bodyAddToCart() = BodyAddToCart()

    @Provides
    fun bodySendComment() = BodySendComment()

    @Provides
    fun bodySetAddressForShipping() = BodySetAddressForShipping()

    @Provides
    fun bodyCoupon() = BodyCoupon()
}