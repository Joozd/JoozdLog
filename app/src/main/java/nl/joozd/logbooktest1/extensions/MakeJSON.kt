package nl.joozd.logbooktest1.extensions


fun List<Any>.makeJSON(): String{ // will make JSON List
    var output="["
    forEach {
        when (it){
            is Int -> output += "$it,"
            is String -> output += "\"$it\","
            is Boolean -> output += "$it,"
            is Float ->  output += "$it,"
            is Char -> output += "$it,"
            else -> output += "\"INVALID DATA\","
        }
    }
    output=output.dropLast(1)
    output += "]"
    return output
}

fun Map<String, Any>.makeJSON(): String{ // Will make a JSON string of simple Maps (with values only Int String Bool Float Char or List of those types
    var output="{"
    forEach {
        output += "\"${it.key}\":"
        when (it.value){
            is Int -> output += "${it.value},"
            is String -> output += "\"${it.value}\","
            is Boolean -> output += "${it.value},"
            is Float ->  output += "${it.value},"
            is Char -> output += "\'${it.value}\',"
            is List<*> -> {
                val list= it.value as List<Any>
                output += list.makeJSON()
            }
            else -> output += "\"INVALID DATA\","
        }
    }
    output=output.dropLast(1)
    output += "}"
    return output
}