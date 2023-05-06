package vidura.chathuranga.jobspot

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.core.Context

class userEdit : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var genderEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var editBtn: Button
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_navigation_bar)

        //Initialize views
        fullNameEditText = findViewById(R.id.fullName_edit)
        emailEditText = findViewById(R.id.userEmail_edit)
        genderEditText = findViewById(R.id.gender_edit)
        phoneEditText = findViewById(R.id.phoneNumber_edit)
        locationEditText = findViewById(R.id.location_edit)
        editBtn = findViewById(R.id.editButton)

        // Get Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("Employees").child(
            FirebaseAuth.getInstance().currentUser!!.uid
        )

        // Read user's details from Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val Employees = dataSnapshot.getValue(EmployeeModel::class.java)
                if (Employees != null) {
                    fullNameEditText.setText(Employees.fullName)
                    emailEditText.setText(Employees.userEmail)
                    genderEditText.setText(Employees.gender)
                    phoneEditText.setText(Employees.phoneNumber)
                    locationEditText.setText(Employees.location)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(
                    "EditDetailsActivity",
                    "Failed to read user details.",
                    databaseError.toException()
                )
            }
        })

        // Set a click listener on the save button
        editBtn.setOnClickListener {
            // Save the edited details to Firebase
            val Employees = EmployeeModel(
                fullNameEditText.text.toString(),
                emailEditText.text.toString(),
                genderEditText.text.toString(),
                phoneEditText.text.toString() ,
                locationEditText.text.toString()

            )
            val editMap = mapOf(
                "empId" to  FirebaseAuth.getInstance().currentUser!!.uid,
                "fullName" to fullNameEditText.text.toString(),
                "userEmail" to emailEditText.text.toString(),
                "gender" to genderEditText.text.toString(),
                "phoneNumber" to  phoneEditText.text.toString() ,
                "location" to locationEditText.text.toString()
            )

            databaseReference.updateChildren(editMap).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show()

                }
            }



        }
    }
}







