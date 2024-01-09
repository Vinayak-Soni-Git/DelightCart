package com.example.delightcart.Models

sealed class ProductsCategories(val category: String) {
    object Mobile : ProductsCategories("Mobile")
    object Clothe : ProductsCategories("Clothe")
    object Furniture : ProductsCategories("Furniture")
    object Watch : ProductsCategories("Watch")
    object Accessory : ProductsCategories("Accessory")
    object Electronic : ProductsCategories("Electronic")
    object Tablet : ProductsCategories("Tablet")
    object Laptop : ProductsCategories("Laptop")
}