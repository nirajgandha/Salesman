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
import com.bumptech.glide.Glide
import com.genetic.salesman.R
import com.genetic.salesman.activity.MainActivity
import com.genetic.salesman.databinding.FragmentAddDealerBinding
import com.genetic.salesman.model.*
import com.genetic.salesman.retrofit_api.APIClient
import com.genetic.salesman.utils.AppConstant
import com.genetic.salesman.utils.GlideApp
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


class AddDealerFragment : Fragment() {
    private var _binding: FragmentAddDealerBinding? = null
    private val binding get() = _binding!!
    private var preference: Preference? = null
    private val REQUEST_CODE_FOR_PROFILE_PICTURE: Int = 1234
    private val REQUEST_CODE_FOR_PARTNERSHIP_MEMORANDUM_DOC: Int = 1235
    private val REQUEST_CODE_FOR_PESTICIDE_LICENSE: Int = 1236
    private val REQUEST_CODE_FOR_FERTILIZER_DOCUMENT: Int = 1237
    private val REQUEST_CODE_FOR_GST_CERTIFICATE: Int = 1238
    private val REQUEST_CODE_FOR_ADHAR_CARD: Int = 1239
    private val REQUEST_CODE_FOR_PAN_CARD: Int = 1240
    private val REQUEST_CODE_FOR_ELECTRICITY_BILL: Int = 1241
    private val REQUEST_CODE_FOR_RENT_AGREEMENT: Int = 1242
    private var profilePictureFileName: String = ""
    private var partnershipMemorandumDocFileName: String = ""
    private var pesticideFileName: String = ""
    private var fertilizerFileName: String = ""
    private var gstCertificateFileName: String = ""
    private var adharCardFileName: String = ""
    private var panCardFileName: String = ""
    private var electricityBillFileName: String = ""
    private var rentAgreementFileName: String = ""
    private val firmNatureItems = listOf("Wholesaler", "Retailer")
    private val ownershipItems = listOf("Owned", "Rented")
    private var selectedFirmStateId = ""
    private var selectedResidentialStateId = ""
    private var selectedFirmCityId = ""
    private var selectedResidentialCityId = ""
    private var isEdit = false
    private var dealerId = ""
    private var dealerDataUpdate: DealerDataUpdate? = null
    private var loadResidentialCityAgain = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDealerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preference = Preference(requireContext())
        val argument = arguments
        if (argument != null) {
            dealerId = argument.getString("editDealerId", "")
            if (dealerId.isNotEmpty()) {
                isEdit = true
            }
        }
        if (isEdit) {
            callGetDealerDetail()
        } else {
            callGetStateListApi()
        }

        //Detail
        if (binding.dealerDetailsLl.visibility == View.VISIBLE) {
            binding.dealerDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.dealerDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.dealerDetailsToggler.setOnClickListener {
            if (binding.dealerDetailsLl.visibility == View.VISIBLE) {
                binding.dealerDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.dealerDetailsLl.visibility = View.GONE
            } else {
                binding.dealerDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.dealerDetailsLl.visibility = View.VISIBLE
            }
        }

        //Bank Detail
        if (binding.dealerBankDetailsLl.visibility == View.VISIBLE) {
            binding.dealerBankDetailToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.dealerBankDetailToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.dealerBankDetailToggler.setOnClickListener {
            if (binding.dealerBankDetailsLl.visibility == View.VISIBLE) {
                binding.dealerBankDetailToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.dealerBankDetailsLl.visibility = View.GONE
            } else {
                binding.dealerBankDetailToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.dealerBankDetailsLl.visibility = View.VISIBLE
            }
        }

        //License Detail
        if (binding.dealerLicenseDetailsLl.visibility == View.VISIBLE) {
            binding.dealerLicenseDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.dealerLicenseDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.dealerLicenseDetailsToggler.setOnClickListener {
            if (binding.dealerLicenseDetailsLl.visibility == View.VISIBLE) {
                binding.dealerLicenseDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.dealerLicenseDetailsLl.visibility = View.GONE
            } else {
                binding.dealerLicenseDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.dealerLicenseDetailsLl.visibility = View.VISIBLE
            }
        }

        //Cheque Detail
        if (binding.dealerChequeDetailsLl.visibility == View.VISIBLE) {
            binding.dealerChequeDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.dealerChequeDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.dealerChequeDetailsToggler.setOnClickListener {
            if (binding.dealerChequeDetailsLl.visibility == View.VISIBLE) {
                binding.dealerChequeDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.dealerChequeDetailsLl.visibility = View.GONE
            } else {
                binding.dealerChequeDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.dealerChequeDetailsLl.visibility = View.VISIBLE
            }
        }

        //Firm Address Detail
        if (binding.dealerFirmAddressDetailsLl.visibility == View.VISIBLE) {
            binding.dealerFirmAddressDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.dealerFirmAddressDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.dealerFirmAddressDetailsToggler.setOnClickListener {
            if (binding.dealerFirmAddressDetailsLl.visibility == View.VISIBLE) {
                binding.dealerFirmAddressDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.dealerFirmAddressDetailsLl.visibility = View.GONE
            } else {
                binding.dealerFirmAddressDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.dealerFirmAddressDetailsLl.visibility = View.VISIBLE
            }
        }

        //Firm Address Detail
        if (binding.dealerResidentialAddressDetailsLl.visibility == View.VISIBLE) {
            binding.dealerResidentialAddressDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.dealerResidentialAddressDetailsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.dealerResidentialAddressDetailsToggler.setOnClickListener {
            if (binding.dealerResidentialAddressDetailsLl.visibility == View.VISIBLE) {
                binding.dealerResidentialAddressDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.dealerResidentialAddressDetailsLl.visibility = View.GONE
            } else {
                binding.dealerResidentialAddressDetailsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.dealerResidentialAddressDetailsLl.visibility = View.VISIBLE
            }
        }
        //Firm Address Detail
        if (binding.dealerUploadDocumentsLl.visibility == View.VISIBLE) {
            binding.dealerDocumentsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_close,
                    requireContext().theme
                )
            )
        } else {
            binding.dealerDocumentsToggler.setImageDrawable(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_open,
                    requireContext().theme
                )
            )
        }
        binding.dealerDocumentsToggler.setOnClickListener {
            if (binding.dealerUploadDocumentsLl.visibility == View.VISIBLE) {
                binding.dealerDocumentsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_open,
                        requireContext().theme
                    )
                )
                binding.dealerUploadDocumentsLl.visibility = View.GONE
            } else {
                binding.dealerDocumentsToggler.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireContext().resources,
                        R.drawable.ic_close,
                        requireContext().theme
                    )
                )
                binding.dealerUploadDocumentsLl.visibility = View.VISIBLE
            }
        }

        binding.toolbarLayout.toolbarNavButton.setOnClickListener { (requireActivity() as MainActivity).onBackPressed() }
        binding.toolbarLayout.screenTitle.text = if (isEdit) {
            "Update Dealer"
        } else {
            "Register Dealer"
        }
        binding.toolbarLayout.toolbarNavButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_back_arrow, requireContext().theme
            )
        )
        binding.submit.setOnClickListener {
            when {
                binding.firmName.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Firm Name")
//                    return@setOnClickListener
                }
                binding.dealerQualification.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Dealer Qualification")
//                    return@setOnClickListener
                }
                binding.responsiblePersonName.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Responsible Person Name")
