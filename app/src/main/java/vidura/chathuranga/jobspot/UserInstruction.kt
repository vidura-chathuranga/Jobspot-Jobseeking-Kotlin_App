package vidura.chathuranga.jobspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class UserInstruction : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_instruction)

        var nextButton = findViewById<ImageButton>(R.id.nextBtn)


        nextButton.setOnClickListener{
            var intent = Intent(this, selectRegister::class.java)
            startActivity(intent)
        }
    }
}