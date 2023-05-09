package vidura.chathuranga.jobspot

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text


class CompanyHomeFrag : Fragment() {

    private lateinit var userName : TextView
    private lateinit var totalVacancies :TextView
    private lateinit var activeVacancies : TextView

    private lateinit var dbRef : DatabaseReference
    private lateinit var  auth : FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("Jobspot - Company Home")
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_company_home, container, false)

        userName =     view.findViewById(R.id.cUserName)
        totalVacancies = view.findViewById(R.id.totalVacancyCount)

        getCompanyUserName()
        getTotalVacancies()
        return view
    }

    private fun getCompanyUserName(){
        dbRef = FirebaseDatabase.getInstance().getReference("Companiees")
        auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser?.uid.toString()

        if(currentUserId.isNotEmpty()){
            dbRef.child(currentUserId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val company = snapshot.getValue(CompanyModel::class.java)!!

                    userName.setText(company.companyName)
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


    }

    private fun getTotalVacancies(){
        dbRef = FirebaseDatabase.getInstance().getReference("Companiees")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val childCount = snapshot.childrenCount
                totalVacancies.text = childCount.toString()!!
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,"Error While fetching total vacancies",Toast.LENGTH_LONG).show()
            }
        })

    }

}