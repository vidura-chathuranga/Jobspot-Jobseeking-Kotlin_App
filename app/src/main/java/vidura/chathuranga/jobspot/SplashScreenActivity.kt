package vidura.chathuranga.jobspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        splash screen timer
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, UserInstruction ::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}