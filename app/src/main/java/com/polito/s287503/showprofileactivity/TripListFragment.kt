package com.polito.s287503.showprofileactivity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject


class TripListFragment : Fragment(R.layout.fragment_trip_list) {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPref = this.activity?.getPreferences(Context.MODE_PRIVATE)
        //sharedPref?.edit()?.clear()?.apply()
        return if (sharedPref?.contains("tripList")== true) {
            inflater.inflate(R.layout.fragment_trip_list,container, false)
        } else {
            inflater.inflate(R.layout.emptylist, container, false)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val sharedPref = this.activity?.getPreferences(Context.MODE_PRIVATE)

        if (sharedPref?.contains("tripList")==false) {
            val fab = view.findViewById<FloatingActionButton>(R.id.fab)
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_tripListFragment_to_tripEditFragment)
            }
        }
        else {
            val rv = view.findViewById<RecyclerView>(R.id.recyclerView)
            val jsonTripList = JSONObject(sharedPref?.getString("tripList", "null").toString())
            val data = mutableListOf<Trip>()
            for (tripID in jsonTripList.keys()) {
                val thisTrip = JSONObject(jsonTripList[tripID].toString())
                // Calculate the estimated duration
                var estDuration = "unknown"
                if (thisTrip["dropHour"].toString() != "" && thisTrip["pickUpHour"].toString() != "") {
                    val arrivalInMinutes = thisTrip["dropHour"].toString().split(":")[0].toInt() * 60 + thisTrip["dropHour"].toString().split(":")[1].toInt()
                    val departureMinutes = thisTrip["pickUpHour"].toString().split(":")[0].toInt() * 60 + thisTrip["pickUpHour"].toString().split(":")[1].toInt()
                    val difference = arrivalInMinutes - departureMinutes
                    val minutes = difference % 60
                    val hours = difference/60
                    estDuration =if (minutes>10) "$hours:$minutes" else "$hours:0${minutes}" } //Better looking format

                data.add(Trip(tripID, thisTrip["carPhotoPath"].toString(),thisTrip["pickUpLocation"].toString(), thisTrip["dropLocation"].toString(), thisTrip["pickUpHour"].toString(), thisTrip["data"].toString(), estDuration, thisTrip["dropHour"].toString(), thisTrip["avaSeats"].toString(), thisTrip["price"].toString(), thisTrip["addInfo"].toString()))
            }
            /*
            // testing data
            val randomTrip = Trip("temp",null, "Belluno", "Venezia", "10:45", "22/04/2021", "1.10h", "11:55", 2, 31.2f)
            var data = listOf<Trip>(randomTrip, randomTrip, randomTrip, randomTrip,randomTrip, randomTrip, randomTrip, randomTrip) */
            rv.layoutManager = LinearLayoutManager(rv.context)
            rv.adapter = TripAdapter(data, findNavController())
            val fab = view.findViewById<FloatingActionButton>(R.id.fab)
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_tripListFragment_to_tripEditFragment)
            }
        }
    }
}

open class Trip(val id:String, val carImageURI:String, val depLocation:String, val arrLocation:String, val depTime:String, val depDate:String, val estDuration:String, val arrTime:String, val avSeats:String, val price:String, val addInfo:String) // We can leave it empty because the data class has already everything that we need

class TripAdapter(val data: List<Trip>, val navController: NavController) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    class TripViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val carImage = v.findViewById<ImageView>(R.id.imageViewCar)
        val depLocation = v.findViewById<TextView>(R.id.pickupLocation)
        val arrLocation = v.findViewById<TextView>(R.id.destination)
        val depTime = v.findViewById<TextView>(R.id.departureHour)
        val arrTime = v.findViewById<TextView>(R.id.arrivalHour)
        val depDate = v.findViewById<TextView>(R.id.date)
        val duration = v.findViewById<TextView>(R.id.estimatedDuration)
        val seats = v.findViewById<TextView>(R.id.availableSeats)
        val price = v.findViewById<TextView>(R.id.price)
        val editButton = v.findViewById<ImageButton>(R.id.edit)
        val cardView = v.findViewById<CardView>(R.id.cardView)

        fun bind(t: Trip, navController: NavController) {
            if (t.carImageURI != "null") carImage.setImageURI(Uri.parse(t.carImageURI))
            depLocation.text = t.depLocation
            arrLocation.text = t.arrLocation
            depTime.text = t.depTime
            arrTime.text = t.arrTime
            depDate.text = t.depDate
            duration.text = t.estDuration
            seats.text = t.avSeats.toString()
            price.text = t.price.toString()
            editButton.setOnClickListener{
                navController.navigate(R.id.action_tripListFragment_to_tripEditFragment, bundleOf("TripID" to t.id))
                //Toast.makeText(depLocation.context, "Test", Toast.LENGTH_LONG).show()
            }
            cardView.setOnClickListener {
                navController.navigate(R.id.action_tripListFragment_to_tripDetailsFragment, bundleOf("TripID" to t.id))
                //Toast.makeText(depLocation.context, "Test", Toast.LENGTH_LONG).show()
            }
        }

        fun unbind() {
            editButton.setOnClickListener{ null }
            cardView.setOnClickListener { null }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layout = layoutInflater.inflate(R.layout.list_item, parent, false)
        return TripViewHolder(layout)
    }

    override fun onBindViewHolder(holder:TripViewHolder, position: Int) {
        holder.bind(data[position], navController)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onViewRecycled(holder: TripViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }
}

