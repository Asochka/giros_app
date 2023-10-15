package com.discopotatodevelopment.giros_app.dataCoordinator

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DataCoordinator {
    companion object {
        @SuppressLint("StaticFieldLeak")
        val shared = DataCoordinator()
        const val identifier = "[DataCoordinator]"
    }
    // MARK: Variables
    var context: Context? = null
    // Create a variable for each preference, along with a default value.
    // This is to guarantee that if it can't find it it resets to a value that you can control.
    /// Sample String
    var sampleStringPreferenceVariable: String = ""
    val defaultSampleStringPreferenceValue: String = ""
    /// Sample Int
    var sampleIntPreferenceVariable: Int = 0
    val defaultSampleIntPreferenceVariable: Int = 0
    /// Sample Boolean
    var sampleBooleanPreferenceVariable:  Boolean = false
    val defaultSampleBooleanPreferenceVariable: Boolean = false

    // MARK: Data Store Variables
    private val USER_PREFERENCES_NAME = "user_preferences"
    val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    // MARK: Lifecycle

    fun initialize(context: Context, onLoad: () -> Unit) {
        // Set Context
        this.context = context
        // Load DataStore Settings
        GlobalScope.launch(Dispatchers.Default) {
            // Update Sample String
            //sampleStringPreferenceVariable = getSampleStringDataStore()
            // Update Sample Int
            sampleIntPreferenceVariable = getuserID()
            // Update Sample Boolean
            sampleBooleanPreferenceVariable = getisLogin()
            sampleBooleanPreferenceVariable = getisPolicyAccepted()
            // Log the variables to confirm that they loaded correctly
            // Callback
            onLoad()
        }
    }
}