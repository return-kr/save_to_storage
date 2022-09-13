package com.bhaskar.savetostorage

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import java.io.File
import java.io.FileOutputStream

/**
 * This library saves a file on the internal storage
 *                                        -- Bhaskar
 */
class SaveToStorage (private val activity: Activity) {
    companion object {
        const val TAG = "SaveToStorage"
    }
    private var onSaveListener: OnSaveListener ? = null
    private var onDeleteListener: OnDeleteListener ? = null

    /**
     * Registering to listener to get Save callbacks
     */
    fun addOnSaveListener(onSaveListener: OnSaveListener) {
        this.onSaveListener = onSaveListener
    }

    /**
     * Registering to listener to get Delete task callbacks
     */
    fun addOnDeleteListener(onDeleteListener: OnDeleteListener) {
        this.onDeleteListener = onDeleteListener
    }

    /**
     * Setting the output or target file directory
     */
    private fun setOutputDirectory(directoryName: String): File {
        val directory = activity.externalMediaDirs.firstOrNull()?.let {
            File(it, directoryName).apply { mkdirs() }
        }
        return if (directory != null && directory.exists())
            directory else activity.filesDir
    }

    /**
     * Function which starts the process to save the image-file
     */
    fun saveImageToStorage(directoryName: String, fileName: String, imageBitmap: Bitmap) {
        val outputDirectory: File = setOutputDirectory(directoryName)
        val targetFile = File("$outputDirectory/$fileName")
        if (targetFile.exists()) {
            targetFile.delete()
        }
        try {
            val out = FileOutputStream(targetFile)
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            onSaveListener?.onSaveSuccess("$outputDirectory/$fileName")
        } catch (e: Exception) {
            onSaveListener?.onSaveFail(e)
            Log.d(TAG, "saveImageToStorage: ${e.message}")
        }
    }

    /**
     * Function to delete a file from storage
     */
    fun deleteFile (directoryName: String, fileName: String) {
        val directory = setOutputDirectory(directoryName)
        val targetFile = File(directory, fileName)
        try {
            if (targetFile.exists()) {
                targetFile.delete()
                onDeleteListener?.onFileDeleted()
            } else {
                onDeleteListener?.onFileNotFound()
            }
        } catch (e: Exception) {
            onDeleteListener?.onDeleteException(e)
        }
    }
}