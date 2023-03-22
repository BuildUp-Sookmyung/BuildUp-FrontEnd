package com.example.buildupfrontend

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

var iconList= arrayListOf(R.drawable.ic_category_all_nor,
    R.drawable.ic_category_puzzle_nor,R.drawable.ic_category_trophy_nor,R.drawable.ic_category_badge_nor,R.drawable.ic_category_school_nor,R.drawable.ic_category_group_nor,R.drawable.ic_category_bulb_nor,R.drawable.ic_category_language_nor,R.drawable.ic_category_music_nor,
    R.drawable.ic_category_workout_nor,R.drawable.ic_category_movie_nor,R.drawable.ic_category_reading_nor,R.drawable.ic_category_suitcase_nor,R.drawable.ic_category_major_nor,R.drawable.ic_category_internship_nor,R.drawable.ic_category_document_nor,R.drawable.ic_category_interview_nor,
    R.drawable.ic_category_academy_nor,R.drawable.ic_category_contest_nor,R.drawable.ic_category_medal_nor,R.drawable.ic_category_oversea_nor,R.drawable.ic_category_clock_nor,R.drawable.ic_category_presentation_nor,R.drawable.ic_category_meal_nor,R.drawable.ic_category_briefcase_nor
)

class GlobalApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        prefs = PreferenceUtil(applicationContext)
    }
}