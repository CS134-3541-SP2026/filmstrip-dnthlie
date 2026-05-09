package edu.cs134.scrapbook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File

class ScrapbookViewModel : ViewModel() {
    var photos by mutableStateOf<List<Bitmap?>>(emptyList())

    fun setPhoto(slot: Int, image: Bitmap) {
        //NEW LIST WITH UPDATED PHOTO todo
        val newList = photos.toMutableList()

        //WHILE LIST INDEX IS JLE, NULL EMPTY SLOTS
        while(newList.size <= slot) {
            newList.add(null)
        }

        //SET PHOTOS TO SLOT
        newList[slot] = image
        photos = newList
    }

    //FUNCTION FOR NEW PHOTOS
    fun addPhoto(image: Bitmap) {
        val newList = photos.toMutableList()
        newList.add(image)
        photos = newList
    }

    //LOAD PHOTOS
    fun refreshPhoto(slot: Int, context: Context) {
        //NOTE: "externalCacheDir" or whatever term must match what's in ScrapbookScreen!!!!!!
        val file = File(context.externalCacheDir, "slot_$slot.jpg")
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)

            setPhoto(slot, bitmap)
        }
    }
}