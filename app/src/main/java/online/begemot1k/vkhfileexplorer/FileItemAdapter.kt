package online.begemot1k.vkhfileexplorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import online.begemot1k.vkhfileexplorer.Utils.Companion.extension
import online.begemot1k.vkhfileexplorer.Utils.Companion.isDirectory
import online.begemot1k.vkhfileexplorer.Utils.Companion.isImage
import online.begemot1k.vkhfileexplorer.Utils.Companion.name
import online.begemot1k.vkhfileexplorer.Utils.Companion.uri
import online.begemot1k.vkhfileexplorer.R
import online.begemot1k.vkhfileexplorer.databinding.FileItemBinding

class FileItemAdapter(val listener: ClickListener) : RecyclerView.Adapter<FileItemAdapter.FileItemHolder>() {

    private val fileList = ArrayList<FileItem>()

    class FileItemHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = FileItemBinding.bind(item)
        fun bind(fileItem: FileItem, listener: ClickListener) = with(binding) {
            setImage(im, fileItem.fileName)
            tvTitle.text = name(fileItem.fileName)
            itemView.setOnClickListener {
                listener.onClick(fileItem)
            }
        }

        private fun setImage(im: ImageView?, fileName: String) {
            when {
                isImage(fileName) -> {
                    im?.setImageURI(uri(fileName))
                }

                isDirectory(fileName) -> {
                    im?.setImageResource(R.drawable.ic_folder)
                }

                else -> {
                    var ico = binding.root.resources.getIdentifier(
                        extension(fileName),
                        "drawable",
                        binding.root.context.packageName
                    )
                    if (ico == 0) {
                        ico = R.drawable._blank
                    }
                    im?.setImageResource(ico)
                }
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