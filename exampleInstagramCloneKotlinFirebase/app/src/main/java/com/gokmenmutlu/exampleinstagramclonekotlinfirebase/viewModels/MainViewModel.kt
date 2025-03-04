package com.gokmenmutlu.exampleinstagramclonekotlinfirebase.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    //for UploadImage
    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?> get() = _selectedImageUri

    fun setSelectedImage(uri: Uri?) {
        _selectedImageUri.value = uri
    }

}