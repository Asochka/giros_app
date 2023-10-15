package com.discopotatodevelopment.giros_app.dataCoordinator

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// MARK: DataStore Update Functionality
//fun DataCoordinator.updateSampleString(value: String) {
//    // Update Value
//    this.sampleStringPreferenceVariable = value
//    // Save to System
//    GlobalScope.launch(Dispatchers.Default) {
//        // Update DataStore
//        setSampleStringDataStore(value)
//        // OPTIONAL - Send Broadcast
//        // Not included in this tutorial - consult the ReadMe to learn how to setup notifications to alert your system.
//    }
//}

fun DataCoordinator.updateuserID(value: Int) {
    // Update Value
    this.sampleIntPreferenceVariable = value
    // Save to System
    GlobalScope.launch(Dispatchers.Default) {
        // Update DataStore
        setuserID(value)
        // OPTIONAL - Send Broadcast
        // Not included in this tutorial - consult the ReadMe to learn how to setup notifications to alert your system.
    }
}

fun DataCoordinator.updateisLogin(value: Boolean) {
    // Update Value
    this.sampleBooleanPreferenceVariable = value
    // Save to System
    GlobalScope.launch(Dispatchers.Default) {
        // Update DataStore
        setisLogin(value)
        // OPTIONAL - Send Broadcast
        // Not included in this tutorial - consult the ReadMe to learn how to setup notifications to alert your system.
    }
}

fun DataCoordinator.updateisPolicyAccepted(value: Boolean) {
    // Update Value
    this.sampleBooleanPreferenceVariable = value
    // Save to System
    GlobalScope.launch(Dispatchers.Default) {
        // Update DataStore
        setisPolicyAccepted(value)
        // OPTIONAL - Send Broadcast
        // Not included in this tutorial - consult the ReadMe to learn how to setup notifications to alert your system.
    }
}