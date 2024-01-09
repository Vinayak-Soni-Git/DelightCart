package com.example.delightcart.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.delightcart.Firebase.Database.FirebaseDB
import com.example.delightcart.Models.Category
import com.example.delightcart.Models.Product
import com.example.delightcart.Util.Resource

class ShoppingViewModel(
    private val firebaseDatabase: FirebaseDB
) : ViewModel() {

    val categories = MutableLiveData<Resource<List<Category>>>()
    val search = MutableLiveData<Resource<List<Product>>>()

    fun searchProducts(searchQuery: String) {
        search.postValue(Resource.Loading())
        firebaseDatabase.searchProducts(searchQuery).addOnCompleteListener {
            if (it.isSuccessful) {
                val productsList = it.result!!.toObjects(Product::class.java)
                search.postValue(Resource.Success(productsList))

            } else
                search.postValue(Resource.Error(it.exception.toString()))

        }
    }

    private lateinit var categoriesSafe: List<Category>
    fun getCategories() {
        categoriesSafe = emptyList()
        if (categoriesSafe != null) {
            categories.postValue(Resource.Success(categoriesSafe))
            return
        }
        categories.postValue(Resource.Loading())
        firebaseDatabase.getCategories().addOnCompleteListener {
            if (it.isSuccessful) {
                val categoriesList = it.result!!.toObjects(Category::class.java)
                categoriesSafe = categoriesList
                categories.postValue(Resource.Success(categoriesList))
            } else
                categories.postValue(Resource.Error(it.exception.toString()))
        }


    }

}