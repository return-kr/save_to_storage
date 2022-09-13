package com.bhaskar.savetostorage

interface OnDeleteListener {
    fun onFileDeleted()
    fun onFileNotFound()
    fun onDeleteException(e: Exception)
}