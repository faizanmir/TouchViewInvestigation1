@file:JvmName("UriUtils")

package com.example.touchviewinvestigation.extension

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.example.touchviewinvestigation.DocumentFileCompat
import com.example.touchviewinvestigation.MediaFile
import com.example.touchviewinvestigation.StorageId.PRIMARY
import com.example.touchviewinvestigation.getStorageId
import java.io.*

fun Uri.getStorageId(context: Context): String {
    val path = path.orEmpty()
    return if (isRawFile) {
        File(path).getStorageId(context)
    } else when {
        isDownloadsDocument || isDocumentsDocument -> PRIMARY
        isExternalStorageDocument -> path.substringBefore(':', "").substringAfterLast('/')
        else -> ""
    }
}

val Uri.isTreeDocumentFile: Boolean
    get() = path?.startsWith("/tree/") == true

val Uri.isExternalStorageDocument: Boolean
    get() = authority == DocumentFileCompat.EXTERNAL_STORAGE_AUTHORITY

val Uri.isDownloadsDocument: Boolean
    get() = authority == DocumentFileCompat.DOWNLOADS_FOLDER_AUTHORITY

/**
 * For URI [DocumentFileCompat.DOCUMENTS_TREE_URI]
 */
val Uri.isDocumentsDocument: Boolean
    get() = isExternalStorageDocument && path?.let { it.startsWith("/tree/home:") || it.startsWith("/document/home:") } == true

val Uri.isMediaDocument: Boolean
    get() = authority == DocumentFileCompat.MEDIA_FOLDER_AUTHORITY

val Uri.isRawFile: Boolean
    get() = scheme == ContentResolver.SCHEME_FILE

val Uri.isMediaFile: Boolean
    get() = authority == MediaStore.AUTHORITY

fun Uri.toMediaFile(context: Context) = if (isMediaFile) MediaFile(context, this) else null

fun Uri.toDocumentFile(context: Context) = DocumentFileCompat.fromUri(context, this)

@JvmOverloads
@WorkerThread
fun Uri.openOutputStream(context: Context, append: Boolean = true): OutputStream? {
    return try {
        if (isRawFile) {
            FileOutputStream(File(path ?: return null), append)
        } else {
            context.contentResolver.openOutputStream(this, if (append && isTreeDocumentFile) "wa" else "w")
        }
    } catch (e: IOException) {
        null
    }
}

@WorkerThread
fun Uri.openInputStream(context: Context): InputStream? {
    return try {
        if (isRawFile) {
            // handle file from external storage
            FileInputStream(File(path ?: return null))
        } else {
            context.contentResolver.openInputStream(this)
        }
    } catch (e: IOException) {
        null
    }
}