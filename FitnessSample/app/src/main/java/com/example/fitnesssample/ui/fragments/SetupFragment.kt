package com.example.fitnesssample.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.fitnesssample.R
import com.example.fitnesssample.model.SetupContent
import com.example.fitnesssample.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.fitnesssample.other.Constants.KEY_NAME
import com.example.fitnesssample.other.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.tvToolbarTitle
import kotlinx.android.synthetic.main.fragment_setup.etName
import kotlinx.android.synthetic.main.fragment_setup.etWeight
import kotlinx.android.synthetic.main.fragment_setup.tvContinue
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment: Fragment(R.layout.fragment_setup) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var setupContent: SetupContent

    @set:Inject
    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isFirstAppOpen) {

            updateToolbarText(setupContent.name)

            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()

            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }

        tvContinue.setOnClickListener {

            if (writePersonalDataToSharedPref())
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            else Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {

        val name = etName.text.toString()
        val weight = etWeight.text.toString()

        if (name.isEmpty() || weight.isEmpty())
            return false

        sharedPreferences.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()

        updateToolbarText(name)
        return true
    }

    private fun updateToolbarText(name: String) {

        val toolbarText = "Let's go $name!"
        requireActivity().tvToolbarTitle.text = toolbarText
    }
}