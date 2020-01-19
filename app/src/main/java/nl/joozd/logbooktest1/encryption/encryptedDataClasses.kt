package nl.joozd.logbooktest1.encryption

class AESMessage (noncePlusTagPlusData: ByteArray) {
    val nonce = noncePlusTagPlusData.slice(0..15).toByteArray()
    val tag = noncePlusTagPlusData.slice(16..31).toByteArray()
    val data = noncePlusTagPlusData.drop(32).toByteArray()

    val sendableData=nonce+tag+data
}


