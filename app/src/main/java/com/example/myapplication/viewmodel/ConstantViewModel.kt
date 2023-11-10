package com.example.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.CitiesResponse


class ConstantViewModel : ViewModel() {
    var mutableLiveData: MutableLiveData<MutableList<CitiesResponse.Data>> = MutableLiveData()
}