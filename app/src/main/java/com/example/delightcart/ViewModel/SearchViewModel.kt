package com.example.delightcart.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.delightcart.Models.Product
import com.example.delightcart.Util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _searchResult = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val searchResult = _searchResult.asStateFlow()

    fun searchProducts(searchQuery: String) {
        viewModelScope.launch {
            _searchResult.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .orderBy("name")
            .startAt(searchQuery)
            .endAt("\u03A9+$searchQuery")
            .limit(5)
            .get().addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _searchResult.emit(Resource.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _searchResult.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}