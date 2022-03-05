package com.example.fundooapp.model

import java.io.Serializable

data class Notes(
    var title: String = "",
    var content: String = "",
    var noteId: String = "",
    var userId: String= "",
    var archive: Boolean = false
) : Serializable
