package vidura.chathuranga.jobspot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import vidura.chathuranga.jobspot.databinding.FragmentEditVacancyBinding

class EditVacancy (private val vacancy : VacancyModel) : Fragment() {
    private lateinit var binding: FragmentEditVacancyBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var fireBaseAuth: FirebaseAuth

    //Text Fields
    private lateinit var jobPosition: EditText
    private lateinit var typeOfWorkPlace: EditText
    private lateinit var jobLocation: EditText
    private lateinit var employmentType: EditText
    private lateinit var jobDescription: EditText
    private lateinit var backBtn: ImageView
    private lateinit var editBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var closeBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentEditVacancyBinding.inflate(inflater, container, false)

        // Initialize Firebase Authentication instance
        fireBaseAuth = FirebaseAuth.getInstance()

        // Initialize Firebase Realtime Database reference to the Vacancies table
        dbRef = FirebaseDatabase.getInstance().getReference("Vacancies")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the text fields
        jobPosition = binding.jobPosition
        typeOfWorkPlace = binding.typeOfWorkplace
        jobLocation = binding.jobLocation
        employmentType = binding.employmentType
        jobDescription = binding.description
        backBtn = binding.backArrowVacancy
        editBtn = binding.editVacancyBtn
        deleteBtn = binding.deleteVacancyBtn
        closeBtn = binding.closeVacancyBtn

        // Set the text fields
        jobPosition.setText(vacancy.jobPosition)
        typeOfWorkPlace.setText(vacancy.typeOfWorkPlace)
        jobLocation.setText(vacancy.jobLocation)
        employmentType.setText(vacancy.employmentType)
        jobDescription.setText(vacancy.jobDescription)

        //If the vacancy is closed, disable the edit and close buttons. change the close button text to "Closed"
        if(vacancy.isClosed){
            editBtn.isEnabled = false
            closeBtn.isEnabled = false
            "Closed".also { closeBtn.text = it }
        }

        // Set the back button click listener
        backBtn.setOnClickListener {
            // Navigate to the company home fragment
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.company_frag, CompanyHomeFrag())
                commit()
            }
        }

        // Set the submit button click listener
        editBtn.setOnClickListener {
            // Call the edit vacancy function
            editVacancy()
        }

        // Set the delete button click listener
        deleteBtn.setOnClickListener {
            // Call the delete vacancy function
            deleteVacancy()
        }

        // Set the close button click listener
        closeBtn.setOnClickListener {
            // Call the close vacancy function
            closeVacancy()
        }
    }

    private fun editVacancy() {
        // Get the text field values
        val jobPosition = jobPosition.text.toString()
        val typeOfWorkPlace = binding.typeOfWorkplace.text.toString()
        val jobLocation = binding.jobLocation.text.toString()
        val employmentType = binding.employmentType.text.toString()
        val jobDescription = binding.description.text.toString()

        if (validateAllFields()) {
            // Create a vacancy object
            val vacancy = VacancyModel(
                vacancy.vacancyId,
                jobPosition,
                typeOfWorkPlace,
                jobLocation,
                employmentType,
                jobDescription,
                vacancy.companyId,
                vacancy.isClosed
            )

            // Update the vacancy in the database
            vacancy.vacancyId?.let { it1 ->
                dbRef.child(it1).setValue(vacancy).addOnSuccessListener {
                    // Show success message
                    Toast.makeText(
                        requireContext(),
                        "Vacancy updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate to the company home fragment
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.company_frag, CompanyHomeFrag())
                        commit()
                    }
                }.addOnFailureListener {
                    // Show error message
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            // Show a toast message
            Toast.makeText(
                requireContext(),
                "Please fill all the fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun closeVacancy() {
        //change isClosed to true and update the vacancy in the database
        vacancy.isClosed = true
        vacancy.vacancyId?.let { it1 ->
            dbRef.child(it1).setValue(vacancy).addOnSuccessListener {
                // Show success message
                Toast.makeText(
                    requireContext(),
                    "Vacancy closed successfully",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate to the company home fragment
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.company_frag, CompanyHomeFrag())
                    commit()
                }
            }.addOnFailureListener {
                // Show error message
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun deleteVacancy() {
        // Delete the vacancy from the database
        vacancy.vacancyId?.let { it1 ->
            dbRef.child(it1).removeValue().addOnSuccessListener {
                // Show success message
                Toast.makeText(
                    requireContext(),
                    "Vacancy deleted successfully",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate to the company home fragment
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.company_frag, CompanyHomeFrag())
                    commit()
                }
            }.addOnFailureListener {
                // Show error message
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun validateJobPosition(jobPositionText: String): Boolean {
        if (jobPositionText.isEmpty()) {
            jobPosition.error = "Please enter a job position"
            return false
        }
        return true
    }

    private fun validateTypeOfWorkPlace(typeOfWorkPlaceText: String): Boolean {
        if (typeOfWorkPlaceText.isEmpty()) {
            typeOfWorkPlace.error = "Please enter a type of work place"
            return false
        }
        return true
    }

    private fun validateJobLocation(jobLocationText: String): Boolean {
        if (jobLocationText.isEmpty()) {
            jobLocation.error = "Please enter a job location"
            return false
        }
        return true
    }

    private fun validateEmploymentType(employmentTypeText: String): Boolean {
        if (employmentTypeText.isEmpty()) {
            employmentType.error = "Please enter an employment type"
            return false
        }
        return true
    }

    private fun validateJobDescription(jobDescriptionText: String): Boolean {
        if (jobDescriptionText.isEmpty()) {
            jobDescription.error = "Please enter a job description"
            return false
        }
        return true
    }

    private fun validateAllFields(): Boolean {
        // Validate all the fields - show error messages if the fields are empty
        var isValid = true
        if (!validateJobPosition(jobPosition.text.toString())) {
            isValid = false
        }
        if (!validateTypeOfWorkPlace(typeOfWorkPlace.text.toString())) {
            isValid = false
        }
        if (!validateJobLocation(jobLocation.text.toString())) {
            isValid = false
        }
        if (!validateEmploymentType(employmentType.text.toString())) {
            isValid = false
        }
        if (!validateJobDescription(jobDescription.text.toString())) {
            isValid = false
        }
        return isValid
    }
}