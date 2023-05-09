package vidura.chathuranga.jobspot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import vidura.chathuranga.jobspot.databinding.FragmentCompanyHomeBinding
import android.widget.TextView
import com.google.firebase.database.*


class CompanyHomeFrag : Fragment() {

    private lateinit var binding: FragmentCompanyHomeBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var companyId: String
    private lateinit var vacancyAdapter: VacancyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var userName : TextView
    private lateinit var totalVacancies :TextView
    private lateinit var activeVacancies : TextView

    //vacancies list
    private lateinit var vacanciesList: ArrayList<VacancyModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentCompanyHomeBinding.inflate(inflater, container, false)

        // Initialize the vacancies list
        vacanciesList = ArrayList()

        // Initialize the vacancy adapter
        vacancyAdapter = VacancyAdapter(vacanciesList)

        // Initialize the recycler view
        recyclerView = binding.vacancyRecyclerView

        // Set layout manager to the recycler view
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set the adapter to the recycler view
        recyclerView.adapter = vacancyAdapter

        // Initialize Firebase Authentication instance
        fireBaseAuth = FirebaseAuth.getInstance()

        // Get the current user id
        val uid = fireBaseAuth.currentUser?.uid.toString()

        // Get the user name text view
        userName = binding.cUserName

        // Get the total vacancies text view
        totalVacancies = binding.totalVacancyCount

        getCompanyUserName()
        getTotalVacancies()

        // Get the company id from the database
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

        //get all vacancies
        dbRef.get().addOnSuccessListener {
            //get all vacancies
            vacanciesList.clear()
            for (vacancy in it.children){
                val vacancyModel = vacancy.getValue(VacancyModel::class.java)
                if (vacancyModel != null) {
                    vacanciesList.add(vacancyModel)
                }
            }
            //OnClick listener for the vacancy adapter
            vacancyAdapter.setOnItemClickListener(object : VacancyAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    // Open the edit vacancy fragment. pass the vacancy object to the fragment
                    val vacancy = vacanciesList[position]
                    val editVacancyFrag = EditVacancy(vacancy)
                    // Open the edit vacancy fragment
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.company_frag, editVacancyFrag)
                        commit()
                    }

                }
            })
            //notify the adapter
            vacancyAdapter.notifyDataSetChanged()
        }.addOnFailureListener {
            // Show error message
            Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }

        return binding.
    }

    private fun getCompanyUserName(){
        dbRef = FirebaseDatabase.getInstance().getReference("Companiees")
        fireBaseAuth = FirebaseAuth.getInstance()
        val currentUserId = fireBaseAuth.currentUser?.uid.toString()

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