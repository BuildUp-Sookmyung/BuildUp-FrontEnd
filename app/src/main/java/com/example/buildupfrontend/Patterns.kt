package com.example.buildupfrontend

import java.util.regex.Pattern

interface Patterns {
    val KorEng: Pattern
        get() = Pattern.compile("^[a-zA-Zㄱ-ㅎ가-힣]+$")
    val KorEngSpace: Pattern
        get() = Pattern.compile("^[a-zA-Zㄱ-ㅎ가-힣\\s]+$")
    val Num: Pattern
        get() = Pattern.compile("^[0-9]+$")
    val Num1: Pattern
        get() = Pattern.compile("^[1-4]{1}$")
    val Num6: Pattern
        get() = Pattern.compile("^[0-9]{6}$")
    val Num11: Pattern
        get() = Pattern.compile("^[0-9]{11}$")
    val typeID: Pattern
        get() = Pattern.compile("^[a-z0-9]{5,20}+$")
    val typeEmail: Pattern
        get() = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
}