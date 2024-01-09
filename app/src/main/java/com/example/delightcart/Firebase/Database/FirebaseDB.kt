package com.example.delightcart.Firebase.Database

import com.example.delightcart.Util.Constants.ADDRESS_COLLECTION
import com.example.delightcart.Util.Constants.CART_COLLECTION
import com.example.delightcart.Util.Constants.CATEGORIES_COLLECTION
import com.example.delightcart.Util.Constants.CATEGORY
import com.example.delightcart.Util.Constants.PRODUCTS_COLLECTION
import com.example.delightcart.Util.Constants.STORES_COLLECTION
import com.example.delightcart.Util.Constants.USERS_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseDB {
    private val usersCollectionRef = Firebase.firestore.collection(USERS_COLLECTION)
    private val productsCollection = Firebase.firestore.collection(PRODUCTS_COLLECTION)
    private val categoriesCollection = Firebase.firestore.collection(CATEGORIES_COLLECTION)
    private val storesCollection = Firebase.firestore.collection(STORES_COLLECTION)
    private val firebaseStorage = Firebase.storage.reference

    val userUid = FirebaseAuth.getInstance().currentUser?.uid

    private val userCartCollection = userUid?.let {
        Firebase.firestore.collection(USERS_COLLECTION).document(it).collection(CART_COLLECTION)
    }
    private val userAddressesCollection = userUid?.let {
        Firebase.firestore.collection(USERS_COLLECTION).document(it).collection(ADDRESS_COLLECTION)

    }
    private val firebaseAuth = Firebase.auth

    fun getProductsByCategory(category: String, page: Long) =
        productsCollection.whereEqualTo(CATEGORY, category).limit(page).get()

    fun searchProducts(searchQuery: String) = productsCollection
        .orderBy("title")
        .startAt(searchQuery)
        .endAt("\u03A9+$searchQuery")
        .limit(5)
        .get()

    fun getCategories() = categoriesCollection.orderBy("rank").get()
}