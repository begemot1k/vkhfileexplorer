package com.example.vkhfileexplorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.vkhfileexplorer.databinding.FileItemBinding
import java.io.File

class FileItemAdapter(val listener: ClickListener) : RecyclerView.Adapter<FileItemAdapter.FileItemHolder>() {

    private val fileList = ArrayList<FileItem>()

    class FileItemHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = FileItemBinding.bind(item)
        fun bind(fileItem: FileItem, listener: ClickListener) = with(binding) {
            setImage(im, fileItem.fileName)
            tvTitle.text = File(fileItem.fileName).name
            itemView.setOnClickListener {
                listener.onClick(fileItem)
            }
        }

        private fun setImage(im: ImageView?, fileName: String) {
            val ext = File(fileName).extension
            val mimeType: String = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext).toString()
            if (mimeType.startsWith("image/")) {
                im?.setImageURI(File(fileName).toUri())
            } else {
                var ico = binding.root.resources.getIdentifier(ext, "drawable", binding.root.context.packageName)
                if (ico == 0) {
                    ico = R.drawable._blank
                }
                im?.setImageResource(ico)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false)
        return FileItemHolder(view)
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    override fun onBindViewHolder(holder: FileItemHolder, position: Int) {
        holder.bind(fileList[position], listener)
    }


    fun addFile(fileItem: FileItem) {
        fileList.add(fileItem)
    }

    fun resetFiles() {
        fileList.clear()
    }

    fun update() {
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onClick(fileItem: FileItem)
    }

}