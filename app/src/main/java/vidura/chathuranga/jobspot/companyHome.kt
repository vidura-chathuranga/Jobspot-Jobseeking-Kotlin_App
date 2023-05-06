package vidura.chathuranga.jobspot

import android.annotation.SuppressLint
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toolbar

class companyHome : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_home)

        val home = CompanyHomeFrag()
        val vacancy = AddVacancy()
        val profileObj = CompanyProfile()
        val homeBtn : ImageView = findViewById(R.id.home_img_btn)
        val addVacancy : ImageView = findViewById(R.id.vacancy_img_btn)
        val profile : ImageView = findViewById(R.id.cmpny_user_profile_btn)

        homeBtn.setOnClickListener{
            homeBtn.setImageResource(R.drawable.active_home)
            addVacancy.setImageResource(R.drawable.deactive_add_vacancy)
            profile.setImageResource(R.drawable.deactivate_user)


            supportFragmentManager.beginTransaction().apply {
                replace(R.id.company_frag,home)
                commit()
            }
        }

        addVacancy.setOnClickListener{
            homeBtn.setImageResource(R.drawable.deactive_home)
            addVacancy.setImageResource(R.drawable.add_vacancy)
            profile.setImageResource(R.drawable.deactivate_user)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.company_frag,vacancy)
                commit()
            }
        }

        profile.setOnClickListener{
            profile.setImageResource(R.drawable.active_peron)
            addVacancy.setImageResource(R.drawable.deactive_add_vacancy)
            homeBtn.setImageResource(R.drawable.deactive_home)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.company_frag,profileObj)
                commit()
            }
        }
    }


}