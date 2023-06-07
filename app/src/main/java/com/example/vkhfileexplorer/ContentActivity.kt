package com.example.vkhfileexplorer

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.vkhfileexplorer.Utils.Companion.isImage
import com.example.vkhfileexplorer.Utils.Companion.name
import com.example.vkhfileexplorer.Utils.Companion.uri
import com.example.vkhfileexplorer.databinding.ActivityContentBinding

class ContentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val item = intent.getSerializableExtra("fileItem") as String
        if (isImage(item)) {
            binding.gifView.setImageURI(uri(item))
            supportActionBar?.apply {
                title = "vkh File Explorer view"
                subtitle = name(item)
                setHomeButtonEnabled(true)
                setDisplayHomeAsUpEnabled(true)
            }
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