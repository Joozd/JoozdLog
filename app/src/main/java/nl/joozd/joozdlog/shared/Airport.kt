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

package nl.joozd.joozdlog.shared

data class Airport(val id: Int, val ident: String, val type: String, val name: String, val latitude_deg: Double, val longitude_deg: Double, val elevation_ft: Int,
                   val continent: String, val iso_country: String, val iso_region: String, val municipality: String, val scheduled_service: String,
                   val gps_code: String, val iata_code: String, val local_code: String, val home_link: String, val wikipedia_link: String, val keywords: String)
