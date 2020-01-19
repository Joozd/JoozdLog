package nl.joozd.logbooktest1.data.db

import nl.joozd.logbooktest1.data.BalanceForward
import nl.joozd.logbooktest1.extensions.parseList
import nl.joozd.logbooktest1.extensions.toVarargArray
import org.jetbrains.anko.db.*

class BalanceForwardDb (private val balanceForwardDbHelper: BalanceForwardDbHelper = BalanceForwardDbHelper.instance, private val dataMapper: DbDataMapper = DbDataMapper()){
    val highestId: Int
        get() = balanceForwardDbHelper.use {
            select(BalanceForwardTable.TABLENAME, BalanceForwardTable.ID).orderBy(BalanceForwardTable.ID, SqlOrderDirection.DESC).limit(1)
                .parseOpt(IntParser) ?: 0
        }

    fun requestAllBalanceForwards() = balanceForwardDbHelper.use {
        val balances = select(BalanceForwardTable.TABLENAME).parseList { BalanceForwardData(HashMap(it)) }
        dataMapper.convertBalanceForwardToDomain(balances)
    }
    fun saveBalanceForward(balances: List<BalanceForward>) = balanceForwardDbHelper.use {
        val balanceDataList = dataMapper.convertBalanceForwardFromDomain(balances)
        balanceDataList.forEach {
            with(it) {
                replace(BalanceForwardTable.TABLENAME, *map.toVarargArray())
            }
        }
    }

    fun saveBalanceForward(balanceForward: BalanceForward) = balanceForwardDbHelper.use {
            with(dataMapper.convertBalanceForwardFromDomain(balanceForward)) {
                replace(BalanceForwardTable.TABLENAME, *map.toVarargArray())
            }
    }

    fun deleteBalanceForward(balanceForward: BalanceForward) = balanceForwardDbHelper.use {
        delete(BalanceForwardTable.TABLENAME, "${BalanceForwardTable.ID}=${balanceForward.id}")
    }
}