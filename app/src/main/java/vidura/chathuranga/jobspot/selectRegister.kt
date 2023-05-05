package vidura.chathuranga.jobspot

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class selectRegister : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_register)

        var companyRegisterBtn = findViewById<ImageView>(R.id.company_register)
        var companyRegisterText = findViewById<TextView>(R.id.already_have_account)

        var userRegisterBtn = findViewById<ImageView>(R.id.user_Register)
        var userRegisterText = findViewById<TextView>(R.id.user_RegisterText)

        companyRegisterBtn.setOnClickListener{
            var intent = Intent(this, companyRegister::class.java)
            startActivity(intent)
        }

        companyRegisterText.setOnClickListener{
            var intent = Intent(this, companyRegister::class.java)
            startActivity(intent)
        }
        userRegisterBtn.setOnClickListener{
            var intent = Intent(this, UserRegister::class.java)
            startActivity(intent)
        }
        userRegisterText.setOnClickListener {
            var intent = Intent(this, UserRegister::class.java)
            startActivity(intent)
        }

    }
}