package com.hoffhaxx.app.concurs.misc.data

data class SignUpResult(val success : Boolean, val message : String, val errors : List<SignUpError>)