package net.aridai.simpleqiitaclient.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import net.aridai.simpleqiitaclient.ui.databinding.MainActivityBinding

internal class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity).also {
            this.binding = it
            it.lifecycleOwner = this
        }
    }
}
