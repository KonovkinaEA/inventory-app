package com.example.inventoryapp.util

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.millisToDate(): String =
    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(this)

