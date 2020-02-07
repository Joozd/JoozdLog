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

package nl.joozd.joozdlog.utils

import nl.joozd.joozdlog.data.Airport

fun makeAirport(id: Int = 0, ident: String = "", type: String = "", name: String = "", latitude_deg: Double = 0.0, longitude_deg: Double = 0.0, elevation_ft: Int = 0,
                continent: String = "", iso_country: String = "",iso_region: String = "",municipality: String = "", scheduled_service: String = "",
                gps_code: String = "", iata_code: String = "", local_code: String = "", home_link: String = "", wikipedia_link: String = "", keywords: String = ""): Airport =
    Airport(id, ident, type, name, latitude_deg, longitude_deg, elevation_ft, continent, iso_country, iso_region, municipality, scheduled_service, gps_code, iata_code, local_code, home_link, wikipedia_link, keywords)