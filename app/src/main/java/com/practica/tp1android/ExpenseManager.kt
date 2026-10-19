package com.practica.tp1android

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExpenseManager(context: Context){
    private val sharedPreferences = context.getSharedPreferences("ExpensePrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Obtener todos los gastos guardados
    fun getExpenses(): MutableList<Expense> {
        val jsonString = sharedPreferences.getString("EXPENSES_JSON", null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Expense>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    // Guardar la lista completa de gastos
    fun saveAllExpenses(expenseList: List<Expense>) {
        val jsonString = gson.toJson(expenseList)
        sharedPreferences.edit().putString("EXPENSES_JSON", jsonString).apply()
    }

    //Agregar un nuevo gasto
    fun addExpense(newExpense: Expense) {
        val currentExpenses = getExpenses()
        currentExpenses.add(newExpense)
        saveAllExpenses(currentExpenses)
    }
}