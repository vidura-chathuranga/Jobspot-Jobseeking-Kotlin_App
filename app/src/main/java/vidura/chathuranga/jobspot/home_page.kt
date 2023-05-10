package vidura.chathuranga.jobspot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [home_page.newInstance] factory method to
 * create an instance of this fragment.
 */
class home_page : Fragment() {

    private lateinit var userName: TextView
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle("Jobspot - User Home")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        userName = view.findViewById(R.id.userName)

        getUserName()

        return view
    }

    private fun getUserName() {
        dbRef = FirebaseDatabase.getInstance().getReference("Employees")
        auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser?.uid.toString()

        if (currentUserId.isNotEmpty()) {
            dbRef.child(currentUserId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val employee = snapshot.getValue(EmployeeModel::class.java)!!

                    userName.setText(employee.fullName)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        user_homePage(),
                        "Something went Wrong when fetching Data",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
        }


    }

}


