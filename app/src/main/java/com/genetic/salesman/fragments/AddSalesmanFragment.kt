package com.genetic.salesman.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.genetic.salesman.R
import com.genetic.salesman.activity.MainActivity
import com.genetic.salesman.databinding.FragmentAddSalesmanBinding
import com.genetic.salesman.retrofit_api.APIClient
import com.genetic.salesman.retrofit_api.APIInterface
import com.genetic.salesman.utils.Preference
import com.genetic.salesman.utils.Utils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.*


class AddSalesmanFragment : Fragment() {
    private var _binding: FragmentAddSalesmanBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val REQUEST_CODE_FOR_PROFILE_PICTURE: Int = 1234
    private val REQUEST_CODE_FOR_RESUME: Int = 1235
    private val REQUEST_CODE_FOR_JOINING_LETTER: Int = 1236
    private val REQUEST_CODE_FOR_OTHER_DOCUMENT: Int = 1237
    private val genderItems = listOf("Male", "Female")
    private val maritalStatusItems = listOf("Single", "Married","Divorce")
    private var profilePictureFileName: String = ""
    private var resumeFileName: String = ""
    private var joiningLetterFileName: String = ""
    private var otherDocumentFileName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSalesmanBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = Preference(requireContext())

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
        if (binding.salesmanAddressDetailsLl.visibility == View.VISIBLE) {
            binding.salesmanAddressToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.salesmanAddressToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.salesmanAddressToggler.setOnClickListener {
            if (binding.salesmanAddressDetailsLl.visibility == View.VISIBLE) {
                binding.salesmanAddressToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.salesmanAddressDetailsLl.visibility = View.GONE
            } else {
                binding.salesmanAddressToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.salesmanAddressDetailsLl.visibility = View.VISIBLE
            }
        }

        //Address
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
        binding.toolbarLayout.screenTitle.text = "Register Salesman"
        binding.toolbarLayout.toolbarNavButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_back_arrow, requireContext().theme
            )
        )
        binding.submit.setOnClickListener {
            when {
                binding.salesManFirstName.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter First Name")
//                    return@setOnClickListener
                }
                binding.salesManLastName.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Last Name")
//                    return@setOnClickListener
                }
                binding.salesManFatherName.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Father Name")
//                    return@setOnClickListener
                }
                binding.salesManMotherName.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Mother Name")
//                    return@setOnClickListener
                }
                binding.salesManDob.editText?.text.toString().isEmpty() -> {
                    showError("Please Select Birth Date")
//                    return@setOnClickListener
                }
                binding.salesManMobile.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Mobile Number")
//                    return@setOnClickListener
                }
                binding.salesManMobile.editText?.text.toString().length != 10 -> {
                    showError("Please Enter Complete Mobile Number")
//                    return@setOnClickListener
                }
                binding.salesManEmergencyNumber.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Emergency Number")
