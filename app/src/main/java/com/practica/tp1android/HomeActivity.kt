package com.practica.tp1android
import android.widget.*
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class HomeActivity : AppCompatActivity(){
    private val expenseList= mutableListOf<Expense>()
    private lateinit var adapter: ExpenseAdapter
    private lateinit var expenseManager: ExpenseManager
    private var totalGeneral: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializamos el ExpenseManager
        expenseManager = ExpenseManager(this)

        //referencias del XML
        val etTitle= findViewById<EditText>(R.id.etExpenseTitle)
        val etAmount= findViewById<EditText>(R.id.etExpenseAmount)
        val spCategory= findViewById<Spinner>(R.id.spCategory)
        val cbIsPaid= findViewById<CheckBox>(R.id.cbIsPaid)
        val cbIsFixed= findViewById<CheckBox>(R.id.cbIsFixed)
        val cbIsUrgent = findViewById<CheckBox>(R.id.cbIsUrgent)
        val btnAdd = findViewById<Button>(R.id.btnAddExpense)
        val lvExpenses = findViewById<ListView>(R.id.lvExpenses)
        val tvTotal = findViewById<TextView>(R.id.tvTotalExpenses)
        val etExpenseDate = findViewById<EditText>(R.id.etExpenseDate)

        //categoria de Spinner
        val categories = listOf("Comida", "Transporte", "Servicios", "Entretenimiento","Otros")
        val spinnerAdapter = ArrayAdapter( this,android.R.layout.simple_spinner_item,categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = spinnerAdapter

        //cargar gastos guardados
        expenseList.clear()
        expenseList.addAll(expenseManager.getExpenses())

        //configura lista y el adapter
        adapter = ExpenseAdapter(this, expenseList)
        lvExpenses.adapter = adapter

        //calcular total de gastos
        recalculateTotal(tvTotal)

        //boton guardar gasto
        btnAdd.setOnClickListener {
            val title= etTitle.text.toString().trim()
            val amountStr = etAmount.text.toString().trim()
            val category = spCategory.selectedItem.toString()
            val dateInput = etExpenseDate.text.toString().trim()
            val isPaid = cbIsPaid.isChecked
            val isFixed = cbIsFixed.isChecked
            val isUrgent = cbIsUrgent.isChecked

            //validar
            if (title.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Completa la descripcion y el monto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val amount = amountStr.toDoubleOrNull() ?: 0.0

            // crear el objeto Gasto
            val newExpense = Expense(title, amount, category, dateInput, isPaid, isFixed, isUrgent)

            //guardar usando expense
            expenseManager.addExpense(newExpense)

            // agregar a la lista y actualizar el adapter
            expenseList.clear()
            expenseList.addAll(expenseManager.getExpenses())
            adapter.notifyDataSetChanged()

            //actualiza total
            recalculateTotal(tvTotal)

            // limpiar los campos del formulario
            etTitle.text.clear()
            etAmount.text.clear()
            etExpenseDate.text.clear()
            cbIsPaid.isChecked = false
            cbIsFixed.isChecked = false
            cbIsUrgent.isChecked = false
            etTitle.requestFocus()

            Toast.makeText(this, "Gasto guardado con éxito", Toast.LENGTH_SHORT).show()
        }
    }
    private fun recalculateTotal(tvTotal: TextView) {
        totalGeneral = expenseList.sumOf {it.amount}

        val formato = NumberFormat.getNumberInstance(Locale("es", "AR"))
        formato.minimumFractionDigits = 2
        formato.maximumFractionDigits = 2

        val totalFormateado = formato.format(totalGeneral)
        tvTotal.text = "Total $ $totalFormateado"
    }
}