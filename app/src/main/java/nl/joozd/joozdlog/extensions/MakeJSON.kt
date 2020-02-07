/*
 * JoozdLog Pilot's Logbook
 * Copyright (C) 2020 Joost Welle
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses
 */

package nl.joozd.joozdlog.extensions


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