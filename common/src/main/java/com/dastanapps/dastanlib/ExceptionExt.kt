package com.dastanapps.dastanlib

import com.google.firebase.FirebaseException

/**
 *
 * "Iqbal Ahmed" created on 24/07/2021
 */

fun ignoreException(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}