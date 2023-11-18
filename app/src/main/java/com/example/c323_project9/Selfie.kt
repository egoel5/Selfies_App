package com.example.c323_project9

import com.google.firebase.database.Exclude

data class Selfie(
    @get:Exclude
    var selfieId: String = "",
    var selfieUrl: String = ""
)