//                    return@setOnClickListener
                }
                binding.salesManEmergencyNumber.editText?.text.toString().length != 10 -> {
                    showError("Please Enter Complete Emergency Number")
//                    return@setOnClickListener
                }
                binding.salesManDateOfJoining.editText?.text.toString().isEmpty() -> {
                    showError("Please Select Joining Date")
//                    return@setOnClickListener
                }
                else -> {
                    callSubmitDetails()
                }
            }
        }

        binding.salesManDob.editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (binding.salesManDob.editText?.text.toString().isNotEmpty()) {
                val splitter = binding.salesManDob.editText?.text.toString().split("-")
                calendar.set(Calendar.DAY_OF_MONTH, splitter[0].toInt())
                calendar.set(Calendar.MONTH, splitter[1].toInt() - 1)
                calendar.set(Calendar.YEAR, splitter[2].toInt())
            }
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                binding.salesManDob.editText?.setText("$dayOfMonth-${month + 1}-$year")
            }, mYear, mMonth, mDay).show()
        }

        binding.salesManDateOfJoining.editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (binding.salesManDateOfJoining.editText?.text.toString().isNotEmpty()) {
                val splitter = binding.salesManDateOfJoining.editText?.text.toString().split("-")
                calendar.set(Calendar.DAY_OF_MONTH, splitter[0].toInt())
                calendar.set(Calendar.MONTH, splitter[1].toInt() - 1)
                calendar.set(Calendar.YEAR, splitter[2].toInt())
            }
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                binding.salesManDateOfJoining.editText?.setText("$dayOfMonth-${month + 1}-$year")
            }, mYear, mMonth, mDay).show()
        }

        binding.profilePic.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_PROFILE_PICTURE)
        }

        binding.btnSelectResume.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_RESUME)
        }

        binding.btnSelectJoiningLetter.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_JOINING_LETTER)
        }

        binding.btnSelectOtherDocuments.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_OTHER_DOCUMENT)
        }

        val genderAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_layout_item, genderItems)
        (binding.salesManGender.editText as? AutoCompleteTextView)?.setAdapter(genderAdapter)

        val maritalStatusAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_layout_item, maritalStatusItems)
        (binding.salesManGender.editText as? AutoCompleteTextView)?.setAdapter(maritalStatusAdapter)
    }

    private fun callSubmitDetails() {
        Utils.showProgress(requireContext())
        val mediaType = "text/plain".toMediaType()
        val code = binding.salesManEmployeeCodeNo.editText?.text.toString().toRequestBody(mediaType)
        val user = binding.salesManUser.editText?.text.toString().toRequestBody(mediaType)
        val first_name = binding.salesManFirstName.editText?.text.toString().toRequestBody(mediaType)
        val last_name = binding.salesManLastName.editText?.text.toString().toRequestBody(mediaType)
        val father_name = binding.salesManFatherName.editText?.text.toString().toRequestBody(mediaType)
        val mother_name = binding.salesManMotherName.editText?.text.toString().toRequestBody(mediaType)
        val gender = binding.salesManGender.editText?.text.toString().toRequestBody(mediaType)
        val dob = binding.salesManDob.editText?.text.toString().toRequestBody(mediaType)
        val mobileno = binding.salesManMobile.editText?.text.toString().toRequestBody(mediaType)
        val emergency_no = binding.salesManEmergencyNumber.editText?.text.toString().toRequestBody(mediaType)
        val date_of_joining = binding.salesManDateOfJoining.editText?.text.toString().toRequestBody(mediaType)
        val marital_status = binding.salesManMaritalStatus.editText?.text.toString().toRequestBody(mediaType)
        val qualification = binding.salesManQualification.editText?.text.toString().toRequestBody(mediaType)
        val experience = binding.salesManExperience.editText?.text.toString().toRequestBody(mediaType)
        val current_address = binding.salesManCurrentAddress.editText?.text.toString().toRequestBody(mediaType)
        val permanet_address = if (binding.checkboxSameAsCurrentAddress.isChecked) {
            current_address
        } else {
            binding.salesManPermanentAddress.editText?.text.toString().toRequestBody(mediaType)
        }
        val avatar = if (profilePictureFileName.isEmpty()) {
            MultipartBody.Part.createFormData("avatar", "", "".toRequestBody(mediaType))
        } else {
            val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                profilePictureFileName)
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("avatar", file.name, fileBody)
        }
        val resume = if (resumeFileName.isEmpty()) {
            MultipartBody.Part.createFormData("resume", "", "".toRequestBody(mediaType))
        } else {
            val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                resumeFileName)
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("resume", file.name, fileBody)
        }
        val joining_letter = if (joiningLetterFileName.isEmpty()) {
            MultipartBody.Part.createFormData("joining_letter", "", "".toRequestBody(mediaType))
        } else {
            val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                joiningLetterFileName)
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("joining_letter", file.name, fileBody)
        }
        val other_document = if (otherDocumentFileName.isEmpty()) {
            MultipartBody.Part.createFormData("other_document", "", "".toRequestBody(mediaType))
        } else {
            val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                otherDocumentFileName)
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("other_document", file.name, fileBody)
        }
        APIClient.getApiInterface().addSalesman(code, user, first_name, last_name, father_name, mother_name, gender,
        dob, mobileno, emergency_no, date_of_joining, marital_status, qualification, experience, current_address, permanet_address,
        avatar, resume, joining_letter, other_document)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_PROFILE_PICTURE || requestCode == REQUEST_CODE_FOR_RESUME ||
                requestCode == REQUEST_CODE_FOR_JOINING_LETTER || requestCode == REQUEST_CODE_FOR_OTHER_DOCUMENT) {
                // Checking whether data is null or not
                var fileName = ""
                data?.data?.let { returnUri ->
                    requireContext().contentResolver.query(returnUri, null, null, null, null).let {
                        val nameIndex = it?.getColumnIndex(OpenableColumns.DISPLAY_NAME)!!
                        it.moveToFirst()
                        fileName = when (requestCode) {
                            REQUEST_CODE_FOR_PROFILE_PICTURE -> {
                                profilePictureFileName = it.getString(nameIndex)!!
                                profilePictureFileName
                            }
                            REQUEST_CODE_FOR_RESUME -> {
                                resumeFileName = it.getString(nameIndex)!!
                                resumeFileName
                            }
                            REQUEST_CODE_FOR_JOINING_LETTER -> {
                                joiningLetterFileName = it.getString(nameIndex)
                                joiningLetterFileName
                            }
                            REQUEST_CODE_FOR_OTHER_DOCUMENT -> {
                                otherDocumentFileName = it.getString(nameIndex)
                                otherDocumentFileName
                            }
                            else -> {
                                ""
                            }
                        }
                        it.close()
                    }
                    val uploadFile = File(
                        requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
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
                    if (requestCode == REQUEST_CODE_FOR_PROFILE_PICTURE) {
                        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), profilePictureFileName)
                        if (file.exists()) {
                            binding.profilePic.setImageBitmap(BitmapFactory.decodeStream(file.inputStream()))
                        }
                    }
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