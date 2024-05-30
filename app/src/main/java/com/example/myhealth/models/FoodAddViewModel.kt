package com.example.myhealth.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myhealth.R
import com.example.myhealth.data.Food
import com.example.myhealth.data.FoodTimeType
import com.example.myhealth.data.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FoodAddViewModel @Inject constructor() : ViewModel() {

    var selectedTypeProduct = MutableStateFlow(Product())
    var foodAddDialog by mutableStateOf(false)
    var foodEditDialog by mutableStateOf(false)
    var eatingTimeName = R.string.breakfast_title
    var eatingFoodTime = MutableStateFlow( Food(emptyList<Product>().toMutableList(), FoodTimeType.Dinner))
    var products = mutableStateListOf<Product>()
    private var editProductIndex by mutableIntStateOf(0)

    /*@OptIn(ExperimentalMaterial3Api::class)
    val timePickerState = rememberTimePickerState()
    val showTimePicker by remember { mutableStateOf(false) }*/

    fun getEatingTimeName(eatingType: String?) {
        when (eatingType) {
            FoodTimeType.Lunch.n -> eatingTimeName = R.string.lunch_title
            FoodTimeType.Breakfast.n -> eatingTimeName = R.string.breakfast_title
            FoodTimeType.Dinner.n -> eatingTimeName = R.string.dinner_title
            else -> eatingTimeName = R.string.lunch_title
        }
    }

    fun getEatingFoodTime(model: DiaryViewModel, eatingType: String?) {
        when (eatingType) {
            FoodTimeType.Lunch.n -> eatingFoodTime.value = model.selectedDay.value.lunch
            FoodTimeType.Breakfast.n -> eatingFoodTime.value = model.selectedDay.value.breakfast
            FoodTimeType.Dinner.n -> eatingFoodTime.value = model.selectedDay.value.dinner
            else -> eatingFoodTime.value = model.selectedDay.value.lunch
        }
        eatingFoodTime.value.products.forEach {
            if(!products.contains(it))
                products.add(it)
        }
    }

    fun foodAddDialogShow(show: Boolean = true) {
        foodAddDialog = show
    }
    fun foodEditDialogShow(show: Boolean = true) {
        foodEditDialog = show
    }

    fun onProductItemSelected(product: Product){
        selectedTypeProduct.value=product
        foodAddDialogShow()
    }

    fun onDelSwipe(product: Product){
        products.remove(product)
        eatingFoodTime.value.products.remove(product)
    }

    fun onEditSwipe(product: Product){
        editProductIndex = eatingFoodTime.value.products.indexOf(product)
        selectedTypeProduct.value=product
        foodEditDialog=true
    }
    fun addProduct(product: Product){
        eatingFoodTime.value.products.add(product)
        foodAddDialogShow(false)
    }

    fun editProduct(product: Product){
        eatingFoodTime.value.products[editProductIndex] = product
        products[editProductIndex] = product
        foodEditDialogShow(false)
    }

    fun updateListProducts(model: DiaryViewModel){
        when (model.selectedEatTimeName.value) {
            FoodTimeType.Lunch.n -> model.selectedDay.value.lunch.products = products
            FoodTimeType.Breakfast.n -> model.selectedDay.value.breakfast.products = products
            FoodTimeType.Dinner.n -> model.selectedDay.value.dinner.products = products
            else -> model.selectedDay.value.lunch.products = products
        }
        model.selectedDay.value.updateAllCount()
    }

}