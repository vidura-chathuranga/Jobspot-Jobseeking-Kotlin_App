package vidura.chathuranga.jobspot

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.firebase.database.*

class home_page_navigationbar : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_home_navigationbar)

        val home = home_page()
        val searchObj = user_fragment()
        val profileObj = user_profile()
        val homeBtn: ImageView = findViewById(R.id.home_img_btn)
        val search: ImageView = findViewById(R.id.search_img_btn)
        val profile: ImageView = findViewById(R.id.user_profile_btn)

        homeBtn.setOnClickListener {
            homeBtn.setImageResource(R.drawable.active_home)
            search.setImageResource(R.drawable.deactive_search)
            profile.setImageResource(R.drawable.deactivate_user)


            supportFragmentManager.beginTransaction().apply {
                replace(R.id.mainFrag, home)
                commit()
            }
        }

        search.setOnClickListener {
            homeBtn.setImageResource(R.drawable.deactive_home)
            search.setImageResource(R.drawable.active_search)
            profile.setImageResource(R.drawable.deactivate_user)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.mainFrag, searchObj)
                commit()
            }
        }

        profile.setOnClickListener {
            profile.setImageResource(R.drawable.active_peron)
            search.setImageResource(R.drawable.deactive_search)
            homeBtn.setImageResource(R.drawable.deactive_home)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.mainFrag, profileObj)
                commit()
            }
        }
    }


}













