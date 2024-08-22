package com.app.thefruitsspirit.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.eazy.base.BaseFragment
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.adapter.AdapterSummary
import com.app.thefruitsspirit.adapter.AdapterSummaryMonth
import com.app.thefruitsspirit.adapter.AdapterYear
import com.app.thefruitsspirit.adapter.SummaryTimeAdapter
import com.app.thefruitsspirit.auth.LoginActivity
import com.app.thefruitsspirit.databinding.FragmentSummaryBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.MonthModel
import com.app.thefruitsspirit.model.SummaryResponse
import com.app.thefruitsspirit.model.WelcomeModel
import com.app.thefruitsspirit.model.YearModel
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.isGone
import com.app.thefruitsspirit.utils.isVisible
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class SummaryFragment : BaseFragment<FragmentSummaryBinding>(),
    Observer<Resource<SummaryResponse>>,
    SummaryTimeAdapter.OnSummaryClick,
    AdapterSummaryMonth.OnClickMonth,
    AdapterYear.OnClickYear {
    private lateinit var adapter: SummaryTimeAdapter
    private val summaryTimeList = ArrayList<WelcomeModel>()

    private val summaryVM by viewModels<AuthVM>()

    val calendar = Calendar.getInstance()
    var cal: Calendar = Calendar.getInstance()

    private var pos = 0
    private var dateType = ""
    private var selectedDate = ""
    private lateinit var adapter1: AdapterSummaryMonth
    private val summaryMonthList = ArrayList<MonthModel>()



    private lateinit var adapterSum: AdapterSummary
    private val summaryList = ArrayList<SummaryResponse.Body>()

    private lateinit var adapterYear: AdapterYear
    private val yearList = ArrayList<YearModel>()

    val myFormat = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    private var monthId = ""

    var yearTime = ""
    override val bindingInflater: (LayoutInflater) -> FragmentSummaryBinding
        get() {
            return FragmentSummaryBinding::inflate
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        summaryTimeAdapter()
        adapterMonthly()
        yearAdapter()
        onClicks()

        //Current Year
        var calendar: Calendar = Calendar.getInstance()
        var year: Int = calendar.get(Calendar.YEAR)
        binding.tvMonthlyYear.text = year.toString()
        binding.tvYearYear.text = year.toString()

        binding.editStartDate.setText(sdf.format(calendar.time))
        binding.editEndDate.setText(sdf.format(calendar.time))

        incrementDateByOne(cal.time)

        dateFormat.format(calendar.time)
        selectedDate = dateFormat.format(calendar.time)
        Log.d("sjkdjksdajksad", "" + sdf)
        dateApi()
    }

    private fun onClicks() {
        binding.editStartDate.setOnClickListener {
            dateType = "0"
            datePickerDialog()
        }
        binding.editEndDate.setOnClickListener {
//            dateType = "1"
//            datePickerDialog()
        }
        binding.tvMonthlyYear.setOnClickListener {
            binding.rvMonthlyYear.isVisible()
        }
        binding.tvYearYear.setOnClickListener {
            binding.rvYearlyYear.isVisible()
        }

        binding.clLayout.setOnClickListener {
            binding.rvYearlyYear.isGone()
            binding.rvMonthlyYear.isGone()
        }

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateFormat.format(calendar.time)
            selectedDate = dateFormat.format(calendar.time)
            dateApi()
            Log.d("sjkdjksdajksad", "" + selectedDate)
        }
    }

    private fun dateApi() {
        binding.also {
            var map = HashMap<String, String>()
            map["Date"] = selectedDate
            summaryVM.summaryApi(map, requireActivity()).observe(requireActivity(), this)
        }
    }

    private fun weeklyApi() {
        binding.also {
            var map = HashMap<String, String>()
            map["startDate"] = binding.editStartDate.text.toString()
            map["endDate"] = binding.editEndDate.text.toString()
            summaryVM.summaryApi(map, requireActivity()).observe(requireActivity(), this)
        }
    }

    private fun monthlyApi() {
        binding.also {
            var map = HashMap<String, String>()

            if (monthId.isEmpty()) {
                val dateFormat: DateFormat = SimpleDateFormat("MM")
                val date = Date()
                monthId = dateFormat.format(date)

                Log.d("Month", dateFormat.format(date))
                map["month"] = dateFormat.format(date)
                var year: Int = calendar.get(Calendar.YEAR)
                if (yearTime.isNotEmpty()){
                    map["year"] = yearTime
                }else{
                    map["year"] = year.toString()
                }
                summaryVM.summaryApi(map, requireActivity()).observe(requireActivity(), this)

            } else {
                map["month"] = monthId
                var year: Int = calendar.get(Calendar.YEAR)
                if (yearTime.isNotEmpty()){
                    map["year"] = yearTime
                }else{
                    map["year"] = year.toString()
                }
                summaryVM.summaryApi(map, requireActivity()).observe(requireActivity(), this)
            }
        }
    }

    private fun yearlyApi() {
        binding.also {
            var map = HashMap<String, String>()
            var year: Int = calendar.get(Calendar.YEAR)
            if (yearTime.isNotEmpty()){
                map["year"] = yearTime
            }else{
                map["year"] = year.toString()
            }

            summaryVM.summaryApi(map, requireActivity()).observe(requireActivity(), this)
        }
    }

    private fun yearAdapter() {
        yearList.add(YearModel("2024"))
        yearList.add(YearModel("2025"))
        yearList.add(YearModel("2026"))
        yearList.add(YearModel("2027"))
        yearList.add(YearModel("2028"))
        yearList.add(YearModel("2029"))
        yearList.add(YearModel("2030"))
        yearList.add(YearModel("2031"))
        yearList.add(YearModel("2032"))
        yearList.add(YearModel("2034"))
        yearList.add(YearModel("2035"))
        yearList.add(YearModel("2036"))
        yearList.add(YearModel("2037"))
        yearList.add(YearModel("2038"))
        yearList.add(YearModel("2039"))
        yearList.add(YearModel("2040"))
        adapterYear = AdapterYear(requireContext(), yearList, this)
        binding.rvMonthlyYear.adapter = adapterYear
        binding.rvYearlyYear.adapter = adapterYear
    }

    private fun datePickerDialog() {
        cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(cal)
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.TimePickerDialogTheme, dateSetListener,
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        cal[cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)] = cal.get(Calendar.DAY_OF_MONTH)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000

        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
    }

    private fun updateDateInView(cal: Calendar) {
        binding.editStartDate.setText(sdf.format(cal.time))
        try {
            incrementDateByOne(cal.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        weeklyApi()
    }

    private fun incrementDateByOne(date: Date?) {
        cal.time = date
        cal.add(Calendar.DATE, 7)
        binding.editEndDate.setText(sdf.format(cal.time))
    }

    private fun adapterSummary() {
        adapterSum = AdapterSummary(requireContext(), summaryList)
        binding.rvDaly.adapter = adapterSum
    }

    private fun summaryTimeAdapter() {
        summaryTimeList.add(WelcomeModel(R.drawable.plus_icon, "Daily", ""))
        summaryTimeList.add(WelcomeModel(R.drawable.plus_icon, "Weekly", ""))
        summaryTimeList.add(WelcomeModel(R.drawable.plus_icon, "Monthly", ""))
        summaryTimeList.add(WelcomeModel(R.drawable.plus_icon, "Yearly", ""))
        adapter = SummaryTimeAdapter(requireContext(), summaryTimeList, true, this)
        binding.rvSummary.adapter = adapter
    }

    private fun adapterMonthly() {
        summaryMonthList.add(MonthModel("1", "January"))
        summaryMonthList.add(MonthModel("2", "February"))
        summaryMonthList.add(MonthModel("3", "March"))
        summaryMonthList.add(MonthModel("4", "April"))
        summaryMonthList.add(MonthModel("5", "May"))
        summaryMonthList.add(MonthModel("6", "June"))
        summaryMonthList.add(MonthModel("7", "July"))
        summaryMonthList.add(MonthModel("8", "August"))
        summaryMonthList.add(MonthModel("9", "September"))
        summaryMonthList.add(MonthModel("10", "October"))
        summaryMonthList.add(MonthModel("11", "November"))
        summaryMonthList.add(MonthModel("12", "December"))
        adapter1 = AdapterSummaryMonth(requireContext(), summaryMonthList, true, this, monthId)
        binding.rvMonthly.adapter = adapter1
    }

    override fun click(position: Int) {
        pos = position
        when (position) {
            0 -> {
                binding.rlDaily.isVisible()
                binding.tvClander.isVisible()
                binding.rlWeekly.isGone()
                binding.rlMonthly.isGone()
                binding.rlSelectYear.isGone()
                dateApi()
            }

            1 -> {
                binding.rlDaily.isGone()
                binding.tvClander.isGone()
                binding.rlWeekly.isVisible()
                binding.rlMonthly.isGone()
                binding.rlSelectYear.isGone()
                weeklyApi()
            }

            2 -> {
                binding.rlDaily.isGone()
                binding.tvClander.isGone()
                binding.rlWeekly.isGone()
                binding.rlMonthly.isVisible()
                binding.rlSelectYear.isGone()
                monthlyApi()
            }

            3 -> {
                binding.rlDaily.isGone()
                binding.tvClander.isGone()
                binding.rlWeekly.isGone()
                binding.rlMonthly.isGone()
                binding.rlSelectYear.isVisible()
                yearlyApi()
            }
        }
    }

    override fun onChanged(value: Resource<SummaryResponse>) {
        summaryList.clear()
        when (value.status) {
            Status.SUCCESS -> {
                if (!value.data?.body.isNullOrEmpty()) {
                    summaryList.addAll(value.data?.body!!)
                    binding.tvNoResultFound.isGone()
                    binding.rvDaly.isVisible()
                    adapterSummary()
                    adapterSum.notifyDataSetChanged()
                } else {
                    binding.rvDaly.isGone()
                    binding.tvNoResultFound.isVisible()
                }
            }

            Status.ERROR -> {
                Log.e("error", value.message.toString())
                if (value.message == "Please Login First") {
                    startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                        putExtra("session", "0")
                    })
                }else{
                    showErrorAlert(requireActivity(), value.message.toString())
                }
            }

            Status.LOADING -> {
                Log.e("error", value.message.toString())
            }
        }
    }

    override fun clickMonth(id: String) {
        monthId = id
        Log.d("jkjksdnjksdjknsa", "" + id)
        binding.rvYearlyYear.isGone()
        binding.rvMonthlyYear.isGone()
        monthlyApi()
    }

    override fun yearClick(year: String) {
        yearTime = year
        Log.d("jkjksdnjksdjknsa", "" + year)
        binding.rvYearlyYear.isGone()
        binding.rvMonthlyYear.isGone()
        binding.tvMonthlyYear.text = year
        binding.tvYearYear.text = year
        yearlyApi()
    }
}