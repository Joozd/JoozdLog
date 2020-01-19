package nl.joozd.logbooktest1.encryption

import java.security.InvalidKeyException
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import javax.crypto.Cipher

class RSAHandler {
    private val keyPair: KeyPair
    private val cipher: Cipher
    private val kpg: KeyPairGenerator

    val publicKey: PublicKey
        get() = keyPair.public

    init {
        kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(2048)
        keyPair = kpg.genKeyPair()
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    }
    fun encrypt(data: ByteArray, publicKey: PublicKey): ByteArray {
        val encryptedBytes: ByteArray
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            encryptedBytes = cipher.doFinal(data)
        }catch (keyError: InvalidKeyException) {
            return ByteArray(0)
        }
        return encryptedBytes
    }
   fun decrypt(data: ByteArray): ByteArray {
        val decryptedBytes: ByteArray
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyPair.private)
            decryptedBytes = cipher.doFinal(data)
        } catch (keyError: InvalidKeyException) {
            return ByteArray(0)
        }
       return decryptedBytes
    }
}


