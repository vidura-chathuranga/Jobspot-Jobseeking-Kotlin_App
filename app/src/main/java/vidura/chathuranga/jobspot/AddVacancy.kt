package vidura.chathuranga.jobspot

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import org.w3c.dom.Text


class AddVacancy : Fragment() {

    private lateinit var jobPosition : EditText
    private lateinit var typeOfWorkplace : Spinner
    private lateinit var jobLocation : EditText
    private lateinit var company : EditText
    private lateinit var employmentType : Spinner
    private lateinit var description : EditText
    private lateinit var addJobVacancy : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_vacancy, container, false)

//      re - set interface title
        activity?.setTitle("Jobspot - Add a Vacancy")

        jobPosition = view.findViewById(R.id.job_position_txt)
        typeOfWorkplace = view.findViewById(R.id.typeOfWorkplace)
        jobLocation = view.findViewById(R.id.job_location)
        company = view.findViewById(R.id.comapnyInput)
        employmentType = view.findViewById(R.id.employeeType)
        description  = view.findViewById(R.id.description)
        addJobVacancy = view.findViewById(R.id.post_vacancy_btn)

        addJobVacancy.setOnClickListener {
            postJobVacancy()
        }

        return view
    }

    private fun postJobVacancy(){

        val jobPositionText = jobPosition.text.toString()
        val typeOfWorkplaceText = typeOfWorkplace.selectedItem.toString()
        val jobLocationText = jobLocation.text.toString()
        val companyNameText = company.text.toString()
        val employmentTypeText = employmentType.selectedItem.toString()
        val descriptionText = description.text.toString()

        var errorCount : Int = 0

        if (jobPositionText.isEmpty()){
            errorCount++
            jobPosition.error = "Job vacancy should contain job position"
        }

        if(typeOfWorkplaceText.isEmpty()){
            errorCount++
            val spinnerAsTextView = typeOfWorkplace as TextView
            spinnerAsTextView.error = "You should select Valid input"
        }

        val newJobVacancy = JobVacancyModal(jobPositionText, typeOfWorkplaceText, jobLocationText, companyNameText, employmentTypeText, descriptionText)

    }
}