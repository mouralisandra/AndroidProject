package com.example.ProjetAndroid.ViewModel.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ProjetAndroid.Model.api.DataClasses.Tags
import com.example.ProjetAndroid.Model.DTOs.Vn
import com.example.ProjetAndroid.Model.DTOs.queries.GetVnDetails
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VnItemViewModel @AssistedInject constructor(
    private val getVnDetails: GetVnDetails,
    @Assisted private val arg: String,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(UiState(vn = Vn()))
    val state = _state.asStateFlow()

    private var _fullTags: List<Tags> = mutableListOf()
    private val fullTags
        get() = _fullTags

    init {
        viewModelScope.launch {
            getVnDetails(arg).collectLatest { vn ->
                _fullTags = vn.tags.toList().sortedByDescending { it.rating }
                val filteredVn = vn.copy(tags = filterTags())
                _state.value = UiState(vn = filteredVn)
            }
        }
    }

    private fun filterTags(): List<Tags> {
        val result = fullTags.toMutableList()
        Log.d("tags", "tags number before filter ${result.size}")
        for (i in fullTags) {
            if (!state.value.sexual && i.category == "ero") {
                result.remove(i)
            }
            if (!state.value.content && i.category == "cont") {
                result.remove(i)
            }
            if (!state.value.technical && i.category == "tech") {
                result.remove(i)
            }
            if (state.value.spoilerLvl == SpoilerLvl.SPOILER_LVL_IS_0
                && (i.spoiler == 1 || i.spoiler == 2)
            ) {
                Log.d("tags", i.toString())
                result.remove(i)
            }
            if (state.value.spoilerLvl == SpoilerLvl.SPOILER_LVL_IS_1 && i.spoiler == 2) {
                result.remove(i)
                Log.d("tags", i.toString())
            }
        }
        Log.d("tags", "tags number after filter ${result.size}")
        return if (state.value.spoilerQuantity == SpoilerQuantity.SPOILER_SUMMARY) {
            result.take(SUMMARY_QUANTITY)
        } else {
            result.toList()
        }
    }

    fun changeSexualContent() {
        _state.value.sexual = !state.value.sexual
        setupNewState()
    }

    fun changeContent() {
        _state.value.content = !state.value.content
        setupNewState()
    }

    fun changeTechnical() {
        _state.value.technical = !state.value.technical
        setupNewState()
    }


    private fun setupNewState() {
        val newVn = _state.value.vn.copy(tags = filterTags())
        val newUiState = state.value.copy(vn = newVn)
        _state.value = newUiState
    }

    data class UiState(
        var content: Boolean = true,
        var sexual: Boolean = false,
        var technical: Boolean = true,
        var spoilerLvl: SpoilerLvl = SpoilerLvl.SPOILER_LVL_IS_0,
        var spoilerQuantity: SpoilerQuantity = SpoilerQuantity.SPOILER_SUMMARY,
        var vn: Vn
    )

    enum class SpoilerLvl {
        SPOILER_LVL_IS_0,
        SPOILER_LVL_IS_1,
    }

    enum class SpoilerQuantity {
        SPOILER_SUMMARY,
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("vm", "cleared")
    }

    @AssistedFactory
    interface Factory {
        fun create(arg: String, savedStateHandle: SavedStateHandle): VnItemViewModel
    }

    companion object {
        private const val SUMMARY_QUANTITY = 30
    }
}