//                    return@setOnClickListener
                }
                binding.mobileNo.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Mobile Number")
//                    return@setOnClickListener
                }
                binding.mobileNo.editText?.text.toString().length != 10 -> {
                    showError("Please Enter Complete Mobile Number")
//                    return@setOnClickListener
                }
                binding.whatsappNo.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Whatsapp Number")
//                    return@setOnClickListener
                }
                binding.whatsappNo.editText?.text.toString().length != 10 -> {
                    showError("Please Enter Complete Whatsapp Number")
//                    return@setOnClickListener
                }
                binding.dateOfBirth.editText?.text.toString().isEmpty() -> {
                    showError("Please Select Birth Date")
//                    return@setOnClickListener
                }
                binding.yearOfEstablishment.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Establishment Year")
//                    return@setOnClickListener
                }
                binding.yearOfEstablishment.editText?.text.toString().length != 4 -> {
                    showError("Please Enter Complete Establishment Year")
//                    return@setOnClickListener
                }
                binding.totalTurnover.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Total Turnover")
//                    return@setOnClickListener
                }
                binding.pesticide.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Pesticide Turnover")
//                    return@setOnClickListener
                }
                binding.fertilizers.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Fertilizer Turnover")
//                    return@setOnClickListener
                }
                binding.seeds.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Seeds Turnover")
//                    return@setOnClickListener
                }
                binding.pgrOther.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter PGR Other Turnover")
//                    return@setOnClickListener
                }
                binding.adharcardNo.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Adhar Card No")
//                    return@setOnClickListener
                }
                binding.adharcardNo.editText?.text.toString().length != 12 -> {
                    showError("Enter Completer Adhar Card No")
//                    return@setOnClickListener
                }
                binding.nameOfBank.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Bank Name")
//                    return@setOnClickListener
                }
                binding.bankAcNo.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Bank Ac. No.")
//                    return@setOnClickListener
                }
                binding.insecticideLicenseNo.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Insecticide License Number")
//                    return@setOnClickListener
                }
                binding.insecticideLicenseValidity.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Insecticide License Validity")
//                    return@setOnClickListener
                }
                binding.fertilizerLicenseNo.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Fertilizer License Number")
//                    return@setOnClickListener
                }
                binding.fertilizerLicenseValidity.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Fertilizer License Validity")
//                    return@setOnClickListener
                }
                binding.amountOfFirstCheque.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Amount of First Cheque")
//                    return@setOnClickListener
                }
                binding.amountOfFirstChequeNo.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter First Cheque No")
//                    return@setOnClickListener
                }
                selectedFirmStateId.isEmpty() -> {
                    showError("Select Firm State")
//                    return@setOnClickListener
                }
                selectedFirmCityId.isEmpty() -> {
                    showError("Select Firm City")
//                    return@setOnClickListener
                }
                binding.firmTaluka.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Firm Taluka")
//                    return@setOnClickListener
                }
                binding.firmZipcode.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Firm Pincode")
//                    return@setOnClickListener
                }
                selectedResidentialStateId.isEmpty() -> {
                    showError("Select Residential State")
//                    return@setOnClickListener
                }
                selectedResidentialCityId.isEmpty() -> {
                    showError("Select Residential City")
//                    return@setOnClickListener
                }
                binding.residentialTakula.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Residential Taluka")
