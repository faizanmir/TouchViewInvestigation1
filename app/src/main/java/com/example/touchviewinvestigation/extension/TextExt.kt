@file:JvmName("TextUtils")

package com.example.touchviewinvestigation.extension

import android.os.Build
import com.example.touchviewinvestigation.DocumentFileCompat
import com.example.touchviewinvestigation.DocumentFileCompat.removeForbiddenCharsFromFilename
import com.example.touchviewinvestigation.SimpleStorage
import com.example.touchviewinvestigation.StorageId

/**
 * Created on 19/08/20
 * @author Anggrayudi H
 */

/**
 * Given the following text `abcdeabcjklab` and count how many `abc`, then should return 2.
 */
fun String.count(text: String): Int {
    var index = indexOf(text)
    if (text.isEmpty() || index == -1) {
        return 0
    }
    var count = 0
    do {
        count++
        index = indexOf(text, startIndex = index + text.length)
    } while (index in 1 until length)
    return count
}

fun String.trimFileName() = trim { it <= ' ' || it == '/' }.trimEnd('.')

fun String.normalizeFileName() = removeForbiddenCharsFromFilename().trimFileName()

fun String.trimFileSeparator() = trim('/')

fun String.trimWhiteSpace() = trim { it <= ' ' }

fun String.replaceCompletely(match: String, replaceWith: String) = let {
    var path = it
    do {
        path = path.replace(match, replaceWith)
    } while (path.isNotEmpty() && path.contains(match))
    path
}


fun String.isKitkatSdCardStorageId() =
    Build.VERSION.SDK_INT < 21 && (this == StorageId.KITKAT_SDCARD || this.matches(
        DocumentFileCompat.SD_CARD_STORAGE_ID_REGEX))


fun String.hasParent(parentPath: String): Boolean {
    val parentTree = parentPath.getFolderTree()
    val subFolderTree = getFolderTree()
    return parentTree.size <= subFolderTree.size && subFolderTree.take(parentTree.size) == parentTree
}


fun String.childOf(parentPath: String): Boolean {
    val parentTree = parentPath.getFolderTree()
    val subFolderTree = getFolderTree()
    return subFolderTree.size > parentTree.size && subFolderTree.take(parentTree.size) == parentTree
}


fun String.parent(): String {
    val folderTree = getFolderTree()
    if (folderTree.isEmpty()) {
        return ""
    }
    val parentPath = folderTree.take(folderTree.size - 1).joinToString("/", "/")
    return if (parentPath.startsWith(SimpleStorage.externalStoragePath)
        || parentPath.matches(Regex("/storage/[A-Z0-9]{4}-[A-Z0-9]{4}(.*?)"))
        || Build.VERSION.SDK_INT < 21 && parentPath.startsWith(SimpleStorage.KITKAT_SD_CARD_PATH)
    ) {
        parentPath
    } else {
        ""
    }
}

private fun String.getFolderTree() = split('/')
    .map { it.trimFileSeparator() }
    .filter { it.isNotEmpty() }