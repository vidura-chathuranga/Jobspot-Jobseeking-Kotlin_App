package vidura.chathuranga.jobspot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class CompanyProfile : Fragment() {

    private lateinit var companyName : EditText
    private lateinit var companyEmail : EditText
    private lateinit var phone : EditText
    private lateinit var location : EditText
    private lateinit var  description : EditText
    private lateinit var editBtn : Button
    private lateinit var delBtn : Button

    private lateinit var auth : FirebaseAuth
    private lateinit var  dbRef : DatabaseReference
    private lateinit var company : CompanyModel
    private lateinit var uid : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_company_profile, container, false)

        companyName = view.findViewById(R.id.companyNameEdt)
        companyEmail = view.findViewById(R.id.emailEdt)
        phone = view.findViewById(R.id.phoneNumberEdt)
        location = view.findViewById(R.id.locationEdt)
        description  = view.findViewById(R.id.descEdt)
        editBtn  = view.findViewById(R.id.edit_btn)
        delBtn = view.findViewById(R.id.deleteBtn)

        //set Text boxes readOnly
        companyName.keyListener = null
        companyEmail.keyListener = null
        phone.keyListener = null
        location.keyListener = null
        description.keyListener = null

        getCurrentUserDetails()

        editBtn.setOnClickListener{

        }

        delBtn.setOnClickListener {

        }

        return view
    }

    private fun getCurrentUserDetails(){
        //        database initialize
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        dbRef = FirebaseDatabase.getInstance().getReference("Companiees")

        if(uid.isNotEmpty()){
            getUserData()
        }
    }

    private fun getUserData(){
        dbRef.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                company = snapshot.getValue(CompanyModel::class.java)!!

                companyName.setText(company.companyName)
                phone.setText(company.phoneNumber)
                location.setText(company.location)
                companyEmail.setText(company.companyEmail)
                description.setText(company.description)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(companyHome(), "Something went Wrong when fetching Data",Toast.LENGTH_LONG).show()
            }

        })
    }


}