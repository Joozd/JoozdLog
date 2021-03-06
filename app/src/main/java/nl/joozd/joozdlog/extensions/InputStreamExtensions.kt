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

import java.io.InputStream

fun InputStream.toByteArray(): ByteArray {
    val bufferSize: Int = 2048
    var buffer = ByteArray(bufferSize)
    var output = ByteArray(0)
    var len = 0
    len = this.read(buffer)
    while (len != -1) {
        output += buffer
        len = this.read(buffer)
    }
    return output
}