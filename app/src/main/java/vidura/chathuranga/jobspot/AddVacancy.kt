package vidura.chathuranga.jobspot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import vidura.chathuranga.jobspot.databinding.FragmentAddVacancyBinding

// This line creates a new class called AddVacancy that extends the Fragment class
class AddVacancy : Fragment() {

    // Declare variables for view binding, database reference, Firebase Authentication and company id
    // binding for the fragment
    private lateinit var binding: FragmentAddVacancyBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var companyId: String

    // Declare variables for the various EditText and ImageView elements
    private lateinit var jobPosition: EditText
    private lateinit var typeOfWorkPlace: EditText
    private lateinit var jobLocation: EditText
    private lateinit var employmentType: EditText
    private lateinit var jobDescription: EditText
    private lateinit var backBtn: ImageView
    private lateinit var submitBtn: Button

    // Override the onCreateView method to inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentAddVacancyBinding.inflate(inflater, container, false)

        // Initialize Firebase Authentication instance
        fireBaseAuth = FirebaseAuth.getInstance()

        // Get the current user id
        val uid = fireBaseAuth.currentUser?.uid.toString()

        // Get the company id from the database using the user id
        dbRef = FirebaseDatabase.getInstance().getReference("Companiees")
        // Get the company id from the database using the user id - add onSuccessListener and onFailureListener
        dbRef.child(uid).get().addOnSuccessListener {
            // Get the company id
            companyId = it.child("companyId").value.toString()
        }.addOnFailureListener {
            // Show error message
            Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }

        // Initialize Firebase Realtime Database reference to the Vacancies table
        dbRef = FirebaseDatabase.getInstance().getReference("Vacancies")

        // Return the root view
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
        submitBtn = binding.postVacancyBtn

        // Set on click listener for the back button
        backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.company_frag, CompanyHomeFrag())
                commit()
            }
        }

        // Set on click listener for the submit button
        submitBtn.setOnClickListener {
            saveVacancyData()
        }

        //Add on change listener for the job position text field
        jobPosition.addTextChangedListener {
            validateJobPosition(it.toString())
        }

        //Add on change listener for the type of work place text field
        typeOfWorkPlace.addTextChangedListener {
            validateTypeOfWorkPlace(it.toString())
        }

        //Add on change listener for the job location text field
        jobLocation.addTextChangedListener {
            validateJobLocation(it.toString())
        }

        //Add on change listener for the employment type text field
        employmentType.addTextChangedListener {
            validateEmploymentType(it.toString())
        }

        //Add on change listener for the job description text field
        jobDescription.addTextChangedListener {
            validateJobDescription(it.toString())
        }

    }

    // Save vacancy data to the database
    private fun saveVacancyData() {
        // Get the text field values
        val jobPositionText = jobPosition.text.toString()
        val typeOfWorkPlaceText = typeOfWorkPlace.text.toString()
        val jobLocationText = jobLocation.text.toString()
        val employmentTypeText = employmentType.text.toString()
        val jobDescriptionText = jobDescription.text.toString()
        val vacancyId = dbRef.push().key.toString()

        // Check if all the fields are filled
        if (validateAllFields()) {
            // Create a vacancy object
            val vacancy = VacancyModel(
                vacancyId,
                jobPositionText,
                typeOfWorkPlaceText,
                jobLocationText,
                employmentTypeText,
                jobDescriptionText,
                companyId,
                false
            )

            // Save the vacancy object to the database
            dbRef.child(vacancyId).setValue(vacancy).addOnCompleteListener {
                if (it.isSuccessful) {
                    // Clear all the fields
                    clearAllFields()
                    // Show a toast message
                    Toast.makeText(
                        requireContext(),
                        "Vacancy added successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate to the company home fragment
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.company_frag, CompanyHomeFrag())
                        commit()
                    }
                } else {
                    // Show a toast message
                    Toast.makeText(
                        requireContext(),
                        "Error occurred while adding the vacancy",
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun validateJobPosition(jobPositionText: String): Boolean {
        // Check if the job position text field is empty
        if (jobPositionText.isEmpty()) {
            jobPosition.error = "Please enter a job position"
            return false
        }
        // Check if the job position text field contains only letters and spaces
        if (!jobPositionText.matches("^[a-zA-Z\\s]*$".toRegex())) {
            jobPosition.error = "Please enter a valid job position, only letters and spaces are allowed"
            return false
        }
        // Check if the job position text field contains more than 50 characters or less than 5 characters
        if (jobPositionText.length > 50 || jobPositionText.length < 5) {
            jobPosition.error = "Please enter a valid job position, 5-50 characters are allowed"
            return false
        }
        return true
    }

    private fun validateTypeOfWorkPlace(typeOfWorkPlaceText: String): Boolean {
        // Check if the type of work place text field is empty
        if (typeOfWorkPlaceText.isEmpty()) {
            typeOfWorkPlace.error = "Please enter a type of work place"
            return false
        }
        // Check if the type of work place text field contains only letters and spaces
        if (!typeOfWorkPlaceText.matches("^[a-zA-Z\\s]*$".toRegex())) {
            typeOfWorkPlace.error = "Please enter a valid type of work place, only letters and spaces are allowed"
            return false
        }
        // Check if the type of work place text field contains more than 20 characters or less than 2 characters
        if (typeOfWorkPlaceText.length > 20 || typeOfWorkPlaceText.length < 2) {
            typeOfWorkPlace.error = "Please enter a valid type of work place, 2-20 characters are allowed"
            return false
        }
        return true
    }

    private fun validateJobLocation(jobLocationText: String): Boolean {
        if (jobLocationText.isEmpty()) {
            jobLocation.error = "Please enter a job location"
            return false
        }
        if (!jobLocationText.matches("^[a-zA-Z\\s]*$".toRegex())) {
            jobLocation.error = "Please enter a valid job location, only letters and spaces are allowed"
            return false
        }
        if (jobLocationText.length > 30 || jobLocationText.length < 2) {
            jobLocation.error = "Please enter a valid job location, 2-30 characters are allowed"
            return false
        }
        return true
    }

    private fun validateEmploymentType(employmentTypeText: String): Boolean {
        if (employmentTypeText.isEmpty()) {
            employmentType.error = "Please enter an employment type"
            return false
        }
        if (!employmentTypeText.matches("^[a-zA-Z\\s]*$".toRegex())) {
            employmentType.error = "Please enter a valid employment type, only letters and spaces are allowed"
            return false
        }
        if (employmentTypeText.length > 20 || employmentTypeText.length < 2) {
            employmentType.error = "Please enter a valid employment type, 2-20 characters are allowed"
            return false
        }
        return true
    }

    private fun validateJobDescription(jobDescriptionText: String): Boolean {
        if (jobDescriptionText.isEmpty()) {
            jobDescription.error = "Please enter a job description"
            return false
        }
        if (jobDescriptionText.length > 500 || jobDescriptionText.length < 10) {
            jobDescription.error = "Please enter a valid job description, 10-500 characters are allowed"
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

    private fun clearAllFields() {
        jobPosition.text.clear()
        typeOfWorkPlace.text.clear()
        jobLocation.text.clear()
        employmentType.text.clear()
        jobDescription.text.clear()
    }

}