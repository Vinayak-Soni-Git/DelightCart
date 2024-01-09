package com.example.delightcart.Data

import com.example.delightcart.R

data class OnboardingViewPagerData(
    val images: Array<Int> = arrayOf(
        R.drawable.quality_5_svgrepo_com,
        R.drawable.payment_method_svgrepo_com,
        R.drawable.delivery_man_svgrepo_com,
        R.drawable.notification_unread_svgrepo_com,
    ),

    val headings: Array<String> = arrayOf(
        "Quality Assured Products",
        "Pay via Any Payment Method",
        "Home delivery",
        "Instant Notifications",
    ),

    val descriptions: Array<String> = arrayOf(
        "Best and Original Quality products",
        "You can pay from any payment method as you want.",
        "Get your product at home within 2-3 business days",
        "Get notification on every purchase you make",
    )
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OnboardingViewPagerData

        if (!images.contentEquals(other.images)) return false
        if (!headings.contentEquals(other.headings)) return false
        if (!descriptions.contentEquals(other.descriptions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = images.contentHashCode()
        result = 31 * result + headings.contentHashCode()
        result = 31 * result + descriptions.contentHashCode()
        return result
    }
}