package org.zlobste.spotter.features.drunk_drivers

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_my_drivers.*
import kotlinx.android.synthetic.main.layout_driver_details_dialog.view.*
import kotlinx.android.synthetic.main.toolbar_with_image.*
import kotlinx.android.synthetic.main.toolbar_with_image.view.*
import org.zlobste.spotter.R
import org.zlobste.spotter.databinding.FragmentMyDriversBinding
import org.zlobste.spotter.features.my_drivers.model.Driver
import org.zlobste.spotter.features.my_drivers.model.MyDrivers
import org.zlobste.spotter.features.my_drivers.view.DriversAdapter
import org.zlobste.spotter.util.ScopedFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class DrunkDriversFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val driversAdapter: DriversAdapter by lazy {
        DriversAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            FragmentMyDriversBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        requireActivity().toolbar_with_image.title_text_view.text = getString(R.string.drunk_drivers)
    }

    private fun initRecyclerView() {
        with(my_drivers_recycler) {
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )

            adapter = driversAdapter

            driversAdapter.onInfoIconClicked = { driver ->
                showDialog(driver)
            }
        }

        driversAdapter.replace(MyDrivers.driversList.values.filter {
            it.isDrunk
        })

    }

    private fun showDialog(driver: Driver) {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.layout_driver_details_dialog, null)

        view.full_name.text = getString(R.string.full_name, driver.lastName, driver.firstName)
        view.email.text = driver.email
        view.b_day.text = driver.bDay
        view.drunk_status.text = if (driver.isDrunk) "Drunk" else "Not drunk"

        builder.setView(view)
        builder.setCustomTitle(null)

        val dialog: AlertDialog = builder.create()
        val close = view.findViewById(R.id.close_info_button) as Button?

        close!!.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    companion object {
        fun getInstance() = DrunkDriversFragment()
    }
}