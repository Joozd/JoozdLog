package nl.joozd.logbooktest1.utils

import nl.joozd.logbooktest1.data.Airport

fun makeAirport(id: Int = 0, ident: String = "", type: String = "", name: String = "", latitude_deg: Double = 0.0, longitude_deg: Double = 0.0, elevation_ft: Int = 0,
                continent: String = "", iso_country: String = "",iso_region: String = "",municipality: String = "", scheduled_service: String = "",
                gps_code: String = "", iata_code: String = "", local_code: String = "", home_link: String = "", wikipedia_link: String = "", keywords: String = ""): Airport =
    Airport(id, ident, type, name, latitude_deg, longitude_deg, elevation_ft, continent, iso_country, iso_region, municipality, scheduled_service, gps_code, iata_code, local_code, home_link, wikipedia_link, keywords)