package vidura.chathuranga.jobspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRegister : AppCompatActivity() {

    private lateinit var fullName: EditText
    private lateinit var userEmail : EditText
    private lateinit var password : EditText
    private lateinit var gender : EditText
    private lateinit var phoneNumber : EditText
    private lateinit var location : EditText
    private lateinit var btnSignUp : Button

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        fullName = findViewById(R.id.fullName)
        userEmail = findViewById(R.id.userEmail)
        password = findViewById(R.id.password)
        gender = findViewById(R.id.gender)
        phoneNumber = findViewById(R.id.phoneNumber)
        location = findViewById(R.id.location)
        btnSignUp = findViewById(R.id.btnSignUp)

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        btnSignUp.setOnClickListener {
            saveEmployeeData()
    }
}
    private fun saveEmployeeData(){
        val fullNameStr  = fullName.text.toString()
        val userEmailStr = userEmail.text.toString()
        val passwordStr = password.text.toString()
        val genderStr = gender.text.toString()
        val phoneNumberStr = phoneNumber.text.toString()
        val locationStr = location.text.toString()

        if(fullNameStr.isEmpty()){
            fullName.error = "Please enter name"
        }
        if(userEmailStr.isEmpty()){
            userEmail.error = "Please enter Email"
        }
        if(passwordStr.isEmpty()){
            password.error = "Please enter password"
        }
        if(genderStr.isEmpty()){
            gender.error = "Please enter Gender"
        }
        if(phoneNumberStr.isEmpty()){
            phoneNumber.error = "Please enter phone number"
        }
        if(locationStr.isEmpty()){
            location.error = "Please enter location"
        }

        val empId = dbRef.push().key!!

        val employee = EmployeeModel(empId,fullNameStr,userEmailStr,passwordStr,genderStr,phoneNumberStr,locationStr)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener{
                Toast.makeText( this, "Data inserted successfully", Toast.LENGTH_LONG).show()
                var intent = Intent(this,LoginUser::class.java)
                startActivity(intent)
            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}