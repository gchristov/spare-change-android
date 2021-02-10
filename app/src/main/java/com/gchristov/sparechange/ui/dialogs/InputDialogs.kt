package com.gchristov.sparechange.ui.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.gchristov.sparechange.R
import com.gchristov.sparechange.common.toMinorUnit
import com.gchristov.sparechange.databinding.DialogSavingsGoalChooseBinding
import com.gchristov.sparechange.databinding.DialogSavingsGoalCreateBinding
import com.gchristov.sparechange.repository.model.Amount
import com.gchristov.sparechange.repository.model.SavingsGoal
import com.gchristov.sparechange.ui.fragments.savingsgoals.SavingsGoalChooseAdapter
import java.math.BigDecimal

object InputDialogs {

    fun showSavingsGoalChooseDialog(
        context: Context,
        savingsGoals: List<SavingsGoal>,
        listener: (savingsGoal: SavingsGoal) -> Unit
    ) {
        // Create dialog
        val builder = AlertDialog.Builder(context, R.style.DialogTheme)
        builder.setTitle(R.string.savings_goals_choose)
        // Attach buttons
        builder
                .setNegativeButton(context.getString(R.string.savings_goals_cancel), null)
        // Set custom layout
        val frameView = FrameLayout(context)
        builder.setView(frameView)
        val alertDialog = builder.create()
        val dialogLayout = DialogSavingsGoalChooseBinding.inflate(alertDialog.layoutInflater, frameView, true)
        val adapter = SavingsGoalChooseAdapter {
            listener(it)
            // Dismiss
            alertDialog.dismiss()
        }
        dialogLayout.recyclerView.layoutManager = LinearLayoutManager(context)
        dialogLayout.recyclerView.adapter = adapter
        adapter.showItems(savingsGoals)
        // Style buttons after dialog shown
        alertDialog.setOnShowListener { dialog: DialogInterface ->
            val negativeBtn = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeBtn.setTextColor(ContextCompat.getColor(context, R.color.accent))
            negativeBtn.setOnClickListener {
                // Dismiss
                dialog.dismiss()
            }
        }
        // Show dialog
        alertDialog.show()
    }

    fun showSavingsGoalCreateDialog(
        context: Context,
        listener: (name: String, target: Amount) -> Unit
    ) {
        // Create dialog
        val builder = AlertDialog.Builder(context, R.style.DialogTheme)
        builder.setTitle(R.string.savings_goals_add)
        // Attach buttons
        builder
                .setPositiveButton(context.getString(R.string.savings_goals_save), null)
                .setNegativeButton(context.getString(R.string.savings_goals_cancel), null)
        // Set custom layout
        val frameView = FrameLayout(context)
        builder.setView(frameView)
        val alertDialog = builder.create()
        val dialogLayout = DialogSavingsGoalCreateBinding.inflate(alertDialog.layoutInflater, frameView, true)
        // Style buttons after dialog shown
        alertDialog.setOnShowListener { dialog: DialogInterface ->
            val positiveBtn = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            positiveBtn.setTextColor(ContextCompat.getColor(context, R.color.accent))
            positiveBtn.setOnClickListener {
                val name = dialogLayout.nameField.text.toString().trim { it <= ' ' }
                val amount = dialogLayout.amountField.text.toString().trim { it <= ' ' }
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(amount)) {
                    listener(name, Amount("GBP", BigDecimal(amount).toMinorUnit()))
                    // Dismiss
                    hideKeyboard(context, dialogLayout.nameField)
                    hideKeyboard(context, dialogLayout.amountField)
                    dialog.dismiss()
                }
            }
            val negativeBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeBtn.setTextColor(ContextCompat.getColor(context, R.color.accent))
            negativeBtn.setOnClickListener {
                // Dismiss
                hideKeyboard(context, dialogLayout.nameField)
                hideKeyboard(context, dialogLayout.amountField)
                dialog.dismiss()
            }
        }
        // Make keyboard appear automatically when dialog appears
        dialogLayout.nameField.onFocusChangeListener = View.OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            val window = alertDialog.window
            if (hasFocus && window != null) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }
        // Show dialog
        alertDialog.show()
        dialogLayout.nameField.requestFocus()
    }

    private fun hideKeyboard(
        context: Context,
        editText: EditText
    ) {
        try {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}