package com.polito.s287503.showprofileactivity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.AdapterContextMenuInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import java.io.File
import java.io.FileOutputStream


class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        val buttonCamera = findViewById<ImageButton>(R.id.imageButton)
        val button = findViewById<Button>(R.id.button)
        val fullName = findViewById<EditText>(R.id.editTextFullName)
        val nickName = findViewById<EditText>(R.id.editTextNickName)
        val emailAddress = findViewById<EditText>(R.id.editTextEmailAddress)
        val postalAddress = findViewById<EditText>(R.id.editTextPostalAddress)
        val photo = findViewById<ImageView>(R.id.imageView2)

        registerForContextMenu(buttonCamera)

        fullName.setText(intent.extras?.getString("group26.lab1.fullName"))
        nickName.setText(intent.extras?.getString("group26.lab1.nickName"))
        emailAddress.setText(intent.extras?.getString("group26.lab1.emailAddress"))
        postalAddress.setText(intent.extras?.getString("group26.lab1.postalAddress"))
        if (intent.hasExtra("group26.lab1.imagePath")) {
            val imagePath=intent.extras?.getString("group26.lab1.imagePath")
            photo.setImageURI(Uri.parse(imagePath))
            photo.tag = imagePath
        }

        button.setOnClickListener {

            if (photo.drawable!=null && photo.drawable is BitmapDrawable) {
                val str1 = fullName.text.toString()
                val str2 = nickName.text.toString()
                val str3 = emailAddress.text.toString()
                val str4 = postalAddress.text.toString()
                val imagePath = photo.tag.toString()
                val imageBitmap = photo.drawable.toBitmap()

            setResult(Activity.RESULT_OK, Intent().also {
                it.putExtra("group26.lab1.fullName", str1)
                it.putExtra("group26.lab1.nickName", str2)
                it.putExtra("group26.lab1.emailAddress", str3)
                it.putExtra("group26.lab1.postalAddress", str4)
                it.putExtra("group26.lab1.imagePath", imagePath)
                it.putExtra("group26.lab1.imageBitmap", imageBitmap)
            }) }
            else {
                val str1 = fullName.text.toString()
                val str2 = nickName.text.toString()
                val str3 = emailAddress.text.toString()
                val str4 = postalAddress.text.toString()

                setResult(Activity.RESULT_OK, Intent().also {
                    it.putExtra("group26.lab1.fullName", str1)
                    it.putExtra("group26.lab1.nickName", str2)
                    it.putExtra("group26.lab1.emailAddress", str3)
                    it.putExtra("group26.lab1.postalAddress", str4)
                })
            }
            finish()
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.floating_context_menu, menu)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionCamera -> {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent, 2)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "No camera found", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {

            val imageBitmap = data!!.extras?.get("data") as Bitmap
            val imageView = findViewById<ImageView>(R.id.imageView2)
            val imgFile = File(applicationContext.filesDir, "group26.lab1.profilePic.png") // Store the image everytime it is returned from the activity
            val fo=FileOutputStream(imgFile)
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fo)
            fo.flush()
            fo.close()
            val filepath=imgFile.absolutePath.toString()
            imageView.setImageBitmap(imageBitmap)
            imageView.tag = filepath

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val image = findViewById<ImageView>(R.id.imageView2)
        if (image.tag!=null) {
            val path=image.tag.toString()
            outState.putString("path", path) }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val image = findViewById<ImageView>(R.id.imageView2)
        val path =savedInstanceState.getString("path")
        if (path!=null) {
            image.setImageURI(Uri.parse(path))
            image.tag = path
        }
    }
}


