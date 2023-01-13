package com.polito.s287503.showprofileactivity

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import coil.load
import org.json.JSONObject


class TripDetailsFragment : Fragment(R.layout.fragment_trip_details) {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_trip_details,container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val pickUpLocation = view.findViewById<TextView>(R.id.textViewPickupLocation)
        val dropLocation = view.findViewById<TextView>(R.id.textViewDropLocation)
        val price = view.findViewById<TextView>(R.id.Price)
        val avaSeats = view.findViewById<TextView>(R.id.textViewAvailableSeats)
        val addInfo = view.findViewById<TextView>(R.id.AddInfo)
        val data = view.findViewById<TextView>(R.id.textViewDate)
        val pickUpHour = view.findViewById<TextView>(R.id.textViewDepartureHour)
        val dropHour = view.findViewById<TextView>(R.id.textViewArrivalHour)
        val carPhoto = view.findViewById<ImageView>(R.id.imageViewCar)

        if (arguments?.containsKey("TripID") == true) {
            val sharedPref = this.activity?.getPreferences(Context.MODE_PRIVATE)
            val jsonTripList = JSONObject(sharedPref?.getString("tripList", "null").toString())
            val id = requireArguments().getString("TripID")
            val thisTrip = JSONObject(jsonTripList[id].toString())
            pickUpLocation.text = thisTrip["pickUpLocation"].toString()
            dropLocation.text = thisTrip["dropLocation"].toString()
            price.text = thisTrip["price"].toString()
            avaSeats.text = thisTrip["avaSeats"].toString()
            addInfo.text = thisTrip["addInfo"].toString()
            data.text= thisTrip["data"].toString()
            pickUpHour.text= thisTrip["pickUpHour"].toString()
            dropHour.text= thisTrip["dropHour"].toString()
            if (thisTrip["carPhotoPath"]!="null") carPhoto.setImageURI(Uri.parse(thisTrip["carPhotoPath"].toString()))
        }
        else {
        pickUpLocation.text = arguments?.getString("pickUpLocation")
        dropLocation.text = arguments?.getString("dropLocation")
        price.text = arguments?.getString("price")
        avaSeats.text = arguments?.getString("avaSeats")
        addInfo.text = arguments?.getString("addInfo")
        data.text= arguments?.getString("data")
        pickUpHour.text= arguments?.getString("pickUpHour")
        dropHour.text= arguments?.getString("dropHour")

        val path=arguments?.getString("carPhotoPath")
        val imageBitmap = arguments?.getParcelable<Bitmap>("carPhotoBit")
        carPhoto.setImageBitmap(imageBitmap)
        carPhoto.tag = path }
    }

}