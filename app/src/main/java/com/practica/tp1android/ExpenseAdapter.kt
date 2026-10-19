package com.practica.tp1android

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.util.Locale

class ExpenseAdapter(context: Context, val expenses: MutableList<Expense>) :
    ArrayAdapter<Expense>(context, 0, expenses){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false)

        val expense= getItem(position) ?: return view

        val tvTitle = view.findViewById<TextView>(R.id.tvItemTitle)
        val tvDetails = view.findViewById<TextView>(R.id.tvItemDetails)
        val tvAmount =view.findViewById<TextView>(R.id.tvItemAmount)
        val tvStatus = view.findViewById<TextView>(R.id.tvItemStatus)
        val tvDate = view.findViewById<TextView>(R.id.tvItemDate)

        //formato moneda
        val formato = NumberFormat.getNumberInstance(Locale("es", "AR"))
        formato.minimumFractionDigits = 2
        formato.maximumFractionDigits = 2

        tvTitle.text = expense.title
        tvDetails.text = "${expense.category} ${if (expense.isFixed == true) "• [Fijo]" else ""} ${if (expense.isUrgent == true) "• [Urgente]" else ""}"

        val montoFormateado = formato.format(expense.amount ?: 0.0)
        tvAmount.text = "$ $montoFormateado"

        tvDate.text = expense.date

        // Lógica de colores
        if (expense?.isPaid == true) {
            tvStatus.text = "Pagado"
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.context, R.color.status_paid))
        } else {
            tvStatus.text = "Pendiente"
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.context, R.color.status_pending))
        }

        //cambia estado
        view.setOnClickListener {
            val nuevoEstado = !expense.isPaid
            val mensaje = if (nuevoEstado) "¿Marcar como Pagado?" else "¿Marcar como Pendiente?"

            android.app.AlertDialog.Builder(context)
                .setTitle("Cambiar estado")
                .setMessage(mensaje)
                .setPositiveButton("Sí") { _, _ ->
                    expense.isPaid = nuevoEstado

                    val expenseManager = ExpenseManager(context)
                    expenseManager.saveAllExpenses(expenses)
                    notifyDataSetChanged() // Refresca la lista y actualiza el color/texto
                }
                    .setNegativeButton("Cancelar", null)
                    .show()
        }
        // eliminar gasto
        view.setOnLongClickListener {
                android.app.AlertDialog.Builder(context)
                    .setTitle("Eliminar Gasto")
                    .setMessage("¿Estás seguro de eliminar '${expense.title}'?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        remove(expense)

                        val expenseManager = ExpenseManager(context)
                        expenseManager.saveAllExpenses(expenses)
                        notifyDataSetChanged() // Saca el elemento de la lista visual
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            true
        }
        return view
    }
}