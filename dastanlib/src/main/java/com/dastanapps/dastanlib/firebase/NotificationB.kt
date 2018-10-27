package com.dastanapps.dastanlib.firebase

data class NotificationB(
        var title: String,
        var content: String,
        var banner_url: String,
        var push_type: String) {
    constructor() : this("", "", "", "")
}