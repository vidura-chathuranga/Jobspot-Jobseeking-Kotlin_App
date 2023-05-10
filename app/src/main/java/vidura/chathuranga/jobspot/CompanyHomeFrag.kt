package vidura.chathuranga.jobspot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import vidura.chathuranga.jobspot.databinding.FragmentCompanyHomeBinding
import android.widget.TextView
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class CompanyHomeFrag : Fragment() {

    private lateinit var binding: FragmentCompanyHomeBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var companyId: String
    private lateinit var vacancyAdapter: VacancyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var userName: TextView
    private lateinit var activeVacancies: TextView
    private lateinit var searchView: SearchView

    //vacancies list
    private lateinit var vacanciesList: ArrayList<VacancyModel>
    private lateinit var filteredVacanciesList: ArrayList<VacancyModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentCompanyHomeBinding.inflate(inflater, container, false)

        // Initialize the vacancies list
        vacanciesList = ArrayList()

        // Initialize the filtered vacancies list
        filteredVacanciesList = ArrayList()

        // Initialize the vacancy adapter
        vacancyAdapter = VacancyAdapter(filteredVacanciesList)

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

        //fetch username, totalvacancies, activeVacancies and closedvacancies count
        getCompanyUserName()
        getTotalVacancies(uid)
        getActiveVacancies(uid)
        getClosedVacancies(uid)

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
            for (vacancy in it.children) {
                val vacancyModel = vacancy.getValue(VacancyModel::class.java)
                if (vacancyModel != null && vacancyModel.companyId == companyId) {
                    vacanciesList.add(vacancyModel)
                }
            }

            //set the filtered vacancies list
            filteredVacanciesList.clear()
            filteredVacanciesList.addAll(vacanciesList)

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

            //get the search view
            searchView = binding.searchView

            //set the onQueryTextListener to the search view
            // Set the query listener for the search view
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    // Filter the vacancies list based on the search query
                    filterVacancies(newText)
                    return true
                }
            })
        }.addOnFailureListener {
            // Show error message
            Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    private fun getCompanyUserName() {
        dbRef = FirebaseDatabase.getInstance().getReference("Companiees")
        fireBaseAuth = FirebaseAuth.getInstance()
        val currentUserId = fireBaseAuth.currentUser?.uid.toString()

        if (currentUserId.isNotEmpty()) {
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

    private fun getTotalVacancies(uid: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Vacancies")

        dbRef.orderByChild("companyId").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val childCount = snapshot.childrenCount
                    binding.totalVacancyCount.text = childCount.toString()!!
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        activity,
                        "Error While fetching total vacancies",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })

    }

    private fun getActiveVacancies(uid: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Vacancies")

        dbRef.orderByChild("companyId").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val filteredSnapShot = snapshot.children.filter {
                        it.child("closed").getValue(Boolean::class.java) == false
                    }

                    val documentCount = filteredSnapShot.size.toString()
                    binding.textView42.text = documentCount
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        activity,
                        "Error While fetching total vacancies",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })

    }

    private fun getClosedVacancies(uid : String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Vacancies")

        dbRef.orderByChild("companyId").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val filteredSnapShot = snapshot.children.filter {
                        it.child("closed").getValue(Boolean::class.java) == true
                    }

                    val documentCount = filteredSnapShot.size.toString()
                    binding.textView43.text = documentCount
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        activity,
                        "Error While fetching total vacancies",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    private fun filterVacancies(query: String) {
        // Clear the filtered list
        filteredVacanciesList.clear()

        // If the query is empty, add all vacancies to the filtered list
        if (query.isEmpty()) {
            filteredVacanciesList.addAll(vacanciesList)
        } else {
            // Filter the vacancies list based on the search query
            for (vacancy in vacanciesList) {
                if (vacancy.jobPosition?.lowercase(Locale.getDefault())
                        ?.contains(query.lowercase(Locale.getDefault())) == true
                ) {
                    filteredVacanciesList.add(vacancy)
                }
            }
        }

        // Notify the adapter that the data set has changed
        vacancyAdapter.notifyDataSetChanged()
    }
}