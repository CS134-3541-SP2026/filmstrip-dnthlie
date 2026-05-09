package edu.cs134.scrapbook

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import androidx.core.graphics.scale
import java.io.FileOutputStream

@Composable
fun ScrapbookScreen(viewModel: ScrapbookViewModel,
                    modifier: Modifier = Modifier) {
    var selectedSlot by rememberSaveable { mutableStateOf<Int?>(null) }
    val context = LocalContext.current  // add context

    // FIN: Craete a launcher
    // NOTE: ActivityResultContracts - is an Android API from jetpack.
    // Allows setup of camera app to launch. Helps assign variable name to use as a tool/function
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts
            .TakePicturePreview()
    ) { image ->
        image?.let {
            // if selectedSlot is not null, can set/update image
            if (selectedSlot != null) {
                viewModel.setPhoto(selectedSlot!!, it)
                //MANUALLY SAVE FILES TO SLOT
                val file = File(context.externalCacheDir, "slot_${selectedSlot}.jpg")
                FileOutputStream(file).use {out ->
                    it.compress(Bitmap.CompressFormat.JPEG, 80, out)
                }
            } else {
                // else add new photo
                viewModel.addPhoto(it)
                val slot = viewModel.photos.size - 1
                val file = File(context.externalCacheDir, "slot_$slot.jpg")
                FileOutputStream(file).use { out ->
                    it.compress(Bitmap.CompressFormat.JPEG, 80, out)
                }
            }
        }
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        viewModel.photos.forEachIndexed { index, photo ->
            ScrapbookSlot (
                photo = photo,
                onClick = {
                    selectedSlot = index
                    cameraLauncher.launch(null)
                }
            )
        }
        ScrapbookSlot(
            photo = null,
            onClick = {
                selectedSlot = null
                cameraLauncher.launch(null)
            }
        )
    }
}

//OLD:
//@Composable
//fun ScrapbookScreen(viewModel: ScrapbookViewModel,
//                    modifier: Modifier = Modifier) {
//    var selectedSlot by rememberSaveable { mutableStateOf<Int?>(null) }
//    val context = LocalContext.current  // add context
//    var photoURI by remember {mutableStateOf<Uri?>(null)}
//
//
//    // FIN: Craete a launcher
//    // NOTE: ActivityResultContracts - is an Android API from jetpack.
//    // Allows setup of camera app to launch. Helps assign variable name to use as a tool/function
//    val cameraLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts
//            .TakePicturePreview()
//    ) { image ->
//        image?.let {
//            // if selectedSlot is not null, can set/update image
//            if (selectedSlot != null) {
//                viewModel.setPhoto(selectedSlot!!, it)
//            } else {
//                // else add new photo
//                viewModel.addPhoto(it)
//            }
//        }
//    }
//
//    //Save photos
//    val photoURI = remember {mutableStateOf<Uri?>(null)}
//
//    Column (
//        modifier = modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//    ){
//        // EXISTING PHOTOS FOR VIEW
//        viewModel.photos.forEachIndexed { index, photo ->
//            ScrapbookSlot (
//                photo = photo,
//                onClick = {
//                    selectedSlot = index
//                    cameraLauncher.launch(null)
//                }
//            )
//        }
//        ScrapbookSlot(
//            photo = null,
//            onClick = {
//                selectedSlot = null
//                cameraLauncher.launch(null)
//            }
//        )
//    }
//}
