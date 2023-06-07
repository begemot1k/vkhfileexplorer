package com.example.vkhfileexplorer

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.vkhfileexplorer.databinding.ActivityContentBinding
import java.io.File

class ContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val item = intent.getSerializableExtra("fileItem") as String
        binding.gifView.setImageURI(File(item).toUri())

        supportActionBar?.apply {
            title = "vkh File Explorer: view"
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            subtitle = File(item).name
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == resources.getIdentifier("home", "id", "android")) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}