package com.polito.s287503.showprofileactivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import org.json.JSONObject


class ShowProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_profile_activity)

        val et1 = findViewById<TextView>(R.id.FullName)
        val et2 = findViewById<TextView>(R.id.Nickname)
        val et3 = findViewById<TextView>(R.id.EmailAddress)
        val et4 = findViewById<TextView>(R.id.Location)
        val image = findViewById<ImageView>(R.id.imageView2)

        //val intent = intent

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        if (sharedPref.contains("profile")) {
            val defaultValue : String = resources.getString(R.string.saved_default_value)
            val jsonProfileData=JSONObject(sharedPref.getString("profile", defaultValue).toString())
            et1.text = jsonProfileData.get("group26.lab1.fullName").toString()
            et2.text = jsonProfileData.get("group26.lab1.nickName").toString()
            et3.text = jsonProfileData.get("group26.lab1.emailAddress").toString()
            et4.text = jsonProfileData.get("group26.lab1.postalAddress").toString()

            if (jsonProfileData.has("group26.lab1.imagePath")) {
                val path = jsonProfileData.get("group26.lab1.imagePath").toString()
                image.setImageURI(Uri.parse(path))
                image.tag = path
            }
        }
    }

    private fun editProfile(full_name:String, nick_name:String, email:String, postal_addr:String, image_path:String? = null){
        val intent = Intent(this, EditProfileActivity::class.java)
        if (image_path==null) {
        intent.also {
            it.putExtra("group26.lab1.fullName", full_name)
            it.putExtra("group26.lab1.nickName", nick_name)
            it.putExtra("group26.lab1.emailAddress", email)
            it.putExtra("group26.lab1.postalAddress", postal_addr)
        } } else {
            intent.also {
                it.putExtra("group26.lab1.fullName", full_name)
                it.putExtra("group26.lab1.nickName", nick_name)
                it.putExtra("group26.lab1.emailAddress", email)
                it.putExtra("group26.lab1.postalAddress", postal_addr)
                it.putExtra("group26.lab1.imagePath", image_path)
        } }
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            val et1 = findViewById<TextView>(R.id.FullName)
            val et2 = findViewById<TextView>(R.id.Nickname)
            val et3 = findViewById<TextView>(R.id.EmailAddress)
            val et4 = findViewById<TextView>(R.id.Location)
            et1.text = data?.getStringExtra("group26.lab1.fullName")
            et2.text = data?.getStringExtra("group26.lab1.nickName")
            et3.text = data?.getStringExtra("group26.lab1.emailAddress")
            et4.text = data?.getStringExtra("group26.lab1.postalAddress")

            if (data?.hasExtra("group26.lab1.imagePath")==true) {
                val image = findViewById<ImageView>(R.id.imageView2)
                val path=data.getStringExtra("group26.lab1.imagePath")
                val imageBitmap = data.getParcelableExtra<Bitmap>("group26.lab1.imageBitmap")
                image.setImageBitmap(imageBitmap)
                image.tag = path
                // Persist information when the EditProfileActivity returns data

                val jsonProfile=JSONObject()
                jsonProfile.put("group26.lab1.fullName", et1.text)
                jsonProfile.put("group26.lab1.nickName", et2.text)
                jsonProfile.put("group26.lab1.emailAddress", et3.text)
                jsonProfile.put("group26.lab1.postalAddress", et4.text)
                jsonProfile.put("group26.lab1.imagePath", path)

                val toStore = jsonProfile.toString()

                val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
                with (sharedPref.edit()) {
                    putString("profile", toStore)
                    apply()
                }
            }

            else {

                // Persist information when the EditProfileActivity returns data
                val jsonProfile=JSONObject()
                jsonProfile.put("group26.lab1.fullName", et1.text)
                jsonProfile.put("group26.lab1.nickName", et2.text)
                jsonProfile.put("group26.lab1.emailAddress", et3.text)
                jsonProfile.put("group26.lab1.postalAddress", et4.text)

                val toStore = jsonProfile.toString()

                val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
                with (sharedPref.edit()) {
                    putString("profile", toStore)
                    apply()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.pencil, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.title.equals("Edit")){

        val et1 = findViewById<TextView>(R.id.FullName)
        val et2 = findViewById<TextView>(R.id.Nickname)
        val et3 = findViewById<TextView>(R.id.EmailAddress)
        val et4 = findViewById<TextView>(R.id.Location)
        val image = findViewById<ImageView>(R.id.imageView2)
        if (image.tag!=null) {
            val imagePath = image.tag.toString()
            editProfile(et1.text.toString(), et2.text.toString(), et3.text.toString(), et4.text.toString(), imagePath)
            } else {
                editProfile(et1.text.toString(), et2.text.toString(), et3.text.toString(), et4.text.toString()) }
    }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val et1 = findViewById<TextView>(R.id.FullName)
        val et2 = findViewById<TextView>(R.id.Nickname)
        val et3 = findViewById<TextView>(R.id.EmailAddress)
        val et4 = findViewById<TextView>(R.id.Location)
        val image = findViewById<ImageView>(R.id.imageView2)
        outState.putString("group26.lab1.fullName", et1.text.toString())
        outState.putString("group26.lab1.nickname", et2.text.toString())
        outState.putString("group26.lab1.email", et3.text.toString())
        outState.putString("group26.lab1.location", et4.text.toString())
        if (image.tag!=null) {
            val imagePath = image.tag.toString()
            outState.putString("group26.lab1.imagePath", imagePath) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val et1 = findViewById<TextView>(R.id.FullName)
        val et2 = findViewById<TextView>(R.id.Nickname)
        val et3 = findViewById<TextView>(R.id.EmailAddress)
        val et4 = findViewById<TextView>(R.id.Location)
        val image = findViewById<ImageView>(R.id.imageView2)
        val imagePath = savedInstanceState.getString("group26.lab1.imagePath")
        et1.text=savedInstanceState.getString("group26.lab1.fullName")
        et2.text=savedInstanceState.getString("group26.lab1.nickname")
        et3.text=savedInstanceState.getString("group26.lab1.email")
        et4.text=savedInstanceState.getString("group26.lab1.location")
        if (imagePath!=null) {
            image.setImageURI(Uri.parse(imagePath))
            image.tag = imagePath
        }
    }
}