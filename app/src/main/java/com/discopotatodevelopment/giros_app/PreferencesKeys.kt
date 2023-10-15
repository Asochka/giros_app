package com.discopotatodevelopment.giros_app

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object PreferencesKeys {
    val isLogin = booleanPreferencesKey("isLogin")
    val isPolicyAccepted = booleanPreferencesKey("isPolicyAccepted")
    val userID = intPreferencesKey("userID")
}