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

package nl.joozd.joozdlog.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_names_dialog.*
import nl.joozd.joozdlog.R
import nl.joozd.joozdlog.data.utils.NamesWorker
import nl.joozd.joozdlog.extensions.ctx
import nl.joozd.joozdlog.extensions.getColorFromAttr

class NamesPickeradapter(private val namesWorker: NamesWorker, private val itemClick: (String) -> Unit): RecyclerView.Adapter<NamesPickeradapter.ViewHolder>() {
    private val instance = this
    private var currentNames: List<String>  = namesWorker.nameList
    private var selectedName: String? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamesPickeradapter.ViewHolder {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_names_dialog, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindName(currentNames[position])
        holder.itemBackground.setOnClickListener {
            selectedName = holder.nameTextView.text.toString()
            itemClick(holder.nameTextView.text.toString())
            notifyDataSetChanged()
        }
        holder.itemBackground.setBackgroundColor(if (holder.nameTextView.text.toString() == selectedName) holder.itemBackground.ctx.getColorFromAttr(android.R.attr.colorPrimary) else holder.itemBackground.ctx.getColor(R.color.none) )
    }

    override fun getItemCount(): Int = currentNames.size

    fun getNames(query: String){
        currentNames = namesWorker.queryName(query)
        this.notifyDataSetChanged()
    }

    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindName(name: String) {
            nameTextView.text = name
        }
    }
}