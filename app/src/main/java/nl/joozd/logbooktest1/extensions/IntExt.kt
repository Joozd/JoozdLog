package nl.joozd.logbooktest1.extensions

/**********************************************************************
 *  Functions to get /set bits in an Integer.                         *
 *  Use value.getBit([0-31]) or value.setBit([0-31], [true/false])    *
 *  Returns true for 1 and false for 0                                *
 *  throws an IllegalArgumentException if requested bit doesn't exist *
 **********************************************************************/


// doesnt work on sign bit
fun Int.getBit(n: Int): Boolean {
    require(n < Int.SIZE_BITS-1) { "$n out of range (0 - ${Int.SIZE_BITS-2}" }
    return this.and(1.shl(n)) > 0 // its more than 0 so the set bit is 1, whatever bit it is
}

// this can set your sign bit
fun Int.setBit(n: Int, value: Boolean): Int{
    require(n < Int.SIZE_BITS) { "$n out of range (0 - ${Int.SIZE_BITS-1}" }
    return if (value) this.or(1.shl(n))
    else this.inv().or(1.shl(n)).inv()
}

fun Int.setBit(n: Int, value: Int): Int{
    require(n < Int.SIZE_BITS && n >= 0) { "$n out of range (0 - ${Int.SIZE_BITS-1}" }
    require(value ==0 || value == 1) { "$value not 0 or 1" }
    return if (value == 1) this.or(1.shl(n))
    else this.inv().or(1.shl(n)).inv()
}

fun Int.pow(n: Int): Int{
    var value = 1
    repeat(n){
        value *= this
    }
    return value
}

