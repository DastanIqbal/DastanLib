package com.dastanapps.dastanlib.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.io.*
import java.nio.channels.FileChannel
import java.text.DecimalFormat

/**
 * Created by dastaniqbal on 17/02/2017.
 * 17/02/2017 3:28
 */

object FileUtils {

    fun getBasename(path: String?): String? {
        if (path == null) {
            return null
        }
        var startIndex = path.lastIndexOf('/') + 1
        var endIndex = path.lastIndexOf('.')
        if (startIndex < 0) {
            startIndex = 0
        }
        if (endIndex < 0) {
            endIndex = path.length - 1
        }
        if (startIndex > endIndex) {
            startIndex = endIndex
        }
        return path.substring(startIndex, endIndex)
    }

    fun saveFile(context: Context, path: String, filename: String, content: String): Boolean {
        try {
            val fos = context.openFileOutput("$filename.txt", Context.MODE_PRIVATE)
            val out = OutputStreamWriter(fos)
            out.write(content)
            out.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    fun saveBitmap(bitmap: Bitmap, path: String): String? {
        val file = File(path)
        file.parentFile.mkdirs()
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(path)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            return path
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun getRealPathFromUri(context: Context, tempUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(tempUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    fun deleteFolder(folderPath: String) {
        deleteFolder(File(folderPath))
    }

    fun deleteFolder(operationDir: File) {
        if (operationDir.isDirectory && operationDir.listFiles() != null)
            for (child in operationDir.listFiles())
                deleteFolder(child)
        operationDir.delete()
    }

    fun deleteFile(file: File?) {
        if (file == null) return
        if (file.isFile) file.delete()
    }

    fun getHumanReadableFileSize(path: String): String {
        val file = File(path)
        return if (file.length() > 0) {
            humanReadableByteCount(file.length(), true)
        } else ""
    }

    fun getFileSize(size: Long): String {
        if (size <= 0)
            return "0"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }

    fun humanReadableByteCount(bytes: Long, si: Boolean): String {
        val unit = if (si) 1000 else 1024
        if (bytes < unit) return bytes.toString() + " B"
        val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
        val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
        return String.format("%.1f %sB", bytes / Math.pow(unit.toDouble(), exp.toDouble()), pre)
    }

    fun getFileNames(folder: String, filter: String): Array<String> {
        val folderDir = File(folder)
        return folderDir.list { dir, name -> name.endsWith(".$filter") }
    }

    fun getFiles(folder: String, filter: String): Array<File> {
        val folderDir = File(folder)
        return folderDir.listFiles { dir, name -> name.endsWith(".$filter") }
    }

    fun readFileContent(filePath: String): String {
        val stringBuilder = StringBuilder()
        try {
            val fileOutputStream = FileInputStream(filePath)
            val bufferedReader = BufferedReader(InputStreamReader(fileOutputStream))
            var receiveString = bufferedReader.readLine()
            while (receiveString != null) {
                stringBuilder.append(receiveString)
                receiveString = bufferedReader.readLine()
            }
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }

    fun writeFileContent(filePath: String, data: String) {
        try {
            val outputStreamWriter = OutputStreamWriter(FileOutputStream(filePath))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }

    }

    fun readTextFileFromStream(inputStream: InputStream): String {
        val outputStream = ByteArrayOutputStream()

        val buf = ByteArray(1024)
        var len = inputStream.read(buf)
        try {
            while (len != -1) {
                outputStream.write(buf, 0, len)
                len = inputStream.read(buf)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }

        return outputStream.toString()
    }

    @Throws(IOException::class)
    fun copyFile(sourceFile: File, destFile: File) {
        if (Build.VERSION.SDK_INT > 19) {
            FileInputStream(sourceFile).use { `in` ->
                FileOutputStream(destFile).use { out ->
                    // Transfer bytes from in to out
                    val buf = ByteArray(1024)
                    var len = `in`.read(buf)
                    while (len > 0) {
                        out.write(buf, 0, len)
                        len = `in`.read(buf)
                    }
                }
            }
        } else {
            if (!destFile.parentFile.exists())
                destFile.parentFile.mkdirs()

            if (!destFile.exists()) {
                destFile.createNewFile()
            }

            var source: FileChannel? = null
            var destination: FileChannel? = null

            try {
                source = FileInputStream(sourceFile).channel
                destination = FileOutputStream(destFile).channel
                destination!!.transferFrom(source, 0, source!!.size())
            } finally {
                source?.close()
                destination?.close()
            }
        }
    }
}
