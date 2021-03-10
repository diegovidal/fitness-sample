package com.example.fitnesssample.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesssample.R
import com.example.fitnesssample.other.SortType
import com.example.fitnesssample.other.TrackingUtility
import com.example.fitnesssample.ui.adapters.RunAdapter
import com.example.fitnesssample.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.fab
import kotlinx.android.synthetic.main.fragment_run.rvRuns
import kotlinx.android.synthetic.main.fragment_run.spFilter
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class RunFragment: Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var runAdapter: RunAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions()
        setupRecyclerView()
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }

        when(viewModel.sortType) {

            SortType.DATE -> spFilter.setSelection(0)
            SortType.RUNNING_TIME -> spFilter.setSelection(1)
            SortType.DISTANCE -> spFilter.setSelection(2)
            SortType.AVG_SPEED -> spFilter.setSelection(3)
            SortType.CALORIES_BURNED -> spFilter.setSelection(4)
        }

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when(position) {
                    0 -> viewModel.sortRuns(SortType.DATE)
                    1 -> viewModel.sortRuns(SortType.RUNNING_TIME)
                    2 -> viewModel.sortRuns(SortType.DISTANCE)
                    3 -> viewModel.sortRuns(SortType.AVG_SPEED)
                    4 -> viewModel.sortRuns(SortType.CALORIES_BURNED)
                }
            }
        }

        viewModel.runsLiveData.observe(viewLifecycleOwner) {
            runAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {

        runAdapter = RunAdapter()
        rvRuns.adapter = runAdapter
        rvRuns.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
        else requestPermissions()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun requestPermissions() {
        TrackingUtility.requestPermissions(this)
    }
}