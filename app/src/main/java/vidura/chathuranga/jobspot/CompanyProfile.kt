package vidura.chathuranga.jobspot

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.KeyListener
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.regex.Pattern


class CompanyProfile : Fragment() {

    private lateinit var companyName: EditText
    private lateinit var companyEmail: EditText
    private lateinit var phone: EditText
    private lateinit var location: EditText
    private lateinit var description: EditText
    private lateinit var editBtn: Button
    private lateinit var delBtn: Button
    private lateinit var signOut : Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var company: CompanyModel
    private lateinit var uid: String

    private lateinit var companyNameListener: KeyListener
    private lateinit var companyEmailListener: KeyListener
    private lateinit var phoneListener: KeyListener
    private lateinit var locationListener: KeyListener
    private lateinit var descriptionListener: KeyListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        activity?.setTitle("Jobspot - Company Profile")
        val view = inflater.inflate(R.layout.fragment_company_profile, container, false)

        companyName = view.findViewById(R.id.companyNameEdt)
        companyEmail = view.findViewById(R.id.emailEdt)
        phone = view.findViewById(R.id.phoneNumberEdt)
        location = view.findViewById(R.id.locationEdt)
        description = view.findViewById(R.id.descEdt)
        editBtn = view.findViewById(R.id.edit_btn)
        delBtn = view.findViewById(R.id.deleteBtn)
        signOut = view.findViewById(R.id.signoutbtn)

        //get default keyListener
        companyNameListener = companyName.keyListener
        companyEmailListener = companyEmail.keyListener
        phoneListener = phone.keyListener
        locationListener = location.keyListener
        descriptionListener = description.keyListener

        //set Text boxes readOnly
        companyName.keyListener = null
        companyEmail.keyListener = null
        phone.keyListener = null
        location.keyListener = null
        description.keyListener = null

        getCurrentUserDetails()

        editBtn.setOnClickListener {

            if (editBtn.text.toString().compareTo("Edit") == 0) {
                Toast.makeText(activity, "You can edit your profile Now", Toast.LENGTH_LONG).show()
                //set Text boxes editable
                companyName.keyListener = companyNameListener
                companyEmail.keyListener = companyEmailListener
                phone.keyListener = phoneListener
                location.keyListener = locationListener
                description.keyListener = descriptionListener

//                Edit button text change to Save
                editBtn.text = "Save"
            } else if (editBtn.text.toString().compareTo("Save") == 0) {

                //call to edit user function
                editUserDetails()
            }


        }

        delBtn.setOnClickListener {
            deleteUser()
        }

        signOut.setOnClickListener {
            userSignOut()
        }
        return view
    }

    private fun getCurrentUserDetails() {
        //        database initialize
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        dbRef = FirebaseDatabase.getInstance().getReference("Companiees")

        if (uid.isNotEmpty()) {
            getUserData()
        }
    }

    private fun getUserData() {
        dbRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                company = snapshot.getValue(CompanyModel::class.java)!!

                companyName.setText(company.companyName)
                phone.setText(company.phoneNumber)
                location.setText(company.location)
                companyEmail.setText(company.companyEmail)
                description.setText(company.description)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    companyHome(),
                    "Something went Wrong when fetching Data",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }

    private fun editUserDetails(){
        // getting current user
        val currentUser = auth.currentUser
        dbRef = FirebaseDatabase.getInstance().getReference("Companiees")

        val cName = companyName!!.text.toString()
        val cEmail = companyEmail!!.text.toString()
        val cPhone = phone!!.text.toString()
        val cLocation = location!!.text.toString()
        val cDesc = description!!.text.toString()

        var errorCount : Int  = 0

        if (cName.isEmpty()){
            errorCount++
            companyName.error="Enter a name"
        }
        if(cEmail.isEmpty()){
            errorCount++
            companyEmail.error = "Enter a email"
        }

        if(cEmail.isNotEmpty()){
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
            val matcher = pattern.matcher(cEmail)

            if (!matcher.matches()) {
                errorCount++
                companyEmail.error = "Enter valid email"
            }
        }
        val editMap = mapOf(
            "companyId" to currentUser?.uid.toString(),
            "companyName" to cName,
            "companyEmail" to cEmail,
            "phoneNumber" to cPhone,
            "location" to cLocation,
            "description" to cDesc
        )

        currentUser!!.updateEmail(cEmail).addOnCompleteListener {
            if (it.isSuccessful) {
                dbRef.child(currentUser?.uid.toString()).updateChildren(editMap)

                //set Text boxes readOnly again
                companyName.keyListener = null
                companyEmail.keyListener = null
                phone.keyListener = null
                location.keyListener = null
                description.keyListener = null

                //when update successfully, toast message will be displayed
                Toast.makeText(activity, "Profile updated Successfully!", Toast.LENGTH_LONG)
                    .show()

                //trans save button to text
                editBtn.text = "Edit"
            }else{
                //when update unSuccess, toast message will be displayed
                Toast.makeText(activity, "Profile was not updated!", Toast.LENGTH_LONG)
            }
        }.addOnFailureListener {
            Toast.makeText(activity, "Something went Wrong!", Toast.LENGTH_LONG).show()

        }
    }

    private fun deleteUser(){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete User")
        builder.setMessage("Are you sure ?")
        builder.setIcon(R.drawable.delete_bin)
        builder.setCancelable(false)

        builder.setPositiveButton("Yes"){_,_->
            dbRef = FirebaseDatabase.getInstance().getReference("Companiees")
            val currentUser = auth.currentUser

            currentUser!!.delete().addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(activity, "User is deleted", Toast.LENGTH_LONG).show()
                    dbRef.child(currentUser!!.uid.toString()).removeValue()
                    FirebaseAuth.getInstance().signOut()
                }else{
                    Toast.makeText(activity,"Something Went Wrong!",Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
                Toast.makeText(activity,"Something Went Wrong!",Toast.LENGTH_LONG).show()
            }
        }

        builder.setNegativeButton("Cancel"){_,_->

        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun userSignOut(){
        if(auth.currentUser!=null){
            auth.signOut()
            var intent = Intent(activity, CompanyLogin::class.java)
            startActivity(intent)
            activity?.finish()

        }
    }

}