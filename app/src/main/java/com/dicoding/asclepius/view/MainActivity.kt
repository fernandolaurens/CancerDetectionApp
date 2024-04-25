package com.dicoding.asclepius.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.FillEventHistory
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.viewModels
import com.dicoding.asclepius.data.local.HistoryEntity
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view_model_factory.HistoryViewModelFactory
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val historyViewModel : HistoryViewModel by viewModels {
        HistoryViewModelFactory.getInstanceHistoryViewModelFactoryForAnalyze(this)
    }
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let { analyzeImage(it, historyViewModel) } ?: showToast("Image not found")
        }
        binding.tombolhistori.setOnClickListener{
            val intent = Intent (this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startUCrop(uri : Uri) {
        val timestamp = Date().time
        val cachedImage = File(cacheDir, "cropped_image_${timestamp}.jpg")

        val destinationUri = Uri.fromFile(cachedImage)

        val uCrop = UCrop.of(uri, destinationUri).withAspectRatio(1f, 1f)
        uCrop.getIntent(this@MainActivity).apply {
            launchUCrop.launch(this)
        }
    }

    private val launchUCrop =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data!!)
                if (resultUri != null) {
                    currentImageUri = resultUri
                    showImage()
                }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val error = UCrop.getError(result.data!!)
                showToast("Error: ${error?.localizedMessage}")
            }
        }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startUCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        } ?: showToast("currentImageUri is null")
    }

    private fun analyzeImage(imageUri: Uri, historyVM: HistoryViewModel) {

            var historyEntity = HistoryEntity(label = "", confidenceScore = "", image = " ")


            try {

                    val imageClassifierHelper = ImageClassifierHelper(context = this@MainActivity,
                        classifierListener = object : ImageClassifierHelper.ClassifierListener {
                            override fun onError(error: String) {
                                showToast("Error: $error")
                            }

                            override fun onResults(
                                results: List<Classifications>?,
                                inferenceTime: Long
                            ) {
                                results?.let { listClassification ->
                                    if (listClassification.isNotEmpty() && listClassification[0].categories.isNotEmpty()) {
                                        val sortedCategories =
                                            listClassification[0].categories.sortedByDescending { it?.score }
                                        historyEntity = HistoryEntity(
                                            label = sortedCategories[0].label,
                                            confidenceScore = formatNumberToPercent(sortedCategories[0].score),
                                            image = imageUri.toString()
                                        )
                                        moveToResult(sortedCategories)
                                    }
                                }
                            }

                        })
                    imageClassifierHelper.classifyImage(imageUri)
                    historyVM.insertAnalyzeHistory(historyEntity)
//                    withContext(Dispatchers.Main) {
//                        showProgressAndDisableButtons(false)
//                    }

            } catch (e: Exception) {
                Log.d("MainActivity", e.message.toString())
            }

    }

    private fun moveToResult(analyzeResult: List<Category>) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("IMAGE", currentImageUri.toString())
        intent.putExtra("LABEL", analyzeResult[0].label)
        intent.putExtra("SCORE", formatNumberToPercent(analyzeResult[0].score))
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun formatNumberToPercent(score: Float): String =
        NumberFormat.getPercentInstance().format(score)
}