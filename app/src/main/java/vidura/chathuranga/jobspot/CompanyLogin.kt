package vidura.chathuranga.jobspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import vidura.chathuranga.jobspot.databinding.ActivityCompanyLoginBinding
import java.util.regex.Pattern

class CompanyLogin : AppCompatActivity() {

    private lateinit var binding: ActivityCompanyLoginBinding
    private lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_login)

        val password : EditText = binding.passwordTxt
        val email :EditText = binding.emailTxt
        val loginBtn:Button = binding.loginBtn
        val signUpBtn : TextView = findViewById(R.id.sign_up_btn)

        signUpBtn.setOnClickListener{
            var intent = Intent(this, companyRegister::class.java)
            startActivity(intent)
        }
        loginBtn.setOnClickListener{
            loginUser(email, password)
        }

    }

    private fun loginUser(email : EditText, password : EditText){

        val emailText = email.text.toString()
        val passwordText = password.text.toString()

        var errorCount : Int = 0

        if(emailText.isEmpty()){
            errorCount++
            email.error = "please enter email"
        }

        if(passwordText.isEmpty()){
            errorCount++
            password.error = "Please enter your password"
        }

        if(!emailText.isEmpty()){
            val pattern = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
            )
            val matcher = pattern.matcher(emailText)

            if (!matcher.matches()) {
                errorCount++
                email.error = "Enter valid email"
            }
        }

        fireBaseAuth = FirebaseAuth.getInstance()

        if(errorCount == 0){
            fireBaseAuth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener {
                if(it.isSuccessful){
                    val intent = Intent(this, companyHome::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener{
                Toast.makeText(this,"Your user credentials are wrong!",Toast.LENGTH_LONG).show()
            }
        }

    }
}