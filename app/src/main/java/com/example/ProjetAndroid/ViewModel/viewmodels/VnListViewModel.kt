package com.example.ProjetAndroid.ViewModel.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.ProjetAndroid.Model.DTOs.queries.GetVnList
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class VnListViewModel @AssistedInject constructor(
    private val getVnList: GetVnList,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    val vnList = getVnList()
        .cachedIn(viewModelScope)
        .onStart { Log.d("flow", "start") }
        .onCompletion { Log.d("flow", "complete") }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): VnListViewModel
    }

    fun searchVn(newText: String?) {
        getVnList.searchVn(newText)

    }
}