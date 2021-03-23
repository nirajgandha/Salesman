package com.genetic.salesman.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.genetic.salesman.R
import com.genetic.salesman.databinding.ActivityOtpBinding
import com.genetic.salesman.model.LoginResponse
import com.genetic.salesman.retrofit_api.APIClient
import com.genetic.salesman.utils.AppConstant
import com.genetic.salesman.utils.Preference
import com.genetic.salesman.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OtpActivity : AppCompatActivity() {

    private var _binding: ActivityOtpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundleExtra = intent.getBundleExtra(getString(R.string.otpBundle))!!
        val phone: String = bundleExtra.getString(getString(R.string.phone),"")
        val otp = bundleExtra.getString(getString(R.string.otp), "")!!
        binding.title.text = getString(R.string.otp_description, phone.substring(phone.length-4))
        binding.edtOtp.text = Editable.Factory.getInstance().newEditable(otp)
        binding.btnSubmit.setOnClickListener {
            val validationError = validateOtp()
            if (validationError == getString(R.string.empty_string)){
                callVerifyApi(phone, binding.edtOtp.text.toString())
            } else {
                showError(validationError)
            }
        }
    }

    private fun callVerifyApi(phone: String, otp: String) {
        Utils.showProgress(this)
        APIClient.getApiInterface().verifyOtpApi(phone, otp).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null){
                    val meta = body.meta
                    if (meta.code.equals("200", true)){
                        val data = body.loginData
                        val preference = Preference(this@OtpActivity)
                        preference.setString(AppConstant.DEALER_ID, data.id.toString())
                        preference.setString(AppConstant.PHONE, data.phone.toString())
                        preference.setString(AppConstant.TYPE_OF, data.type)
                        val intent = Intent(this@OtpActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showError(meta.message)
                    }
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun validateOtp(): String {
        return when {
            binding.edtOtp.text!!.isEmpty() -> {
                getString(R.string.otp_empty_error)
            }
            (binding.edtOtp.text!!.length < 4)-> {
                getString(R.string.otp_incomplete_error)
            }
            else -> {
                getString(R.string.empty_string)
            }
        }
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string)
    }
}