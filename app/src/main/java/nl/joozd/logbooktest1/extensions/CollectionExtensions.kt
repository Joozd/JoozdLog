package nl.joozd.logbooktest1.extensions

fun <K, V : Any> MutableMap<K, V?>.toVarargArray():
        Array<out Pair<K, V>> =  map { Pair(it.key, it.value!!) }.toTypedArray()