package nl.joozd.logbooktest1.comm.utils

//TODO: MAKE A WAY TO SAVE A USER/PASS COMBO
const val userName = "testaccount"
const val password = "jemoeder123"

fun String.addCredentials(): String = "$userName:$password:$this"