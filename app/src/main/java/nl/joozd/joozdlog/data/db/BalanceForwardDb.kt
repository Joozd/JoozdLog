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

package nl.joozd.joozdlog.data.db

import nl.joozd.joozdlog.data.BalanceForward
import nl.joozd.joozdlog.extensions.parseList
import nl.joozd.joozdlog.extensions.toVarargArray
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