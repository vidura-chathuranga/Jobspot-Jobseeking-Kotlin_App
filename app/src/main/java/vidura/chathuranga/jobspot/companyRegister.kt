package vidura.chathuranga.jobspot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import vidura.chathuranga.jobspot.databinding.ActivityCompanyRegisterBinding
import java.util.regex.Pattern

class companyRegister : AppCompatActivity() {

    private lateinit var binding: ActivityCompanyRegisterBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_register)

        val companyName: EditText = binding.companyname
        val companyEmail: EditText = binding.companyemail
        val password: EditText = binding.companypassword
        val submitButton: Button = binding.signupbtn


        dbRef = FirebaseDatabase.getInstance().getReference("Companiees")

        submitButton.setOnClickListener {
            saveCompanyData(companyName, companyEmail, password)
        }

    }

    private fun saveCompanyData(
        companyName: EditText,
        companyEmail: EditText,
        password: EditText
    ) {
        //set Error count as Zero
        var errorCount : Int = 0

        //getting register values
        val companyNameText = companyName.text.toString()
        val companyEmailText = companyEmail.text.toString()
        val passwordText = password.text.toString()

        if (companyNameText.isEmpty()) {
            errorCount++
            companyName.error = "Plaese Enter Comapany name"
        }
        if (companyEmailText.isEmpty()) {
            errorCount++
            companyEmail.error = "Plaese Enter Comapany email"
        }
        if (passwordText.isEmpty()) {
            errorCount++
            password.error = "Plaese Enter Comapany password"
        }

        if (!companyEmailText.isEmpty()) {
            val pattern = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
            )
            val matcher = pattern.matcher(companyEmailText)

            if (!matcher.matches()) {
                errorCount++
                companyEmail.error = "Enter valid email"
            }
        }

        if (passwordText.length < 8) {
            errorCount++
            password.error = "Your password should have at least 8 characters"
        }

//        Generate unique ID
        val companyId = dbRef.push().key!!

//        if error doesnt exist in the user input then we add data into out database
        if(errorCount == 0){
            //        create company object
            val company = CompanyModel(companyId, companyNameText, companyEmailText, passwordText)

            dbRef.child(companyId).setValue(company).addOnCompleteListener {

                Toast.makeText(this, "You are Registered Successfully!", Toast.LENGTH_LONG).show()

                print("Success")

                companyName.text.clear()
                companyEmail.text.clear()
                password.text.clear()

            }.addOnFailureListener { err ->
                print("Error")
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        }

    }
}