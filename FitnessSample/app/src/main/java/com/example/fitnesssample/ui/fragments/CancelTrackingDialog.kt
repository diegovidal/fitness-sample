package com.example.fitnesssample.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.fitnesssample.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CancelTrackingDialog: DialogFragment() {

    private var onPositiveClick: (() -> Unit)? = null

    fun setOnPositiveClick(listener: () -> Unit) {
        onPositiveClick = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Cancel the Run?")
            .setMessage("Are you sure to cancel the current run and delete all its data?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { _, _ ->
                onPositiveClick?.let { yes -> yes() }
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
    }
}