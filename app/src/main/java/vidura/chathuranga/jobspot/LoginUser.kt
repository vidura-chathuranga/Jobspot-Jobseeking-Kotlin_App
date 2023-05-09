package vidura.chathuranga.jobspot

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginUser : AppCompatActivity() {

    private lateinit var userEmailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var fireBaseAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        userEmailEditText = findViewById(R.id.user_EmailLogin)
        passwordEditText = findViewById(R.id.user_Password)
        loginButton = findViewById(R.id.login_Btn)

        fireBaseAuth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val emailText = userEmailEditText.text.toString()
            val passwordText = passwordEditText.text.toString()

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            } else {
                fireBaseAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, user_homePage::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
