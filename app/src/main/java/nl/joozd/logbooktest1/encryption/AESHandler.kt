package nl.joozd.logbooktest1.encryption

import android.util.Log
import java.security.SecureRandom
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec


// usage: create an instance of AESHandler with a key, that will be used to handle de/encryption of RSA encrypted data
class AESHandler (keyBytes: ByteArray) {
    val key: SecretKey
    init{
        key = SecretKeySpec(keyBytes, "AES")
    }

    fun encrypt (data: ByteArray): AESMessage {
        var nonce = ByteArray(16)
        val r = SecureRandom()

        r.nextBytes(nonce)
        val myParams = GCMParameterSpec(128, nonce)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key, myParams)
        val encryptedDataPlusTag = cipher.doFinal(data)
        val tag = encryptedDataPlusTag.takeLast(16).toByteArray()
        val encryptedData = encryptedDataPlusTag.dropLast(16).toByteArray()
        return AESMessage(nonce+tag+encryptedData)
    }

    fun decrypt (message: AESMessage): ByteArray {
        val decryptedData: ByteArray
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        with (message) {
            val myParams = GCMParameterSpec(128, nonce)
            // AEADBadTagException
            try {
                cipher.init(Cipher.DECRYPT_MODE, key, myParams);
            } catch (keyError: IllegalArgumentException) {
                Log.e("AES Keyerror", "keyError.printStackTrace()", keyError)
                return ByteArray(0)
            }
            try {
                decryptedData = cipher.doFinal(data + tag)
            } catch (bte: AEADBadTagException) {
                Log.e("Bad AES Tag", "bte.printStackTrace()", bte)
                return ByteArray(0)
            }
        }
        return decryptedData
    }

}