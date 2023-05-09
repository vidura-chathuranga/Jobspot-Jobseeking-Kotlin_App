package vidura.chathuranga.jobspot

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.KeyListener
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.core.Context
import java.util.regex.Pattern

class userEdit : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var genderEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var editBtn: Button
    private lateinit var delButton: Button
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var userNameListener: KeyListener
    private lateinit var userEmailListener: KeyListener
    private lateinit var phoneListener: KeyListener
    private lateinit var locationListener: KeyListener
    private lateinit var genderListener: KeyListener

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_navigation_bar)

        //Initialize views
        fullNameEditText = findViewById(R.id.fullName_edit)
        emailEditText = findViewById(R.id.userEmail_edit)
        //passwordEditText = findViewById(R.id.password)
        genderEditText = findViewById(R.id.gender_edit)
        phoneEditText = findViewById(R.id.phoneNumber_edit)
        locationEditText = findViewById(R.id.location_edit)
        editBtn = findViewById(R.id.editButton)
        delButton = findViewById(R.id.deleteButton)

        getCurrentUserDetails()


        //get default keyListener
        userNameListener = fullNameEditText.keyListener
        userEmailListener = emailEditText.keyListener
        phoneListener = phoneEditText.keyListener
        locationListener = locationEditText.keyListener
        genderListener = genderEditText.keyListener

        //set Text boxes readOnly
        fullNameEditText.keyListener = null
        emailEditText.keyListener = null
        phoneEditText.keyListener = null
        locationEditText.keyListener = null
        genderEditText.keyListener = null


        // Set a click listener on the save button
        editBtn.setOnClickListener {
            if (editBtn.text.toString().compareTo("Edit") == 0) {
                Toast.makeText(this, "You can edit your profile Now", Toast.LENGTH_LONG).show()
                //set Text boxes editable
                fullNameEditText.keyListener = userNameListener
                emailEditText.keyListener = userEmailListener
                phoneEditText.keyListener = phoneListener
                locationEditText.keyListener = locationListener
                genderEditText.keyListener = genderListener

//                Edit button text change to Save
                editBtn.text = "Save"
            } else if (editBtn.text.toString().compareTo("Save") == 0) {

                //call to edit user function
                editUserDetails()
            }


        }
        delButton.setOnClickListener {
            deleteUser()
        }


    }

    private fun getCurrentUserDetails() {
        //        database initialize
        auth = FirebaseAuth.getInstance()
        var uid = auth.currentUser?.uid.toString()

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        if (uid.isNotEmpty()) {
            getUserData()
        }
    }

    private fun getUserData() {
        auth = FirebaseAuth.getInstance()
        var uid = auth.currentUser?.uid.toString()
        dbRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val Employees = snapshot.getValue(EmployeeModel::class.java)!!

                fullNameEditText.setText(Employees.fullName)
                locationEditText.setText(Employees.location)
                phoneEditText.setText(Employees.phoneNumber)
                emailEditText.setText(Employees.userEmail)
                genderEditText.setText(Employees.gender)

            }

            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(
//                    ,
//                    "Something went Wrong when fetching Data",
//                    Toast.LENGTH_LONG
//                ).show()
            }

        })
    }


    private fun editUserDetails() {
        auth = FirebaseAuth.getInstance()
        // getting current user
        val currentUser = auth.currentUser
        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        val uFullName = fullNameEditText!!.text.toString()
        val uEmail = emailEditText!!.text.toString()
        val uGender = genderEditText!!.text.toString()
        val uPhone = phoneEditText!!.text.toString()
        val uLocation = locationEditText!!.text.toString()


        var errorCount: Int = 0

        if (uFullName.isEmpty()) {
            errorCount++
            fullNameEditText.error = "Enter a name"
        }
        if (uEmail.isEmpty()) {
            errorCount++
            emailEditText.error = "Enter a email"
        }

        if (uEmail.isNotEmpty()) {
            errorCount++
            val pattern = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
            )
            val matcher = pattern.matcher(uEmail)

            if (!matcher.matches()) {
                errorCount++
                emailEditText.error = "Enter valid email"
            }
        }
        val editMap = mapOf(
            "empId" to currentUser?.uid.toString(),
            "fullName" to uFullName,
            "userEmail" to uEmail,
            "phoneNumber" to uPhone,
            "gender" to uGender,
            "phoneNumber" to uPhone,
            "location" to uLocation
        )

        currentUser!!.updateEmail(uEmail).addOnCompleteListener {
            if (it.isSuccessful) {
                dbRef.child(currentUser?.uid.toString()).updateChildren(editMap)

                //set Text boxes readOnly again
                fullNameEditText.keyListener = null
                emailEditText.keyListener = null
                // passwordEditText.keyListener = null
                genderEditText.keyListener = null
                phoneEditText.keyListener = null
                locationEditText.keyListener = null

                //when update successfully, toast message will be displayed
                Toast.makeText(this, "Profile updated Successfully!", Toast.LENGTH_LONG)
                    .show()

                //trans save button to text
                editBtn.text = "Edit"
            } else {
                //when update unSuccess, toast message will be displayed
                Toast.makeText(this, "Profile was not updated!", Toast.LENGTH_LONG)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Something went Wrong!", Toast.LENGTH_LONG).show()

        }
    }
    private fun deleteUser(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete User")
        builder.setMessage("Are you sure ?")
        builder.setIcon(R.drawable.delete_bin)
        builder.setCancelable(false)

        builder.setPositiveButton("Yes"){_,_->
            dbRef = FirebaseDatabase.getInstance().getReference("Employees")
            val currentUser = auth.currentUser

            currentUser!!.delete().addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "User is deleted", Toast.LENGTH_LONG).show()
                    dbRef.child(currentUser!!.uid.toString()).removeValue()
                    FirebaseAuth.getInstance().signOut()
                }else{
                    Toast.makeText(this,"Something Went Wrong!",Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this,"Something Went Wrong!",Toast.LENGTH_LONG).show()
            }
        }

        builder.setNegativeButton("Cancel"){_,_->

        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

}









