package com.tim.lib.download

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

// The name of the image manipulation work
const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"

// Other keys
const val KEY_IMAGE_URI = "KEY_IMAGE_URI"
const val TAG_OUTPUT = "OUTPUT"

const val DELAY_TIME_MILLIS: Long = 3000
class DownloadViewModel(application: Application) : ViewModel() {

    private val workManager = WorkManager.getInstance(application)

    internal fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        builder.putString(KEY_IMAGE_URI, "Tim wowowo")
        return builder.build()
    }

    fun testRun() {
        val constraints = Constraints.Builder().build()
        val saveBuilder = OneTimeWorkRequestBuilder<CSVDownloader>()
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)

        saveBuilder.setInputData(createInputDataForUri())
        val continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                saveBuilder.build()
            )

        continuation.enqueue()
    }

}

class DownloadViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DownloadViewModel::class.java)) {
            DownloadViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}