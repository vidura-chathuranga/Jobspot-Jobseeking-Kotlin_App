package vidura.chathuranga.jobspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class selectRegister : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_register)

        var companyRegisterBtn = findViewById<ImageView>(R.id.company_register)
        var companyRegisterText = findViewById<TextView>(R.id.already_have_account)

        companyRegisterBtn.setOnClickListener{
            var intent = Intent(this, companyRegister::class.java)
            startActivity(intent)
        }

        companyRegisterText.setOnClickListener{
            var intent = Intent(this, companyRegister::class.java)
            startActivity(intent)
        }
    }
}