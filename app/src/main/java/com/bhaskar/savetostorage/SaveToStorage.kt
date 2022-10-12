package com.bhaskar.savetostorage

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
     * Function to create target file instance
     */
    private fun getFile(directoryName: String, fileName: String): File {
        val outputDirectory: File = setOutputDirectory(directoryName)
        val targetFile = File("$outputDirectory/$fileName")
        if (targetFile.exists()) {
            targetFile.delete()
        }
        return targetFile
    }

    /**
     * Function to convert ByteArray into Bitmap
     */
    fun byteArrayToBitmap(imageByteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
    }

    /**
     * Function which starts the process to save the image-file
     */
    fun saveImageToStorage(directoryName: String, fileName: String, imageBitmap: Bitmap) {
        val targetFile = getFile(directoryName, fileName)
        try {
            val out = FileOutputStream(targetFile)
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            onSaveListener?.onSaveSuccess(targetFile.absolutePath)
        } catch (e: Exception) {
            onSaveListener?.onSaveFail(e)
            Log.d(TAG, "saveImageToStorage: ${e.message}")
        }
    }

    /**
     * Function to save content in a Text file
     */
    fun saveTextFileToStorage(text: String, directoryName: String, fileName: String) {
        val targetFile = getFile(directoryName, "$fileName.txt")
        try {
            val out = FileOutputStream(targetFile)
            out.write(text.toByteArray())
            out.flush()
            out.close()
            onSaveListener?.onSaveSuccess(targetFile.absolutePath)
        } catch (e: Exception) {
            onSaveListener?.onSaveFail(e)
        }
    }

    /**
     * Function to delete a file from storage
     */
    fun deleteFile (directoryName: String, fileName: String) {
        val directory = activity.externalMediaDirs.firstOrNull()?.let {
            File(it, directoryName).apply { mkdirs() }
        }
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