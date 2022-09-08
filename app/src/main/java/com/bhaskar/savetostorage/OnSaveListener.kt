package com.bhaskar.savetostorage

interface OnSaveListener {
    fun onSaveSuccess(filePath: String)
    fun onSaveFail(e: Exception)
}