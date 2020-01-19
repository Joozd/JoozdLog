package nl.joozd.logbooktest1.extensions

fun <K, V> Map<K, V>.reversedMap() = HashMap<V, K>().also { newMap ->
    entries.forEach { newMap.put(it.value, it.key) }
}