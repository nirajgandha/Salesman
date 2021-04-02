package com.genetic.salesman.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.genetic.salesman.R
import com.genetic.salesman.activity.MainActivity
import com.genetic.salesman.adapter.DailyReportListFragmentAdapter
import com.genetic.salesman.databinding.FragmentDailyReportToAdminBinding
import com.genetic.salesman.model.AddUpdateDailyReportResponse
import com.genetic.salesman.model.DailyReportData
import com.genetic.salesman.model.DailyReportDetailResponse
import com.genetic.salesman.model.DailyReportListResponse
import com.genetic.salesman.retrofit_api.APIClient
import com.genetic.salesman.utils.AppConstant
import com.genetic.salesman.utils.Preference
import com.genetic.salesman.utils.Utils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*


class DailyReportToAdmin : Fragment() {
    private var _binding: FragmentDailyReportToAdminBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val REQUEST_CODE_FOR_DEALER_FARMER_SELFIE: Int = 1250
    private val REQUEST_CODE_FOR_START_KM_PIC: Int = 1251
    private val REQUEST_CODE_FOR_END_KM_PIC: Int = 1252
    private var selfieFileName: String = ""
    private var startKmFileName: String = ""
    private var endKmFileName: String = ""
    private var isEdit = false
    private var id_daily_report = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyReportToAdminBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = Preference(requireContext())
        binding.salesManId.editText?.setText(preference?.getString(AppConstant.SALESMAN_ID, ""))
        val argument = arguments
        if (argument != null) {
            id_daily_report = argument.getString("editDailyReport", "")
            if (id_daily_report.isNotEmpty()) {
                isEdit = true
                callGetDailyReportDetailApi()
            }
        }

        //Detail
        if (binding.salesmanDetailsLl.visibility == View.VISIBLE) {
            binding.salesmanDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.salesmanDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.salesmanDetailsToggler.setOnClickListener {
            if (binding.salesmanDetailsLl.visibility == View.VISIBLE) {
                binding.salesmanDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.salesmanDetailsLl.visibility = View.GONE
            } else {
                binding.salesmanDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.salesmanDetailsLl.visibility = View.VISIBLE
            }
        }

        //Address
        if (binding.dealerFarmerDetailLl.visibility == View.VISIBLE) {
            binding.dealerFarmerDetailToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.dealerFarmerDetailToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.dealerFarmerDetailToggler.setOnClickListener {
            if (binding.dealerFarmerDetailLl.visibility == View.VISIBLE) {
                binding.dealerFarmerDetailToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.dealerFarmerDetailToggler.visibility = View.GONE
            } else {
                binding.dealerFarmerDetailToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.dealerFarmerDetailLl.visibility = View.VISIBLE
            }
        }

        //Upload Documents
        if (binding.expenseDetailLl.visibility == View.VISIBLE) {
            binding.expenseDetailToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.expenseDetailToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.expenseDetailToggler.setOnClickListener {
            if (binding.expenseDetailLl.visibility == View.VISIBLE) {
                binding.expenseDetailToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.expenseDetailLl.visibility = View.GONE
            } else {
                binding.expenseDetailToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.expenseDetailLl.visibility = View.VISIBLE
            }
        }

        //Upload Documents
        if (binding.salesmanUploadDocumentsLl.visibility == View.VISIBLE) {
            binding.salesmanDocumentsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.salesmanDocumentsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.salesmanDocumentsToggler.setOnClickListener {
            if (binding.salesmanUploadDocumentsLl.visibility == View.VISIBLE) {
                binding.salesmanDocumentsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.salesmanUploadDocumentsLl.visibility = View.GONE
            } else {
                binding.salesmanDocumentsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.salesmanUploadDocumentsLl.visibility = View.VISIBLE
            }
        }

        binding.toolbarLayout.toolbarNavButton.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.toolbarLayout.screenTitle.text = if (isEdit){
            "Update Daily Report"
        } else {
            "Add Daily Report"
        }
        binding.toolbarLayout.toolbarNavButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_back_arrow, requireContext().theme
            )
        )
        binding.submit.setOnClickListener {
            when {
                binding.salesManId.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Sales Man Name")
//                    return@setOnClickListener
                }
                binding.startKm.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Start KM")
//                    return@setOnClickListener
                }
                binding.endKm.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter End KM")
//                    return@setOnClickListener
                }
                binding.totalKm.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Total KM")
//                    return@setOnClickListener
                }
                binding.dealerName.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Dealer/Farmer Name")
//                    return@setOnClickListener
                }
                binding.dealerMobileNo.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Dealer/Farmer Mobile Number")
