package com.example.uber.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(message: String){
    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: String){
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}

const val SERVER_URL = "https://parseapi.back4app.com/"
const val APPLICATION_ID = "iJC3BKG3Vj3wCFGyutFjD7BZrtWQ2Ff5eDPBbFtG"
const val CLIENT_KEY = "kwz9yFWGKCZWRqGqyR7PMAXtFUwGdizzIF0PjfK0"
const val REST_API_KEY = "I9UvbiaVGedZgfocuO54vnN0Qc4fpTe6Rq7wrpEn"
const val CONTENT_TYPE = "application/json"

