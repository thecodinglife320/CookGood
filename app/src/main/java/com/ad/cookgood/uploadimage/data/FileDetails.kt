package com.ad.cookgood.uploadimage.data

data class FileDetails(
   val bytes: ByteArray,
   val filename: String?,
   val mimeType: String?
) {
   // It's good practice to override equals and hashCode if you use this in collections
   override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false

      other as FileDetails

      if (!bytes.contentEquals(other.bytes)) return false
      if (filename != other.filename) return false
      if (mimeType != other.mimeType) return false

      return true
   }

   override fun hashCode(): Int {
      var result = bytes.contentHashCode()
      result = 31 * result + (filename?.hashCode() ?: 0)
      result = 31 * result + (mimeType?.hashCode() ?: 0)
      return result
   }
}