//                    return@setOnClickListener
                }
                binding.dealerMobileNo.editText?.text.toString().length != 10 -> {
                    showError("Enter Complete Dealer/Farmer Mobile Number")
//                    return@setOnClickListener
                }
                binding.dealerBusinessName.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Dealer/Farmer Business Name")
//                    return@setOnClickListener
                }
                else -> {
                    callSubmitDetails()
                }
            }
        }

        binding.date.editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (binding.date.editText?.text.toString().isNotEmpty()) {
                val splitter = binding.date.editText?.text.toString().split("-")
                if (binding.date.editText?.text.toString().indexOf("-") == 2) {
                    calendar.set(Calendar.DAY_OF_MONTH, splitter[0].toInt())
                    calendar.set(Calendar.MONTH, splitter[1].toInt() - 1)
                    calendar.set(Calendar.YEAR, splitter[2].toInt())
                } else {
                    calendar.set(Calendar.DAY_OF_MONTH, splitter[2].toInt())
                    calendar.set(Calendar.MONTH, splitter[1].toInt() - 1)
                    calendar.set(Calendar.YEAR, splitter[0].toInt())
                }
            }
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                binding.date.editText?.setText("$dayOfMonth-${month + 1}-$year")
            }, mYear, mMonth, mDay).show()
        }

        binding.btnSelectSelfie.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_DEALER_FARMER_SELFIE)
        }

        binding.btnSelectStartKmPic.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_START_KM_PIC)
        }

        binding.btnSelectEndKmPic.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_END_KM_PIC)
        }
    }

    private fun callGetDailyReportDetailApi() {
        Utils.showProgress(requireContext())
        APIClient.getApiInterface().getDailyReportDetail(
            preference?.getString(AppConstant.SALESMAN_ID, ""),
            id_daily_report
        )
            .enqueue(object : Callback<DailyReportDetailResponse> {
                override fun onResponse(
                    call: Call<DailyReportDetailResponse>,
                    response: Response<DailyReportDetailResponse>
                ) {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        if (body.meta.code.equals("200")) {
                            loadData(body.data)
                        } else {
                            showError(body.meta.message)
                        }
                    } else {
                        showError(response.message())
                    }

                }

                override fun onFailure(call: Call<DailyReportDetailResponse>, t: Throwable) {
                    Utils.hideProgress()
                    t.printStackTrace()
                    showError(t.message!!)
                }

            })
    }

    private fun loadData(data: DailyReportData) {
        binding.startKm.editText?.setText(data.startKm.toString())
        binding.endKm.editText?.setText(data.endKm.toString())
        binding.totalKm.editText?.setText(data.totalKm.toString())
        binding.date.editText?.setText(data.date)
        binding.dealerName.editText?.setText(data.dealerName)
        binding.dealerMobileNo.editText?.setText(data.dealerMobileNo)
        binding.dealerEmail.editText?.setText(data.dealerEmail)
        binding.dealerBusinessName.editText?.setText(data.delearBusinessName)
        binding.dealerLocation.editText?.setText(data.dealerLocation)
        binding.dealerUsedCropProduct.editText?.setText(data.dealerUsedCropProduct)
        binding.dealerDemoProduct.editText?.setText(data.dealerDemoProduct)
        binding.mobileExp.editText?.setText(data.mobileExp.toString())
        binding.dinnerExp.editText?.setText(data.dinnerExp.toString())
        binding.stayingCharges.editText?.setText(data.stayingCharges.toString())
        binding.otherExp.editText?.setText(data.otherExp.toString())
    }

    private fun callSubmitDetails() {
        Utils.showProgress(requireContext())
        val mediaType = "text/plain".toMediaType()
        val sales_man_id = binding.salesManId.editText?.text.toString().toRequestBody(mediaType)
        val start_km = binding.startKm.editText?.text.toString().toRequestBody(mediaType)
        val end_km = binding.endKm.editText?.text.toString().toRequestBody(mediaType)
        val total_km = binding.totalKm.editText?.text.toString().toRequestBody(mediaType)
        val date = binding.date.editText?.text.toString().toRequestBody(mediaType)
        val dealer_name = binding.dealerName.editText?.text.toString().toRequestBody(mediaType)
        val dealer_mobile_no =
            binding.dealerMobileNo.editText?.text.toString().toRequestBody(mediaType)
        val dealer_email = binding.dealerEmail.editText?.text.toString().toRequestBody(mediaType)
        val dealer_business_name =
            binding.dealerBusinessName.editText?.text.toString().toRequestBody(mediaType)
        val dealer_location =
            binding.dealerLocation.editText?.text.toString().toRequestBody(mediaType)
        val dealer_used_crop_product =
            binding.dealerUsedCropProduct.editText?.text.toString().toRequestBody(mediaType)
        val dealer_demo_product =
            binding.dealerDemoProduct.editText?.text.toString().toRequestBody(mediaType)
        val mobile_exp = binding.mobileExp.editText?.text.toString().toRequestBody(mediaType)
        val dinner_exp = binding.dinnerExp.editText?.text.toString().toRequestBody(mediaType)
        val staying_charges =
            binding.stayingCharges.editText?.text.toString().toRequestBody(mediaType)
        val other_exp = binding.otherExp.editText?.text.toString().toRequestBody(mediaType)
        val farmer_delear_selfy_pic = if (selfieFileName.isEmpty()) {
            MultipartBody.Part.createFormData(
                "farmer_delear_selfy_pic",
                "",
                "".toRequestBody(mediaType)
            )
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                selfieFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("farmer_delear_selfy_pic", file.name, fileBody)
        }
        val start_km_pic = if (startKmFileName.isEmpty()) {
            MultipartBody.Part.createFormData("start_km_pic", "", "".toRequestBody(mediaType))
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                startKmFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("start_km_pic", file.name, fileBody)
        }
        val end_km_pic = if (endKmFileName.isEmpty()) {
            MultipartBody.Part.createFormData("end_km_pic", "", "".toRequestBody(mediaType))
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                endKmFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("end_km_pic", file.name, fileBody)
        }

        if (isEdit) {
            val id_daily_report = id_daily_report.toRequestBody(mediaType)
            callUpdateDetail(
                sales_man_id,
                id_daily_report,
                start_km,
                end_km,
                total_km,
                date,
                dealer_name,
                dealer_mobile_no,
                dealer_email,
                dealer_business_name,
                dealer_location,
                dealer_used_crop_product,
                dealer_demo_product,
                mobile_exp,
                dinner_exp,
                staying_charges,
                other_exp,
                farmer_delear_selfy_pic,
                start_km_pic,
                end_km_pic
            )
        } else {
            callAddDetail(
                sales_man_id,
                start_km,
                end_km,
                total_km,
                date,
                dealer_name,
                dealer_mobile_no,
                dealer_email,
                dealer_business_name,
                dealer_location,
                dealer_used_crop_product,
                dealer_demo_product,
                mobile_exp,
                dinner_exp,
                staying_charges,
                other_exp,
                farmer_delear_selfy_pic,
                start_km_pic,
                end_km_pic
            )
        }

    }

    private fun callUpdateDetail(
        salesManId: RequestBody,
        idDailyReport: RequestBody,
        startKm: RequestBody,
        endKm: RequestBody,
        totalKm: RequestBody,
        date: RequestBody,
        dealerName: RequestBody,
        dealerMobileNo: RequestBody,
        dealerEmail: RequestBody,
        dealerBusinessName: RequestBody,
        dealerLocation: RequestBody,
        dealerUsedCropProduct: RequestBody,
        dealerDemoProduct: RequestBody,
        mobileExp: RequestBody,
        dinnerExp: RequestBody,
        stayingCharges: RequestBody,
        otherExp: RequestBody,
        farmerDelearSelfyPic: MultipartBody.Part,
        startKmPic: MultipartBody.Part,
        endKmPic: MultipartBody.Part
    ) {
        Utils.showProgress(requireContext())
        APIClient.getApiInterface().updateDailyReport(
            salesManId,
            idDailyReport,
            startKm,
            endKm,
            totalKm,
            date,
            dealerName,
            dealerMobileNo,
            dealerEmail,
            dealerBusinessName,
            dealerLocation,
            dealerUsedCropProduct,
            dealerDemoProduct,
            mobileExp,
            dinnerExp,
            stayingCharges,
            otherExp,
            farmerDelearSelfyPic,
            startKmPic,
            endKmPic
        ).enqueue(object : Callback<AddUpdateDailyReportResponse> {
            override fun onResponse(
                call: Call<AddUpdateDailyReportResponse>,
                response: Response<AddUpdateDailyReportResponse>
            ) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    if (body.meta.code.equals("200")) {
                        Handler().postDelayed(Runnable { (requireActivity() as MainActivity).onBackPressed() }, 1500)
                    }
                    showError(body.meta.message)
                } else {
                    showError(response.message())
                }

            }

            override fun onFailure(call: Call<AddUpdateDailyReportResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    private fun callAddDetail(
        salesManId: RequestBody,
        startKm: RequestBody,
        endKm: RequestBody,
        totalKm: RequestBody,
        date: RequestBody,
        dealerName: RequestBody,
        dealerMobileNo: RequestBody,
        dealerEmail: RequestBody,
        dealerBusinessName: RequestBody,
        dealerLocation: RequestBody,
        dealerUsedCropProduct: RequestBody,
        dealerDemoProduct: RequestBody,
        mobileExp: RequestBody,
        dinnerExp: RequestBody,
        stayingCharges: RequestBody,
        otherExp: RequestBody,
        farmerDelearSelfyPic: MultipartBody.Part,
        startKmPic: MultipartBody.Part,
        endKmPic: MultipartBody.Part
    ) {
        Utils.showProgress(requireContext())
        APIClient.getApiInterface().addDailyReport(
            salesManId,
            startKm,
            endKm,
            totalKm,
            date,
            dealerName,
            dealerMobileNo,
            dealerEmail,
            dealerBusinessName,
            dealerLocation,
            dealerUsedCropProduct,
            dealerDemoProduct,
            mobileExp,
            dinnerExp,
            stayingCharges,
            otherExp,
            farmerDelearSelfyPic,
            startKmPic,
            endKmPic
        ).enqueue(object : Callback<AddUpdateDailyReportResponse> {
            override fun onResponse(
                call: Call<AddUpdateDailyReportResponse>,
                response: Response<AddUpdateDailyReportResponse>
            ) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    if (body.meta.code.equals("200")) {
                        Handler().postDelayed(Runnable { (requireActivity() as MainActivity).onBackPressed() }, 1500)
                    }
                    showError(body.meta.message)
                } else {
                    showError(response.message())
                }

            }

            override fun onFailure(call: Call<AddUpdateDailyReportResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_DEALER_FARMER_SELFIE || requestCode == REQUEST_CODE_FOR_START_KM_PIC ||
                requestCode == REQUEST_CODE_FOR_END_KM_PIC
            ) {
                // Checking whether data is null or not
                var fileName = ""
                data?.data?.let { returnUri ->
                    requireContext().contentResolver.query(returnUri, null, null, null, null).let {
                        val nameIndex = it?.getColumnIndex(OpenableColumns.DISPLAY_NAME)!!
                        it.moveToFirst()
                        fileName = when (requestCode) {
                            REQUEST_CODE_FOR_DEALER_FARMER_SELFIE -> {
                                selfieFileName = it.getString(nameIndex)!!
                                binding.txtSelfieStatus.text = if (selfieFileName.isEmpty()) {
                                    "Not Selected"
                                } else {
                                    "Selected"
                                }
                                selfieFileName
                            }
                            REQUEST_CODE_FOR_START_KM_PIC -> {
                                startKmFileName = it.getString(nameIndex)!!
                                binding.txtStartKmPicStatus.text = if (startKmFileName.isEmpty()) {
                                    "Not Selected"
                                } else {
                                    "Selected"
                                }
                                startKmFileName
                            }
                            REQUEST_CODE_FOR_END_KM_PIC -> {
                                endKmFileName = it.getString(nameIndex)
                                binding.txtEndKmPicStatus.text = if (endKmFileName.isEmpty()) {
                                    "Not Selected"
                                } else {
                                    "Selected"
                                }
                                endKmFileName
                            }
                            else -> {
                                ""
                            }
                        }
                        it.close()
                    }
                    val uploadFile = File(
                        requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                        fileName
                    )
                    val inputStream = requireContext().contentResolver.openInputStream(returnUri)
                    val outputSteam = FileOutputStream(uploadFile)
                    val buffer = ByteArray(1024)
                    var read: Int
                    while (inputStream!!.read(buffer).also { read = it } != -1) {
                        outputSteam.write(buffer, 0, read)
                    }
                    inputStream.close()
                    outputSteam.flush()
                    outputSteam.close()
                }
            }
        }
    }

    /*private fun callUpdateProfile() {
        Utils.showProgress(requireContext())
        val mediaType = "text/plain".toMediaType()
        val dealer_id = preference!!.getString(AppConstant.DEALER_ID, "").toRequestBody(mediaType)
        val responsible_person_name = binding.name.text.toString().toRequestBody(mediaType)
        val whatsappno = binding.whatsapp.text.toString().toRequestBody(mediaType)
        val mobile = binding.mobile.text.toString().toRequestBody(mediaType)
        val dob = binding.dob.text.toString().toRequestBody(mediaType)
        val fileList = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.listFiles()
        val files = if (fileList.isNullOrEmpty()) {
            MultipartBody.Part.createFormData("image", "", "".toRequestBody(mediaType))
        } else {
            val file = fileList[0]
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("image", file.name, fileBody)
        }
        APIClient.getApiInterface()
            .updateProfile(dealer_id, responsible_person_name, whatsappno, mobile, dob, files)
            .enqueue(object : Callback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    response: Response<UpdateProfileResponse>
                ) {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        showError(body.meta.message)
                    } else {
                        showError(response.message())
                    }

                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    Utils.hideProgress()
                    showError("Error occurred!! Please try again later")
                    t.printStackTrace()
                }

            })
    }*/

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string)
    }
}