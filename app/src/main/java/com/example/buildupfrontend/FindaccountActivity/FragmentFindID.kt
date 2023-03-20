package com.example.buildupfrontend.FindaccountActivity

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.buildupfrontend.FragmentSharedUser
import com.example.buildupfrontend.ViewModels.SignupViewModel
import com.example.buildupfrontend.retrofit.Client.FindIDService
import com.example.buildupfrontend.retrofit.Request.FindIDRequest
import com.example.buildupfrontend.retrofit.Response.FindIDError
import com.example.buildupfrontend.retrofit.Response.FindIDErrorResponse
import com.example.buildupfrontend.retrofit.Response.FindIDResponse
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

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

        findID(viewModel.userName, viewModel.userEmail)
    }

    private fun findID(name: String, mail: String) {

        FindIDService.getRetrofit(FindIDRequest(name, mail)).enqueue(object: retrofit2.Callback<FindIDResponse> {
            override fun onResponse(call: Call<FindIDResponse>, response: Response<FindIDResponse>){
                if (response.isSuccessful) {
                    Log.e("Find id response", response.body().toString())
                    viewModel.userID = response.body()?.response.toString()
                    (activity as FindaccountActivity?)!!.nextFragment(FragmentFindID2())

                } else {
                    try {
                        val body = Gson().fromJson(response.errorBody()!!.string(), FindIDErrorResponse::class.java)
                        Log.e(ContentValues.TAG, "body : $body")
                        tlVerify.error = body.error.errorMessage
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<FindIDResponse>, t: Throwable) {
                Log.e("find id failure", t.message.toString())
            }
        })
    }
}