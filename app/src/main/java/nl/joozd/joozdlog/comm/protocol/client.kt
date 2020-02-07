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

import android.util.Log
import java.io.BufferedInputStream
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLSocketFactory


const val SERVER_URL = "joozd.nl"
const val SERVER_PORT = 1337



class Client {
    private val socket = SSLSocketFactory.getDefault().createSocket(SERVER_URL, SERVER_PORT)
    fun sendToServer(packet: Packet): String{
        val message = packet.output
        Log.d("SendToServer:", message.take(40).toByteArray().toString(Charsets.UTF_8))
        try {
            socket.let {
                it.getOutputStream().write(message)
                return "message sent: " + String(message)
            }
        }catch (he: UnknownHostException){
            val exceptionString = "An exception 1 occurred:\n ${he.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, he)
            return   exceptionString
        }catch (ioe: IOException){
            val exceptionString = "An exception 1 occurred:\n ${ioe.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, ioe)
            return   exceptionString
        } catch (ce: ConnectException){
            val exceptionString = "An exception 1 occurred:\n ${ce.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, ce)
            return   exceptionString
        }catch (se: SocketException){
            val exceptionString = "An exception 1 occurred:\n ${se.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, se)
            return   exceptionString
        }
    }

    fun readFromSocket(): Packet?{
        var lengthString = ByteArray(0)
        var receivedData = ByteArray(0)
        var readBuffer=ByteArray(8192)
        //var readByte: Int

        try {
            socket.let {
                val input=BufferedInputStream(it.getInputStream())
                while (lengthString.size < 10) lengthString += input.read().toByte()
                val messageLength = lengthString.take(10).toByteArray().toString(Charsets.UTF_8).toInt()
                Log.d("readFromSocket", "messageLength = $messageLength")
                do{
                    input.read(readBuffer)
                    var readBytes= readBuffer.indexOf(0.toByte())
                    if (readBytes == -1) readBytes = readBuffer.size
                    receivedData += readBuffer.slice(0 until readBytes)
                    readBuffer=ByteArray(8192)
                } while (receivedData.size < messageLength-10)
                while (receivedData.last() == 0.toByte()) receivedData = receivedData.dropLast(1).toByteArray()



                Log.d("readFromSocket", "done receiving ${receivedData.size} bytes of data! First and Last bytes are ${receivedData.first().toInt()} ${receivedData.last().toInt()}")
                Log.d("readFromSocket", receivedData.toString(Charsets.UTF_8))
            }
        }catch (he: UnknownHostException){
            val exceptionString = "An exception 2 occurred:\n ${he.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, he)
            return null
        }catch (ioe: IOException){
            val exceptionString = "An exception 2 occurred:\n ${ioe.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, ioe)
            return null
        } catch (ce: ConnectException){
            val exceptionString = "An exception 2 occurred:\n ${ce.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, ce)
            return null
        }catch (se: SocketException){
            val exceptionString = "An exception 2 occurred:\n ${se.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, se)
            return null
        }catch (nfe:NumberFormatException ){
            val exceptionString = "An exception 2.1 occurred:\n ${nfe.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, nfe)
            return null

        }
        return Packet(receivedData, INBOUND_PACKET)
    }

    fun sendOK(): String{
        try {
            socket.let {
                it.getOutputStream().write("OK".toByteArray(Charsets.UTF_8))
                return "OK Sent"
            }
        }catch (he: UnknownHostException){
            val exceptionString = "An exception 1 occurred:\n ${he.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, he)
            return   exceptionString
        }catch (ioe: IOException){
            val exceptionString = "An exception 1 occurred:\n ${ioe.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, ioe)
            return   exceptionString
        } catch (ce: ConnectException){
            val exceptionString = "An exception 1 occurred:\n ${ce.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, ce)
            return   exceptionString
        }catch (se: SocketException){
            val exceptionString = "An exception 1 occurred:\n ${se.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, se)
            return   exceptionString
        }

    }
    fun receiveOK(): Boolean{
        try {
            socket.let {
                val shouldBeOK: ByteArray = byteArrayOf(it.getInputStream().read().toByte(), it.getInputStream().read().toByte())
                Log.d("receiveOK", shouldBeOK.toString(Charsets.UTF_8))
                return shouldBeOK.toString(Charsets.UTF_8) == "OK"
            }
        }catch (he: UnknownHostException){
            val exceptionString = "An exception 1 occurred:\n ${he.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, he)
            return   false
        }catch (ioe: IOException){
            val exceptionString = "An exception 1 occurred:\n ${ioe.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, ioe)
            return   false
        } catch (ce: ConnectException){
            val exceptionString = "An exception 1 occurred:\n ${ce.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, ce)
            return   false
        }catch (se: SocketException) {
            val exceptionString = "An exception 1 occurred:\n ${se.printStackTrace()}"
            Log.e(javaClass.simpleName, exceptionString, se)
            return false
        }
    }

    fun close(){
        socket.close()
    }

}

