package alexey.gritsenko.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class AbstractPlayListActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayListMakerApp.currentActivity=this
    }
}