package com.example.fitnesssample.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.fitnesssample.R
import com.example.fitnesssample.model.SetupContent
import com.example.fitnesssample.other.Constants.KEY_NAME
import com.example.fitnesssample.other.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.tvToolbarTitle
import kotlinx.android.synthetic.main.fragment_settings.btnApplyChanges
import kotlinx.android.synthetic.main.fragment_settings.etName
import kotlinx.android.synthetic.main.fragment_settings.etWeight
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment: Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var setupContent: SetupContent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFieldsFromSharedPrefs()

        btnApplyChanges.setOnClickListener {

            if (applyChangesToSharedPrefs())
                Snackbar.make(requireView(), "Saved changes", Snackbar.LENGTH_LONG).show()
            else Snackbar.make(requireView(), "Please fill out all the fields", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun loadFieldsFromSharedPrefs() {

        etName.setText(setupContent.name)
        etWeight.setText(setupContent.weight.toString())
    }

    private fun applyChangesToSharedPrefs(): Boolean {

        val name = etName.text.toString()
        val weight = etWeight.text.toString()

        if (name.isEmpty() || weight.isEmpty())
            return false

        sharedPreferences.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .apply()

        val toolbarText = "Let's go $name!"
        requireActivity().tvToolbarTitle.text = toolbarText
        return true
    }
}