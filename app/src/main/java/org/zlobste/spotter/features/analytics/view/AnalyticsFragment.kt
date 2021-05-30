package org.zlobste.spotter.features.analytics.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_analytics.*
import kotlinx.android.synthetic.main.layout_driver_analytics_dialog.view.*
import kotlinx.android.synthetic.main.toolbar_with_image.*
import kotlinx.android.synthetic.main.toolbar_with_image.view.*
import org.zlobste.spotter.R
import org.zlobste.spotter.databinding.FragmentAnalyticsBinding
import org.zlobste.spotter.features.analytics.logic.FilterDriversByDate
import org.zlobste.spotter.features.my_drivers.model.Driver
import org.zlobste.spotter.features.my_drivers.view.DriversAdapter
import org.zlobste.spotter.util.ScopedFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

class AnalyticsFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()

    private val driversAdapter: DriversAdapter by lazy {
        DriversAdapter(requireContext())
    }
    val startDate = MutableLiveData<Date>()
    val endDate = MutableLiveData<Date>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            FragmentAnalyticsBinding.inflate(inflater, container, false)
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initToolbar()
        initFields()
        initRecyclerView()
    }

    private fun initToolbar() {
        requireActivity().toolbar_with_image.title_text_view.text =
            getString(R.string.analytics)
    }

    private fun initFields() {
        start_date.setEndIconOnClickListener {
            showCalendar(true)
        }

        end_date.setEndIconOnClickListener {
            showCalendar(false)
        }
    }

    private fun showCalendar(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        val pickerDialog = DatePickerDialog(
            requireContext(),
            { _, chosenYear, monthOfYear, dayOfMonth ->
                val date = GregorianCalendar(chosenYear, monthOfYear, dayOfMonth).time
                if (isStartDate) startDate.value = date else endDate.value = date
                updateDrivers()
            },
            year,
            month,
            day
        )
        pickerDialog.datePicker.maxDate = calendar.timeInMillis
        pickerDialog.show()
    }

    private fun initRecyclerView() {
        with(analytics_drivers) {
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )

            adapter = driversAdapter

            driversAdapter.onInfoIconClicked = { driver ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    showDialog(driver)
                }
            }
        }
    }

    private fun getDriversInTimesBounds(): List<Driver> {
        return if (startDate.value != null && endDate.value != null) {
            FilterDriversByDate.getDrivers(
                startDate.value!!,
                endDate.value!!
            ).distinct()
        } else {
            emptyList()
        }
    }

    private fun updateDrivers() {
        val drivers = getDriversInTimesBounds()
        driversAdapter.replace(drivers)
        total.text = drivers.size.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDialog(driver: Driver) {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.layout_driver_analytics_dialog, null)

        view.full_name.text = getString(R.string.full_name, driver.lastName, driver.firstName)
        view.email.text = driver.email
        view.b_day.text = driver.bDay
        val count = FilterDriversByDate.getDriverLevelById(
            driver.driverId, startDate.value!!,
            endDate.value!!
        )
        view.incidents_count.text = count.toString()
        view.drinking_level.text = chooseLevel(count).first
        view.indicator.setImageResource(chooseLevel(count).second)

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun chooseLevel(incidentsCount: Int): Pair<String, Int> {
        val rate = incidentsCount / getMonthDiff()
        val level: String
        val drawable: Int
        when {
            rate < 2 -> {
                level = "Low"
                drawable = R.drawable.background_green_circle
            }
            rate < 10 -> {
                level = "Middle"
                drawable = R.drawable.background_yellow_circle
            }
            else -> {
                level = "High"
                drawable = R.drawable.background_red_circle
            }
        }
        return level to drawable
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMonthDiff(): Long {
        val difference = ChronoUnit.MONTHS.between(
            startDate.value!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            endDate.value!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        )
        return if(difference == 0L) 1 else difference
    }

    companion object {
        fun getInstance() = AnalyticsFragment()
    }
}