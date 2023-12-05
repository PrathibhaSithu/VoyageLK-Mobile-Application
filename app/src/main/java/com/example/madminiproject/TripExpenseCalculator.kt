package com.example.madminiproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView

class TripExpenseCalculator : AppCompatActivity() {

    private lateinit var travelFromSpinner : Spinner
    private lateinit var tripDurationSpinner : Spinner
    private lateinit var travelerCountSpinner : Spinner
    private lateinit var hotelTypeSpinner : Spinner
    private lateinit var transportSpinner : Spinner
    private lateinit var expenseDisplay : TextView


    private var travelFrom: String = ""
    private var tripDuration: String = ""
    private var travelerCount: String = ""
    private var hotelType: String = ""
    private var transportMedium: String = ""
    private var finalTotal:Double = 0.0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_expense_calculator)

        travelFromSpinner = findViewById<Spinner>(R.id.spinner_travel_from)
        tripDurationSpinner = findViewById<Spinner>(R.id.spinner_trip_duration)
        travelerCountSpinner = findViewById<Spinner>(R.id.spinner_num_of_travellers)
        hotelTypeSpinner = findViewById<Spinner>(R.id.spinner_hotel_type)
        transportSpinner = findViewById<Spinner>(R.id.spinner_transport_medium)
        expenseDisplay = findViewById<TextView>(R.id.expense_display)


        val calcExpenseBtn = findViewById<Button>(R.id.calc_expense_btn)
        calcExpenseBtn.setOnClickListener {

            travelFrom = travelFromSpinner.selectedItem.toString()
            travelerCount = travelerCountSpinner.selectedItem.toString()
            tripDuration = tripDurationSpinner.selectedItem.toString()
            hotelType = hotelTypeSpinner.selectedItem.toString()
            transportMedium = transportSpinner.selectedItem.toString()

            finalTotal += when(travelFrom){
                "Europe Country" -> 197500.0
                "North American Country" -> 275000.0
                "South American Country" -> 294000.0
                "Asian Country" -> 97000.0
                "Middle East Country" -> 125000.0
                else -> 0.0
            }

            when(tripDuration){
                "Weekend (1 to 3 days)" -> finalTotal += 20000 * 3
                "Short Trip (up to 1 week)" -> finalTotal += 20000 * 8
                "Long Trip (over 1 week)" -> finalTotal += 20000 * 12
            }

            when(hotelType){
                "3 Star" -> finalTotal += 25000
                "4 Star" -> finalTotal += 50000
                "5 Star" -> finalTotal += 75000
            }

            when(travelerCount){
                "1 Person" -> finalTotal *= 1
                "2 Person" -> finalTotal *= 2
                "3 Person" -> finalTotal *= 3
                "4 Person" -> finalTotal *= 4
                "Group" -> finalTotal *= 8
            }

            when(transportMedium){
                "Rental Car/Cab" -> finalTotal += 100000
                "Rental Motorcycle" -> finalTotal += 30000
                "Uber" -> finalTotal += 20000
                "Bus" -> finalTotal += 9000
                "Train" -> finalTotal += 10000
            }

            expenseDisplay.setText("LKR ${finalTotal}0")

            finalTotal = 0.0
        }

    }
}