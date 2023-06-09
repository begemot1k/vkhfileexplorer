package online.begemot1k.vkhfileexplorer

import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import java.io.File

class Utils {
    companion object {
        fun isImage(fileName: String): Boolean {
            val ext = File(fileName).extension
            val mimeType: String = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext).toString()
            return mimeType.startsWith("image/")
        }

        fun isDirectory(fileName: String): Boolean {
            return File(fileName).isDirectory
        }

        fun name(fileName: String): String {
            return File(fileName).name
        }

        fun extension(fileName: String): String {
            return File(fileName).extension
        }

        fun parent(fileName: String): String {
            return File(fileName).parentFile?.absolutePath ?: "/"
        }

        fun uri(fileName: String): Uri {
            return File(fileName).toUri()
        }

        fun listFiles(path: String): List<String> {
            return File(path).listFiles()
                ?.toList()
                ?.sortedBy { -1 * it.lastModified() }
                ?.map { it.absolutePath }
                ?.toList()
                ?: emptyList()
        }

        fun externalRoot(): String {
            return Environment.getExternalStorageDirectory().absolutePath
        }
    }
}