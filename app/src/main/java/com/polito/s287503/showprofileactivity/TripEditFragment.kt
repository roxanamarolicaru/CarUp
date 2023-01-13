package com.polito.s287503.showprofileactivity

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import coil.load
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream


class TripEditFragment : Fragment(R.layout.fragment_trip_edit), DatePickerDialog.OnDateSetListener {

    lateinit var id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /********************************OPTION MENU*************************/
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_trip_edit, container, false)
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val sharedPref = this.activity?.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref?.contains("tripList") == true) {
            val jsonTripList = JSONObject(sharedPref?.getString("tripList", "null").toString())
            var editing: Boolean = false // Check if we're in edit mode or creating a new trip

            if (arguments?.containsKey("TripID") == true) {
                if (arguments?.getString("TripID") != "null") {
                    editing = true
                    id = arguments?.getString("TripID")!!
                } else {
                    id = UUID.randomUUID().toString()
                    while (jsonTripList.has(id)) {
                        id = UUID.randomUUID().toString()
                    }
                }
            } else {
                id = UUID.randomUUID().toString()
                while (jsonTripList.has(id)) {
                    id = UUID.randomUUID().toString()
                }
            }

            if (editing && jsonTripList.has(id)) {

                val thisTrip = JSONObject(jsonTripList[id].toString())

                val pickUpLocation = view.findViewById<EditText>(R.id.textViewPickupLocation)
                val dropLocation = view.findViewById<EditText>(R.id.textViewDropLocation)
                val price = view.findViewById<EditText>(R.id.Price)
                val avaSeats = view.findViewById<EditText>(R.id.textViewAvailableSeats)
                val addInfo = view.findViewById<EditText>(R.id.AddInfo)
                val data = view.findViewById<EditText>(R.id.textViewDate)
                val pickUpHour = view.findViewById<EditText>(R.id.textViewDepartureHour)
                val dropHour = view.findViewById<EditText>(R.id.textViewArrivalHour)
                val carPhoto = view.findViewById<ImageView>(R.id.imageViewCar)

                pickUpLocation.setText(thisTrip["pickUpLocation"].toString())
                dropLocation.setText(thisTrip["dropLocation"].toString())
                price.setText(thisTrip["price"].toString())
                avaSeats.setText(thisTrip["avaSeats"].toString())
                addInfo.setText(thisTrip["addInfo"].toString())
                data.setText(thisTrip["data"].toString())
                pickUpHour.setText(thisTrip["pickUpHour"].toString())
                dropHour.setText(thisTrip["dropHour"].toString())
                if (thisTrip["carPhotoPath"] != "null") carPhoto.setImageURI(Uri.parse(thisTrip["carPhotoPath"].toString()))
            }
        } else {
            id = UUID.randomUUID().toString() // First trip, id can be whatever
        }

        /*3*******************floatContexMenu**********************/
        val buttonCamera = view.findViewById<ImageButton>(R.id.photo_selector)
        registerForContextMenu(buttonCamera)
        /**********************************************************/

        /*2******************************DATA PICKER******************************2*/

        val dataButton = view.findViewById<EditText>(R.id.textViewDate)

        dataButton.setOnClickListener {
            val c = Calendar.getInstance()
            val dataPicker = DatePickerDialog(it.context, { _, i, i2, i3 ->

                dataButton.setText("$i3/${i2+1}/$i")

            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))


            dataPicker.show()
        }
        /*******************************************************************************/

        /****************USA IL TIME PICKER PER SELEZIONARE L'ORA'**********************/
        /*val timeButtonDeparture = view.findViewById<EditText>(R.id.textViewDepartureHour)

        timeButtonDeparture.setOnClickListener {
            val c = Calendar.getInstance()
            val tp = TimePickerDialog(it.context, TimePickerDialog.OnTimeSetListener { _, i, i1 ->

                timeButtonDeparture.setText("h:$i m:$i1")

            }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true)

            tp.show()
        }*/
        /*******************************************************************************/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {

            val imageBitmap = data!!.extras?.get("data") as Bitmap
            val imageView = view?.findViewById<ImageView>(R.id.imageViewCar)
            val imgFile = File(context?.filesDir, "group26.lab2.$id.carPhoto.png") // Store the image everytime it is returned from the activity
            val fo=FileOutputStream(imgFile)
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fo)
            fo.flush()
            fo.close()
            val filepath=imgFile.absolutePath.toString()
            //imageView?.setImageURI(Uri.parse(filepath))
            imageView?.setImageBitmap(imageBitmap)
            imageView?.tag = filepath
        }
    }


    /*3*************************************floatContexMenu************************************3*/
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.activity?.menuInflater?.inflate(R.menu.floating_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionCamera -> {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent, 2)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this.context, "No camera found", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    /*3******************************************************************************************3*/

    /*2********************************######****************************************2*/
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

    }
    /*2******************************************************************************2*/

    /*4**************************************OPTION MENU************************************4*/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
        return;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Salvo i contenuti degli editText nel bundle
        val pickUpLocation = view?.findViewById<EditText>(R.id.textViewPickupLocation)
        val dropLocation = view?.findViewById<EditText>(R.id.textViewDropLocation)
        val price = view?.findViewById<EditText>(R.id.Price)
        val avaSeats = view?.findViewById<EditText>(R.id.textViewAvailableSeats)
        val addInfo = view?.findViewById<EditText>(R.id.AddInfo)
        val data = view?.findViewById<EditText>(R.id.textViewDate)
        val pickUpHour = view?.findViewById<EditText>(R.id.textViewDepartureHour)
        val dropHour = view?.findViewById<EditText>(R.id.textViewArrivalHour)
        val carPhoto = view?.findViewById<ImageView>(R.id.imageViewCar)

        val bundle = bundleOf(

                "ID" to id,
                "pickUpLocation" to pickUpLocation?.text.toString(),
                "dropLocation" to dropLocation?.text.toString(),
                "price" to price?.text.toString(),
                "avaSeats" to avaSeats?.text.toString(),
                "addInfo" to addInfo?.text.toString(),
                "data" to data?.text.toString(),
                "pickUpHour" to pickUpHour?.text.toString(),
                "dropHour" to dropHour?.text.toString(),
                "carPhotoPath" to carPhoto?.tag.toString(),
                "carPhotoBit" to carPhoto?.drawable?.toBitmap()
        )

        // Salvo i dati nella memoria locale: assegno un unique ID ad ogni bundle di informazioni, così che siano univocamente accessibili

        val sharedPref = this.activity?.getPreferences(Context.MODE_PRIVATE)
        val jsonTrip = JSONObject()
        //jsonTrip.put("ID", id)
        jsonTrip.put("pickUpLocation", pickUpLocation?.text.toString())
        jsonTrip.put("dropLocation", dropLocation?.text.toString())
        jsonTrip.put("price", price?.text.toString())
        jsonTrip.put("avaSeats", avaSeats?.text.toString())
        jsonTrip.put("addInfo", addInfo?.text.toString())
        jsonTrip.put("data", data?.text.toString())
        jsonTrip.put("pickUpHour", pickUpHour?.text.toString())
        jsonTrip.put("dropHour", dropHour?.text.toString())
        if (carPhoto?.tag!= null) {
            jsonTrip.put("carPhotoPath", carPhoto.tag.toString()) }
        else {
            jsonTrip.put("carPhotoPath", "null")
        }

        if (sharedPref?.contains("tripList") == true) {
            val jsonTripList = JSONObject(sharedPref.getString("tripList", "null").toString())
            jsonTripList.put(id, jsonTrip)
            with(sharedPref.edit()){
                putString("tripList", jsonTripList.toString())
                apply()
            }
        } else {
            val tripList = JSONObject()
            tripList.put(id, jsonTrip)
            with (sharedPref?.edit()) {
                this?.putString("tripList", tripList.toString())
                this?.apply()
            }
        }

        //invio il bundle al fragment di destinazione
        findNavController().navigate(R.id.action_tripEditFragment_to_tripDetailsFragment, bundle)
        return super.onOptionsItemSelected(item)
    }
    /*4*************************************************************************************4*/
}