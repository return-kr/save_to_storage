# The library saves a file on the Internal Storage #
## Use of the library ##
### Add library dependency to the app level build.gradle file. ###

```gradle
dependencies {
    implementation 'com.github.return-kr:save_to_storage:$latest_stable_version'
}
```
### Add the following to the settings.gradle file. ###
```gradle
dependencyResolutionManagement {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
### Initialize the SaveToStorage class providing the Activity. ###
```kotlin
private var saveImageTask: SaveToStorage ? = null

saveImageTask = SaveToStorage(this@MainActivity)
```
### Set the listener to get the Save task callbacks. ###
```kotlin
saveImageTask?.addOnSaveListener(object : OnSaveListener {
    override fun onSaveSuccess(filePath: String) {
        Log.d("saveImageTask", "onSaveSuccess: ")
    }

    override fun onSaveFail(e: Exception) {
        Log.d("saveImageTask", "onSaveFail: ${e.message}")
    }
})
```
### Call the function to save the file by providing details on parameter. ###
```kotlin
saveImageTask?.saveImageToStorage(
    fileName = "test_image.png", 
    imageBitmap = myLogo, 
    directoryName = "MyTestDirectory"
)
```
### Call the function to delete a file by providing directory and filename. ###
```kotlin
saveImageTask?.deleteFile(
    fileName = "test_image.png",
    directoryName = "MyTestDirectory"
)
```
### Set the listener to get the delete task callbacks. ###
```kotlin
saveImageTask?.addOnDeleteListener(object : OnDeleteListener {
    override fun onFileDeleted() {
        Log.d("deleteTask", "onFileDeleted: ")
    }
    
    override fun onFileNotFound() {
        Log.d("deleteTask", "onFileNotFound: ")
    }

    override fun onDeleteException(e: Exception) {
        Log.d("deleteTask", "onDeleteException: ${e.message}")
    }
})
```
***End of Doc***