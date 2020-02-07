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

package nl.joozd.joozdlog.comm.protocol

const val OUTBOUND_PACKET = true
const val INBOUND_PACKET = false


class Packet (private val input: ByteArray, val outbound : Boolean = false) {
    val header="JOOZDLOG"
    val message: ByteArray
    val output: ByteArray
    init{
        if (outbound)
        {
            message=input
            val length=input.size + 8
            output = length.toString().padStart(10,'0').toByteArray(Charsets.UTF_8) + header.toByteArray(Charsets.UTF_8)+input
        }
        else { // inbound package
            output=input
            if (input.size < 8) {
                throw IllegalArgumentException("Header is not $header")
            }
            if (input.take(8).toByteArray().toString(Charsets.UTF_8) != header) {
                throw IllegalArgumentException("Header is not $header")
            }
            message = input.drop(8).toByteArray()
        }
    }
}