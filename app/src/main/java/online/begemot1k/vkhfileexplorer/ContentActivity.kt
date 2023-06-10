package online.begemot1k.vkhfileexplorer

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import online.begemot1k.vkhfileexplorer.Utils.Companion.isImage
import online.begemot1k.vkhfileexplorer.Utils.Companion.name
import online.begemot1k.vkhfileexplorer.Utils.Companion.uri
import online.begemot1k.vkhfileexplorer.databinding.ActivityContentBinding

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