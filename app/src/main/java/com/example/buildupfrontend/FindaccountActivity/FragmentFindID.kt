package com.example.buildupfrontend.FindaccountActivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.buildupfrontend.FragmentSharedUser
import com.example.buildupfrontend.ViewModels.SignupViewModel
import com.example.buildupfrontend.retrofit.Client.FindIDService
import com.example.buildupfrontend.retrofit.Client.SocialAccessService
import com.example.buildupfrontend.retrofit.Response.FindIDResponse
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import kotlin.text.Typography.dagger


@AndroidEntryPoint
class FragmentFindID : FragmentSharedUser() {

    private val viewModel : SignupViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return super.getView()
    }
    override fun nextStep() {
        viewModel.userName = etName.text.toString()
        viewModel.userEmail = etEmail.text.toString()

        viewModel.userID = findID(viewModel.userName, viewModel.userEmail)

        (activity as FindaccountActivity?)!!.nextFragment(FragmentFindID2())
    }

    private fun findID(name: String, mail: String): String {
        val api = FindIDService.create()
        val body = FindIDService.body(name, mail)
        var message = ""

        api.post(body)
            .enqueue(object : retrofit2.Callback<FindIDResponse?> {
                override fun onResponse(call: Call<FindIDResponse?>, response: Response<FindIDResponse?>,
                ) {
                    message = if (response.code() != 200) {
                        Log.i("error", response.errorBody().toString())
                        response.errorBody().toString()
                    } else {
                        val responseBody = response.body()!!
                        if (!responseBody.success) {
                            Log.i("response error", responseBody.error.toString())
                        }
                        Log.i("response", responseBody.response.toString())
                        responseBody.response.username
                    }
                }
                override fun onFailure(p0: Call<FindIDResponse?>, error: Throwable) {

                    Log.i("error", error.message.toString())
                    message = error.message.toString()
                }
            })
        Log.i("message", message)

        return message
    }
}