package com.discopotatodevelopment.giros_app.dataCoordinator

import androidx.datastore.preferences.core.edit
import com.discopotatodevelopment.giros_app.PreferencesKeys
import kotlinx.coroutines.flow.firstOrNull

// MARK: Sample String Functionality
// Please note that the DataStore functionality must be called within a couroutine.
//suspend fun DataCoordinator.getSampleStringDataStore(): String {
//    val context = this.context ?: return defaultSampleStringPreferenceValue
//    return context.dataStore.data.firstOrNull()?.get(PreferencesKeys.)
//        ?: defaultSampleStringPreferenceValue
//}
//
//suspend fun DataCoordinator.setSampleStringDataStore(value: String) {
//    val context = this.context ?: return
//    context.dataStore.edit { preferences ->
//        preferences[PreferencesKeys.sampleString] = value
//    }
//}

// MARK: Sample Int Functionality
// Please note that the DataStore functionality must be called within a couroutine.
suspend fun DataCoordinator.getuserID(): Int {
    val context = this.context ?: return defaultSampleIntPreferenceVariable
    return context.dataStore.data.firstOrNull()?.get(PreferencesKeys.userID)
        ?: defaultSampleIntPreferenceVariable
}

suspend fun DataCoordinator.setuserID(value: Int) {
    val context = this.context ?: return
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.userID] = value
    }
}

// MARK: Sample Boolean Functionality
// Please note that the DataStore functionality must be called within a couroutine.
suspend fun DataCoordinator.getisLogin(): Boolean {
    val context = this.context ?: return defaultSampleBooleanPreferenceVariable
    return context.dataStore.data.firstOrNull()?.get(PreferencesKeys.isLogin)
        ?: defaultSampleBooleanPreferenceVariable
}

suspend fun DataCoordinator.setisLogin(value: Boolean) {
    val context = this.context ?: return
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.isLogin] = value
    }
}

suspend fun DataCoordinator.getisPolicyAccepted(): Boolean {
    val context = this.context ?: return defaultSampleBooleanPreferenceVariable
    return context.dataStore.data.firstOrNull()?.get(PreferencesKeys.isPolicyAccepted)
        ?: defaultSampleBooleanPreferenceVariable
}

suspend fun DataCoordinator.setisPolicyAccepted(value: Boolean) {
    val context = this.context ?: return
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.isPolicyAccepted] = value
    }
}