//                    return@setOnClickListener
                }
                binding.residentialZipcode.editText?.text.toString().isEmpty() -> {
                    showError("Please Enter Residential Pincode")
//                    return@setOnClickListener
                }
                else -> {
                    if (isEdit) {
                        callSubmitDetails()
                    } else {
                        callUpdateDetails()
                    }
                }
            }
        }

        binding.dateOfBirth.editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (binding.dateOfBirth.editText?.text.toString().isNotEmpty()) {
                val splitter = binding.dateOfBirth.editText?.text.toString().split("-")
                if (binding.dateOfBirth.editText?.text.toString().indexOf("-") == 2) {
                    calendar.set(Calendar.YEAR, splitter[2].toInt())
                    calendar.set(Calendar.MONTH, splitter[1].toInt()-1)
                    calendar.set(Calendar.DAY_OF_MONTH, splitter[0].toInt())
                } else {
                    calendar.set(Calendar.YEAR, splitter[0].toInt())
                    calendar.set(Calendar.MONTH, splitter[1].toInt()-1)
                    calendar.set(Calendar.DAY_OF_MONTH, splitter[2].toInt())
                }
            }
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                binding.dateOfBirth.editText?.setText("$dayOfMonth-${month + 1}-$year")
            }, mYear, mMonth, mDay).show()
        }

        binding.dateOfAnniversary.editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (binding.dateOfAnniversary.editText?.text.toString().isNotEmpty()) {
                val splitter = binding.dateOfAnniversary.editText?.text.toString().split("-")
                if (binding.dateOfAnniversary.editText?.text.toString().indexOf("-") == 2) {
                    calendar.set(Calendar.YEAR, splitter[2].toInt())
                    calendar.set(Calendar.MONTH, splitter[1].toInt() - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, splitter[0].toInt())
                } else {
                    calendar.set(Calendar.YEAR, splitter[0].toInt())
                    calendar.set(Calendar.MONTH, splitter[1].toInt() - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, splitter[2].toInt())
                }
            }
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                binding.dateOfAnniversary.editText?.setText("$dayOfMonth-${month + 1}-$year")
            }, mYear, mMonth, mDay).show()
        }

        binding.profilePic.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_PROFILE_PICTURE)
        }

        binding.btnSelectPartnershipMemorandumOfFirmDoc.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_PARTNERSHIP_MEMORANDUM_DOC)
        }

        binding.btnSelectPesticideLicenseDoc.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_PESTICIDE_LICENSE)
        }

        binding.btnSelectFertilizerLicenseDoc.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_FERTILIZER_DOCUMENT)
        }

        binding.btnSelectAdharcardDoc.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_ADHAR_CARD)
        }

        binding.btnSelectGstCertificateDoc.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_GST_CERTIFICATE)
        }

        binding.btnSelectPanCardDoc.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_PAN_CARD)
        }

        binding.btnSelectElectricityBillDoc.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_ELECTRICITY_BILL)
        }

        binding.btnSelectRentAgreementDoc.setOnClickListener {
            val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
            filesIntent.type = "image/*"
            startActivityForResult(filesIntent, REQUEST_CODE_FOR_RENT_AGREEMENT)
        }

        val firmNatureAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_layout_item, firmNatureItems)
        (binding.natureOfFirm.editText as? AutoCompleteTextView)?.setAdapter(firmNatureAdapter)

        val ownershipShopAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_layout_item, ownershipItems)
        (binding.ownershipOfShop.editText as? AutoCompleteTextView)?.setAdapter(ownershipShopAdapter)

        val ownershipGodownAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_layout_item, ownershipItems)
        (binding.ownershipOfGodown.editText as? AutoCompleteTextView)?.setAdapter(
            ownershipGodownAdapter
        )

        (binding.firmStateId.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedFirmStateId = stateIdArray[position]
            callGetFirmCityListApi(selectedFirmStateId)
        }
        (binding.residentialStateId.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedResidentialStateId = stateIdArray[position]
            callGetResidentialCityListApi(selectedResidentialStateId)
        }
        (binding.residentialCityId.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedResidentialCityId = cityResidentialIdArray[position]
        }
        (binding.firmCityId.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
            selectedFirmCityId = cityFirmIdArray[position]
        }
    }

    private fun callUpdateDetails() {
        Utils.showProgress(requireContext())
        val mediaType = "text/plain".toMediaType()
        val salesman_id = dealerDataUpdate!!.id.toString().toRequestBody(mediaType)
        val firm_name = binding.firmName.editText?.text.toString().toRequestBody(mediaType)
        val dealer_qualification =
            binding.dealerQualification.editText?.text.toString().toRequestBody(mediaType)
        val responsible_person_name =
            binding.responsiblePersonName.editText?.text.toString().toRequestBody(mediaType)
        val residential_address =
            binding.residentialAddress.editText?.text.toString().toRequestBody(mediaType)
        val mobile_no = binding.mobileNo.editText?.text.toString().toRequestBody(mediaType)
        val whatsapp_no = binding.whatsappNo.editText?.text.toString().toRequestBody(mediaType)
        val date_of_birth = binding.dateOfBirth.editText?.text.toString().toRequestBody(mediaType)
        val date_of_anniversary =
            binding.dateOfAnniversary.editText?.text.toString().toRequestBody(mediaType)
        val year_of_establishment =
            binding.yearOfEstablishment.editText?.text.toString().toRequestBody(mediaType)
        val nature_of_firm = binding.natureOfFirm.editText?.text.toString().toRequestBody(mediaType)
        val total_turnover =
            binding.totalTurnover.editText?.text.toString().toRequestBody(mediaType)
        val pesticide = binding.pesticide.editText?.text.toString().toRequestBody(mediaType)
        val fertilizers = binding.fertilizers.editText?.text.toString().toRequestBody(mediaType)
        val pgr_other = binding.pgrOther.editText?.text.toString().toRequestBody(mediaType)
        val ownership_of_shop =
            binding.ownershipOfShop.editText?.text.toString().toRequestBody(mediaType)
        val other_agency_names =
            binding.otherAgencyNames.editText?.text.toString().toRequestBody(mediaType)
        val gst_no = binding.gstNo.editText?.text.toString().toRequestBody(mediaType)
        val adharcard_no = binding.adharcardNo.editText?.text.toString().toRequestBody(mediaType)
        val pan_card_no = binding.panCardNo.editText?.text.toString().toRequestBody(mediaType)
        val name_of_bank = binding.nameOfBank.editText?.text.toString().toRequestBody(mediaType)
        val bank_ac_no = binding.bankAcNo.editText?.text.toString().toRequestBody(mediaType)
        val ifsc_code = binding.ifscCode.editText?.text.toString().toRequestBody(mediaType)
        val insecticide_license_no =
            binding.insecticideLicenseNo.editText?.text.toString().toRequestBody(mediaType)
        val insecticide_license_validity =
            binding.insecticideLicenseValidity.editText?.text.toString().toRequestBody(mediaType)
        val fertilizer_license_no =
            binding.fertilizerLicenseNo.editText?.text.toString().toRequestBody(mediaType)
        val fertilizer_license_validity =
            binding.fertilizerLicenseValidity.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_first_cheque =
            binding.amountOfFirstCheque.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_first_cheque_no =
            binding.amountOfFirstChequeNo.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_2 =
            binding.amountOfCheque2.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_no_2 =
            binding.amountOfChequeNo2.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_3 =
            binding.amountOfCheque3.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_no_3 =
            binding.amountOfChequeNo3.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_4 =
            binding.amountOfCheque4.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_no_4 =
            binding.amountOfChequeNo4.editText?.text.toString().toRequestBody(mediaType)
        val firm_address = binding.firmAddress.editText?.text.toString().toRequestBody(mediaType)
        val firm_state_id = selectedFirmStateId.toRequestBody(mediaType)
        val firm_city_id = selectedFirmCityId.toRequestBody(mediaType)
        val firm_taluka = binding.firmTaluka.editText?.text.toString().toRequestBody(mediaType)
        val firm_zipcode = binding.firmZipcode.editText?.text.toString().toRequestBody(mediaType)
        val residential_address_1 =
            binding.residentialAddress1.editText?.text.toString().toRequestBody(mediaType)
        val residential_state_id = selectedResidentialStateId.toRequestBody(mediaType)
        val residential_city_id = selectedResidentialCityId.toRequestBody(mediaType)
        val residential_takula =
            binding.residentialTakula.editText?.text.toString().toRequestBody(mediaType)
        val residential_zipcode =
            binding.residentialZipcode.editText?.text.toString().toRequestBody(mediaType)
        val avatar = if (profilePictureFileName.isEmpty()) {
            MultipartBody.Part.createFormData("image", "", "".toRequestBody(mediaType))
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                profilePictureFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("image", file.name, fileBody)
        }
        val partnership_memorandum_of_firm_doc = if (partnershipMemorandumDocFileName.isEmpty()) {
            MultipartBody.Part.createFormData(
                "partenership_memorandum_of_firm_doc",
                "",
                "".toRequestBody(mediaType)
            )
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                partnershipMemorandumDocFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData(
                "partenership_memorandum_of_firm_doc",
                file.name,
                fileBody
            )
        }
        val pesticide_license_doc = if (pesticideFileName.isEmpty()) {
            MultipartBody.Part.createFormData(
                "pesticide_license_doc",
                "",
                "".toRequestBody(mediaType)
            )
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                pesticideFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("pesticide_license_doc", file.name, fileBody)
        }
        val fertilizer_license_doc = if (fertilizerFileName.isEmpty()) {
            MultipartBody.Part.createFormData(
                "fertilizer_license_doc",
                "",
                "".toRequestBody(mediaType)
            )
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                fertilizerFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("fertilizer_license_doc", file.name, fileBody)
        }
        val gst_certificate_doc = if (gstCertificateFileName.isEmpty()) {
            MultipartBody.Part.createFormData(
                "gst_certificate_doc",
                "",
                "".toRequestBody(mediaType)
            )
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                gstCertificateFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("gst_certificate_doc", file.name, fileBody)
        }
        val adharcard_doc = if (adharCardFileName.isEmpty()) {
            MultipartBody.Part.createFormData("adharcard_doc", "", "".toRequestBody(mediaType))
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                adharCardFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("adharcard_doc", file.name, fileBody)
        }
        val pancard_doc = if (panCardFileName.isEmpty()) {
            MultipartBody.Part.createFormData("pancard_doc", "", "".toRequestBody(mediaType))
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                panCardFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("pancard_doc", file.name, fileBody)
        }
        val electricity_bill_doc = if (electricityBillFileName.isEmpty()) {
            MultipartBody.Part.createFormData(
                "electricity_bill_doc",
                "",
                "".toRequestBody(mediaType)
            )
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                electricityBillFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("electricity_bill_doc", file.name, fileBody)
        }
        val rent_agreement_doc = if (rentAgreementFileName.isEmpty()) {
            MultipartBody.Part.createFormData("rent_agreement_doc", "", "".toRequestBody(mediaType))
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                rentAgreementFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("rent_agreement_doc", file.name, fileBody)
        }
        APIClient.getApiInterface().updateDealer(
            salesman_id,
            firm_name,
            dealer_qualification,
            responsible_person_name,
            residential_address,
            mobile_no,
            whatsapp_no,
            date_of_birth,
            date_of_anniversary,
            year_of_establishment,
            nature_of_firm,
            total_turnover,
            pesticide,
            fertilizers,
            pgr_other,
            ownership_of_shop,
            other_agency_names,
            gst_no,
            adharcard_no,
            pan_card_no,
            name_of_bank,
            bank_ac_no,
            ifsc_code,
            insecticide_license_no,
            insecticide_license_validity,
            fertilizer_license_no,
            fertilizer_license_validity,
            amount_of_first_cheque,
            amount_of_first_cheque_no,
            amount_of_cheque_2,
            amount_of_cheque_no_2,
            amount_of_cheque_3,
            amount_of_cheque_no_3,
            amount_of_cheque_4,
            amount_of_cheque_no_4,
            firm_address,
            firm_state_id,
            firm_city_id,
            firm_taluka,
            firm_zipcode,
            residential_address_1,
            residential_state_id,
            residential_city_id,
            residential_takula,
            residential_zipcode,
            avatar,
            partnership_memorandum_of_firm_doc,
            pesticide_license_doc,
            fertilizer_license_doc,
            gst_certificate_doc,
            adharcard_doc,
            pancard_doc,
            electricity_bill_doc,
            rent_agreement_doc
        ).enqueue(object : Callback<DealerRegistrationResponse> {
            override fun onResponse(
                call: Call<DealerRegistrationResponse>,
                response: Response<DealerRegistrationResponse>
            ) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.code.equals("200")) {
                        Handler().postDelayed(
                            Runnable { (requireActivity() as MainActivity).onBackPressed() },
                            1500
                        )
                    }
                    showError(meta.message)
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<DealerRegistrationResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }
        })
    }

    private fun callGetDealerDetail() {
        Utils.showProgress(requireContext())
        APIClient.getApiInterface().getDealerDetail(dealerId)
            .enqueue(object : Callback<GetDealerDataResponse> {
                override fun onResponse(
                    call: Call<GetDealerDataResponse>,
                    response: Response<GetDealerDataResponse>
                ) {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        if (body.meta.code.equals("200")) {
                            dealerDataUpdate = body.data[0]
                            callGetStateListApi()
                        } else {
                            showError(body.meta.message)
                        }
                    } else {
                        showError(response.message())
                    }

                }

                override fun onFailure(call: Call<GetDealerDataResponse>, t: Throwable) {
                    Utils.hideProgress()
                    t.printStackTrace()
                    showError(t.message!!)
                }

            })
    }

    var stateArray: Array<String> = emptyArray()
    var stateIdArray: Array<String> = emptyArray()
    private fun callGetStateListApi() {
        Utils.showProgress(requireContext())
        APIClient.getApiInterface().getStateList(preference?.getString(AppConstant.SALESMAN_ID, ""))
            .enqueue(object : Callback<StateListResponse> {
                override fun onResponse(
                    call: Call<StateListResponse>,
                    response: Response<StateListResponse>
                ) {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        if (body.meta.code.equals("200")) {
                            stateArray = Array(body.data.size) { "" }
                            stateIdArray = Array(body.data.size) { "" }
                            for (index in 0 until body.data.size) {
                                val item = body.data[index]
                                stateArray[index] = item.name
                                stateIdArray[index] = item.id.toString()
                            }
                            val firm_adapter = ArrayAdapter(
                                requireContext(),
                                R.layout.dropdown_layout_item,
                                stateArray
                            )
                            (binding.firmStateId.editText as AutoCompleteTextView).setAdapter(
                                firm_adapter
                            )
                            val residential_adapter = ArrayAdapter(
                                requireContext(),
                                R.layout.dropdown_layout_item,
                                stateArray
                            )
                            (binding.residentialStateId.editText as AutoCompleteTextView).setAdapter(
                                residential_adapter
                            )
                            if (isEdit) {
                                callGetFirmCityListApi(dealerDataUpdate!!.firmStateId.toString())
                            }
                        } else {
                            showError(body.meta.message)
                        }
                    } else {
                        showError(response.message())
                    }

                }

                override fun onFailure(call: Call<StateListResponse>, t: Throwable) {
                    Utils.hideProgress()
                    t.printStackTrace()
                    showError(t.message!!)
                }

            })
    }

    var cityFirmArray: Array<String> = emptyArray()
    var cityResidentialArray: Array<String> = emptyArray()
    var cityFirmIdArray: Array<String> = emptyArray()
    var cityResidentialIdArray: Array<String> = emptyArray()
    var dataLoaded = false
    private fun callGetResidentialCityListApi(stateId: String) {
        Utils.showProgress(requireContext())
        APIClient.getApiInterface()
            .getCityList(preference?.getString(AppConstant.SALESMAN_ID, ""), stateId)
            .enqueue(object : Callback<CityListResponse> {
                override fun onResponse(
                    call: Call<CityListResponse>,
                    response: Response<CityListResponse>
                ) {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        if (body.meta.code.equals("200")) {
                            cityResidentialArray = Array(body.data.size) { "" }
                            cityResidentialIdArray = Array(body.data.size) { "" }
                            for (index in 0 until body.data.size) {
                                val item = body.data[index]
                                cityResidentialArray[index] = item.name
                                cityResidentialIdArray[index] = item.id.toString()
                            }
                            val residential_adapter = ArrayAdapter(
                                requireContext(),
                                R.layout.dropdown_layout_item,
                                cityResidentialArray
                            )
                            (binding.residentialCityId.editText as AutoCompleteTextView).setAdapter(
                                residential_adapter
                            )
                            (binding.residentialCityId.editText as AutoCompleteTextView).setText("")
                            selectedResidentialCityId = ""
                            if (isEdit && !dataLoaded) {
                                dataLoaded = true
                                loadData()
                            }
                        } else {
                            showError(body.meta.message)
                        }
                    } else {
                        showError(response.message())
                    }

                }

                override fun onFailure(call: Call<CityListResponse>, t: Throwable) {
                    Utils.hideProgress()
                    t.printStackTrace()
                    showError(t.message!!)
                }

            })
    }

    private fun loadData() {
        (binding.residentialStateId.editText as AutoCompleteTextView).setText(
            (binding.residentialStateId.editText as AutoCompleteTextView).adapter.getItem(
                stateIdArray.indexOf(dealerDataUpdate!!.residentialStateId.toString())
            ).toString(), false
        )
        selectedResidentialStateId = dealerDataUpdate!!.residentialStateId.toString()
        (binding.firmStateId.editText as AutoCompleteTextView).setText(
            (binding.firmStateId.editText as AutoCompleteTextView).adapter.getItem(
                stateIdArray.indexOf(dealerDataUpdate!!.firmStateId.toString())
            ).toString(), false
        )
        selectedFirmStateId = dealerDataUpdate!!.firmStateId.toString()
        (binding.firmCityId.editText as AutoCompleteTextView).setText(
            (binding.firmCityId.editText as AutoCompleteTextView).adapter.getItem(
                cityFirmIdArray.indexOf(dealerDataUpdate!!.firmCityId.toString())
            ).toString(), false
        )
        selectedFirmCityId = dealerDataUpdate!!.firmCityId.toString()
        (binding.residentialCityId.editText as AutoCompleteTextView).setText(
            (binding.residentialCityId.editText as AutoCompleteTextView).adapter.getItem(
                cityResidentialIdArray.indexOf(dealerDataUpdate!!.residentialCityId.toString())
            ).toString(), false
        )
        selectedResidentialCityId = dealerDataUpdate!!.residentialCityId.toString()
        binding.firmName.editText?.setText(dealerDataUpdate!!.firmName)
        binding.dealerQualification.editText?.setText(dealerDataUpdate!!.dealerQualification)
        binding.responsiblePersonName.editText?.setText(dealerDataUpdate!!.responsiblePersonName)
        binding.residentialAddress.editText?.setText(dealerDataUpdate!!.residentialAddress)
        binding.mobileNo.editText?.setText(dealerDataUpdate!!.mobileno.toString())
        binding.whatsappNo.editText?.setText(dealerDataUpdate!!.whatsappno.toString())
        binding.dateOfBirth.editText?.setText(dealerDataUpdate!!.birthdate)
        binding.dateOfAnniversary.editText?.setText(dealerDataUpdate!!.anniversaryDate)
        binding.yearOfEstablishment.editText?.setText(dealerDataUpdate!!.yearOfEstablishment.toString())
        binding.natureOfFirm.editText?.setText(dealerDataUpdate!!.natureOfFirm)
        binding.totalTurnover.editText?.setText(dealerDataUpdate!!.totalTurnover.toString())
        binding.pesticide.editText?.setText(dealerDataUpdate!!.pesticide)
        binding.fertilizers.editText?.setText(dealerDataUpdate!!.fertilizers)
        binding.seeds.editText?.setText(dealerDataUpdate!!.seeds)
        binding.pgrOther.editText?.setText(dealerDataUpdate!!.pgrOther)
        binding.ownershipOfShop.editText?.setText(dealerDataUpdate!!.ownershipOfShop)
        binding.ownershipOfGodown.editText?.setText(dealerDataUpdate!!.ownershipOfGodown)
        binding.otherAgencyNames.editText?.setText(dealerDataUpdate!!.otherAgencyNames)
        binding.gstNo.editText?.setText(dealerDataUpdate!!.gstNo)
        binding.adharcardNo.editText?.setText(dealerDataUpdate!!.adharcardNo)
        binding.panCardNo.editText?.setText(dealerDataUpdate!!.panCardNo)
        binding.nameOfBank.editText?.setText(dealerDataUpdate!!.nameOfBank)
        binding.bankAcNo.editText?.setText(dealerDataUpdate!!.bankAcNo)
        binding.ifscCode.editText?.setText(dealerDataUpdate!!.ifscCode)
        binding.insecticideLicenseNo.editText?.setText(dealerDataUpdate!!.insecticideLicenseNo)
        binding.insecticideLicenseValidity.editText?.setText(dealerDataUpdate!!.insecticideLicenseValidity)
        binding.fertilizerLicenseNo.editText?.setText(dealerDataUpdate!!.fertilizerLicenseNo)
        binding.fertilizerLicenseValidity.editText?.setText(dealerDataUpdate!!.fertilizerLicenseValidity)
        binding.amountOfFirstCheque.editText?.setText(dealerDataUpdate!!.amountOfFirstCheque.toString())
        binding.amountOfFirstChequeNo.editText?.setText(dealerDataUpdate!!.amountOfFirstChequeNo)
        binding.amountOfCheque2.editText?.setText(dealerDataUpdate!!.amountOfCheque2)
        binding.amountOfChequeNo2.editText?.setText(dealerDataUpdate!!.amountOfChequeNo2)
        binding.amountOfCheque3.editText?.setText(dealerDataUpdate!!.amountOfCheque3)
        binding.amountOfChequeNo3.editText?.setText(dealerDataUpdate!!.amountOfChequeNo3)
        binding.amountOfCheque4.editText?.setText(dealerDataUpdate!!.amountOfCheque4)
        binding.amountOfChequeNo4.editText?.setText(dealerDataUpdate!!.amountOfChequeNo4)
        binding.firmAddress.editText?.setText(dealerDataUpdate!!.firmAddress)
        binding.firmTaluka.editText?.setText(dealerDataUpdate!!.firmTakula)
        binding.firmZipcode.editText?.setText(dealerDataUpdate!!.firmZipcode.toString())
//        binding.residentialAddress1.editText?.setText(dealerDataUpdate!!.)
        binding.residentialTakula.editText?.setText(dealerDataUpdate!!.residentialTakula)
        binding.residentialZipcode.editText?.setText(dealerDataUpdate!!.residentialZipcode)
        GlideApp.with(requireContext())
            .load(dealerDataUpdate!!.image)
            .fitCenter()
            .into(binding.profilePic)
            .onLoadFailed(
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.logo,
                    requireContext().theme
                )
            )
    }

    private fun callGetFirmCityListApi(stateId: String) {
        Utils.showProgress(requireContext())
        APIClient.getApiInterface()
            .getCityList(preference?.getString(AppConstant.SALESMAN_ID, ""), stateId)
            .enqueue(object : Callback<CityListResponse> {
                override fun onResponse(
                    call: Call<CityListResponse>,
                    response: Response<CityListResponse>
                ) {
                    Utils.hideProgress()
                    val body = response.body()
                    if (body != null) {
                        if (body.meta.code.equals("200")) {
                            cityFirmArray = Array(body.data.size) { "" }
                            cityFirmIdArray = Array(body.data.size) { "" }
                            for (index in 0 until body.data.size) {
                                val item = body.data[index]
                                cityFirmArray[index] = item.name
                                cityFirmIdArray[index] = item.id.toString()
                            }
                            val firm_adapter = ArrayAdapter(
                                requireContext(),
                                R.layout.dropdown_layout_item,
                                cityFirmArray
                            )
                            (binding.firmCityId.editText as AutoCompleteTextView).setAdapter(
                                firm_adapter
                            )
                            (binding.firmCityId.editText as AutoCompleteTextView).setText("")
                            selectedFirmCityId = ""

                            if (isEdit && loadResidentialCityAgain) {
                                loadResidentialCityAgain = false
                                callGetResidentialCityListApi(dealerDataUpdate!!.residentialStateId.toString())
                            }
                        } else {
                            showError(body.meta.message)
                        }
                    } else {
                        showError(response.message())
                    }

                }

                override fun onFailure(call: Call<CityListResponse>, t: Throwable) {
                    Utils.hideProgress()
                    t.printStackTrace()
                    showError(t.message!!)
                }

            })
    }

    private fun callSubmitDetails() {
        Utils.showProgress(requireContext())
        val mediaType = "text/plain".toMediaType()
        val salesman_id =
            preference?.getString(AppConstant.SALESMAN_ID, "")!!.toRequestBody(mediaType)
        val firm_name = binding.firmName.editText?.text.toString().toRequestBody(mediaType)
        val dealer_qualification =
            binding.dealerQualification.editText?.text.toString().toRequestBody(mediaType)
        val responsible_person_name =
            binding.responsiblePersonName.editText?.text.toString().toRequestBody(mediaType)
        val residential_address =
            binding.residentialAddress.editText?.text.toString().toRequestBody(mediaType)
        val mobile_no = binding.mobileNo.editText?.text.toString().toRequestBody(mediaType)
        val whatsapp_no = binding.whatsappNo.editText?.text.toString().toRequestBody(mediaType)
        val date_of_birth = binding.dateOfBirth.editText?.text.toString().toRequestBody(mediaType)
        val date_of_anniversary =
            binding.dateOfAnniversary.editText?.text.toString().toRequestBody(mediaType)
        val year_of_establishment =
            binding.yearOfEstablishment.editText?.text.toString().toRequestBody(mediaType)
        val nature_of_firm = binding.natureOfFirm.editText?.text.toString().toRequestBody(mediaType)
        val total_turnover =
            binding.totalTurnover.editText?.text.toString().toRequestBody(mediaType)
        val pesticide = binding.pesticide.editText?.text.toString().toRequestBody(mediaType)
        val fertilizers = binding.fertilizers.editText?.text.toString().toRequestBody(mediaType)
        val pgr_other = binding.pgrOther.editText?.text.toString().toRequestBody(mediaType)
        val ownership_of_shop =
            binding.ownershipOfShop.editText?.text.toString().toRequestBody(mediaType)
        val other_agency_names =
            binding.otherAgencyNames.editText?.text.toString().toRequestBody(mediaType)
        val gst_no = binding.gstNo.editText?.text.toString().toRequestBody(mediaType)
        val adharcard_no = binding.adharcardNo.editText?.text.toString().toRequestBody(mediaType)
        val pan_card_no = binding.panCardNo.editText?.text.toString().toRequestBody(mediaType)
        val name_of_bank = binding.nameOfBank.editText?.text.toString().toRequestBody(mediaType)
        val bank_ac_no = binding.bankAcNo.editText?.text.toString().toRequestBody(mediaType)
        val ifsc_code = binding.ifscCode.editText?.text.toString().toRequestBody(mediaType)
        val insecticide_license_no =
            binding.insecticideLicenseNo.editText?.text.toString().toRequestBody(mediaType)
        val insecticide_license_validity =
            binding.insecticideLicenseValidity.editText?.text.toString().toRequestBody(mediaType)
        val fertilizer_license_no =
            binding.fertilizerLicenseNo.editText?.text.toString().toRequestBody(mediaType)
        val fertilizer_license_validity =
            binding.fertilizerLicenseValidity.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_first_cheque =
            binding.amountOfFirstCheque.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_first_cheque_no =
            binding.amountOfFirstChequeNo.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_2 =
            binding.amountOfCheque2.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_no_2 =
            binding.amountOfChequeNo2.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_3 =
            binding.amountOfCheque3.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_no_3 =
            binding.amountOfChequeNo3.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_4 =
            binding.amountOfCheque4.editText?.text.toString().toRequestBody(mediaType)
        val amount_of_cheque_no_4 =
            binding.amountOfChequeNo4.editText?.text.toString().toRequestBody(mediaType)
        val firm_address = binding.firmAddress.editText?.text.toString().toRequestBody(mediaType)
        val firm_state_id = selectedFirmStateId.toRequestBody(mediaType)
        val firm_city_id = selectedFirmCityId.toRequestBody(mediaType)
        val firm_taluka = binding.firmTaluka.editText?.text.toString().toRequestBody(mediaType)
        val firm_zipcode = binding.firmZipcode.editText?.text.toString().toRequestBody(mediaType)
        val residential_address_1 =
            binding.residentialAddress1.editText?.text.toString().toRequestBody(mediaType)
        val residential_state_id = selectedResidentialStateId.toRequestBody(mediaType)
        val residential_city_id = selectedResidentialCityId.toRequestBody(mediaType)
        val residential_takula =
            binding.residentialTakula.editText?.text.toString().toRequestBody(mediaType)
        val residential_zipcode =
            binding.residentialZipcode.editText?.text.toString().toRequestBody(mediaType)
        val avatar = if (profilePictureFileName.isEmpty()) {
            MultipartBody.Part.createFormData("image", "", "".toRequestBody(mediaType))
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                profilePictureFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("image", file.name, fileBody)
        }
        val partnership_memorandum_of_firm_doc = if (partnershipMemorandumDocFileName.isEmpty()) {
            MultipartBody.Part.createFormData(
                "partenership_memorandum_of_firm_doc",
                "",
                "".toRequestBody(mediaType)
            )
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                partnershipMemorandumDocFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData(
                "partenership_memorandum_of_firm_doc",
                file.name,
                fileBody
            )
        }
        val pesticide_license_doc = if (pesticideFileName.isEmpty()) {
            MultipartBody.Part.createFormData(
                "pesticide_license_doc",
                "",
                "".toRequestBody(mediaType)
            )
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                pesticideFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("pesticide_license_doc", file.name, fileBody)
        }
        val fertilizer_license_doc = if (fertilizerFileName.isEmpty()) {
            MultipartBody.Part.createFormData(
                "fertilizer_license_doc",
                "",
                "".toRequestBody(mediaType)
            )
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                fertilizerFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("fertilizer_license_doc", file.name, fileBody)
        }
        val gst_certificate_doc = if (gstCertificateFileName.isEmpty()) {
            MultipartBody.Part.createFormData(
                "gst_certificate_doc",
                "",
                "".toRequestBody(mediaType)
            )
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                gstCertificateFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("gst_certificate_doc", file.name, fileBody)
        }
        val adharcard_doc = if (adharCardFileName.isEmpty()) {
            MultipartBody.Part.createFormData("adharcard_doc", "", "".toRequestBody(mediaType))
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                adharCardFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("adharcard_doc", file.name, fileBody)
        }
        val pancard_doc = if (panCardFileName.isEmpty()) {
            MultipartBody.Part.createFormData("pancard_doc", "", "".toRequestBody(mediaType))
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                panCardFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("pancard_doc", file.name, fileBody)
        }
        val electricity_bill_doc = if (electricityBillFileName.isEmpty()) {
            MultipartBody.Part.createFormData(
                "electricity_bill_doc",
                "",
                "".toRequestBody(mediaType)
            )
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                electricityBillFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("electricity_bill_doc", file.name, fileBody)
        }
        val rent_agreement_doc = if (rentAgreementFileName.isEmpty()) {
            MultipartBody.Part.createFormData("rent_agreement_doc", "", "".toRequestBody(mediaType))
        } else {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                rentAgreementFileName
            )
            val fileBody: RequestBody = file.asRequestBody(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    file.extension
                )!!.toMediaType()
            )
            MultipartBody.Part.createFormData("rent_agreement_doc", file.name, fileBody)
        }
        APIClient.getApiInterface().addDealer(
            salesman_id,
            firm_name,
            dealer_qualification,
            responsible_person_name,
            residential_address,
            mobile_no,
            whatsapp_no,
            date_of_birth,
            date_of_anniversary,
            year_of_establishment,
            nature_of_firm,
            total_turnover,
            pesticide,
            fertilizers,
            pgr_other,
            ownership_of_shop,
            other_agency_names,
            gst_no,
            adharcard_no,
            pan_card_no,
            name_of_bank,
            bank_ac_no,
            ifsc_code,
            insecticide_license_no,
            insecticide_license_validity,
            fertilizer_license_no,
            fertilizer_license_validity,
            amount_of_first_cheque,
            amount_of_first_cheque_no,
            amount_of_cheque_2,
            amount_of_cheque_no_2,
            amount_of_cheque_3,
            amount_of_cheque_no_3,
            amount_of_cheque_4,
            amount_of_cheque_no_4,
            firm_address,
            firm_state_id,
            firm_city_id,
            firm_taluka,
            firm_zipcode,
            residential_address_1,
            residential_state_id,
            residential_city_id,
            residential_takula,
            residential_zipcode,
            avatar,
            partnership_memorandum_of_firm_doc,
            pesticide_license_doc,
            fertilizer_license_doc,
            gst_certificate_doc,
            adharcard_doc,
            pancard_doc,
            electricity_bill_doc,
            rent_agreement_doc
        ).enqueue(object : Callback<DealerRegistrationResponse> {
            override fun onResponse(
                call: Call<DealerRegistrationResponse>,
                response: Response<DealerRegistrationResponse>
            ) {
                Utils.hideProgress()
                val body = response.body()
                if (body != null) {
                    val meta = body.meta
                    if (meta.code.equals("200")) {
                        Handler().postDelayed(
                            Runnable { (requireActivity() as MainActivity).onBackPressed() },
                            1500
                        )
                    }
                    showError(meta.message)
                } else {
                    showError(response.message())
                }
            }

            override fun onFailure(call: Call<DealerRegistrationResponse>, t: Throwable) {
                Utils.hideProgress()
                showError("Error occurred!! Please try again later")
                t.printStackTrace()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_PROFILE_PICTURE || requestCode == REQUEST_CODE_FOR_PARTNERSHIP_MEMORANDUM_DOC ||
                requestCode == REQUEST_CODE_FOR_PESTICIDE_LICENSE || requestCode == REQUEST_CODE_FOR_FERTILIZER_DOCUMENT ||
                requestCode == REQUEST_CODE_FOR_GST_CERTIFICATE || requestCode == REQUEST_CODE_FOR_ADHAR_CARD ||
                requestCode == REQUEST_CODE_FOR_PAN_CARD || requestCode == REQUEST_CODE_FOR_ELECTRICITY_BILL || requestCode == REQUEST_CODE_FOR_RENT_AGREEMENT
            ) {
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
                            REQUEST_CODE_FOR_PARTNERSHIP_MEMORANDUM_DOC -> {
                                partnershipMemorandumDocFileName = it.getString(nameIndex)!!
                                binding.txtPartnershipMemorandumOfFirmDocStatus.text =
                                    if (partnershipMemorandumDocFileName.isNotEmpty()) {
                                        "Selected"
                                    } else {
                                        "Not Selected"
                                    }
                                partnershipMemorandumDocFileName
                            }
                            REQUEST_CODE_FOR_PESTICIDE_LICENSE -> {
                                pesticideFileName = it.getString(nameIndex)
                                binding.txtPesticideLicenseDocStatus.text =
                                    if (pesticideFileName.isNotEmpty()) {
                                        "Selected"
                                    } else {
                                        "Not Selected"
                                    }
                                pesticideFileName
                            }
                            REQUEST_CODE_FOR_FERTILIZER_DOCUMENT -> {
                                fertilizerFileName = it.getString(nameIndex)
                                binding.txtFertilizerLicenseDocStatus.text =
                                    if (fertilizerFileName.isNotEmpty()) {
                                        "Selected"
                                    } else {
                                        "Not Selected"
                                    }
                                fertilizerFileName
                            }
                            REQUEST_CODE_FOR_GST_CERTIFICATE -> {
                                gstCertificateFileName = it.getString(nameIndex)
                                binding.txtGstCertificateDocStatus.text =
                                    if (gstCertificateFileName.isNotEmpty()) {
                                        "Selected"
                                    } else {
                                        "Not Selected"
                                    }
                                gstCertificateFileName
                            }
                            REQUEST_CODE_FOR_ADHAR_CARD -> {
                                adharCardFileName = it.getString(nameIndex)
                                binding.txtAdharcardDocStatus.text =
                                    if (adharCardFileName.isNotEmpty()) {
                                        "Selected"
                                    } else {
                                        "Not Selected"
                                    }
                                adharCardFileName
                            }
                            REQUEST_CODE_FOR_PAN_CARD -> {
                                panCardFileName = it.getString(nameIndex)
                                binding.txtPanCardDocStatus.text =
                                    if (panCardFileName.isNotEmpty()) {
                                        "Selected"
                                    } else {
                                        "Not Selected"
                                    }
                                panCardFileName
                            }
                            REQUEST_CODE_FOR_ELECTRICITY_BILL -> {
                                electricityBillFileName = it.getString(nameIndex)
                                binding.txtElectricityBillDocStatus.text =
                                    if (electricityBillFileName.isNotEmpty()) {
                                        "Selected"
                                    } else {
                                        "Not Selected"
                                    }
                                electricityBillFileName
                            }
                            REQUEST_CODE_FOR_RENT_AGREEMENT -> {
                                rentAgreementFileName = it.getString(nameIndex)
                                binding.txtRentAgreementDocStatus.text =
                                    if (rentAgreementFileName.isNotEmpty()) {
                                        "Selected"
                                    } else {
                                        "Not Selected"
                                    }
                                rentAgreementFileName
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
                    if (requestCode == REQUEST_CODE_FOR_PROFILE_PICTURE) {
                        val file = File(
                            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                            profilePictureFileName
                        )
                        if (file.exists()) {
                            binding.profilePic.setImageBitmap(BitmapFactory.decodeStream(file.inputStream()))
                        }
                    }
                }
            }
        }
    }

    private fun showError(string: String) {
        Utils.showSnackBar(binding.root, string)
    }
}