package nl.joozd.logbooktest1.data

data class Airport(val id: Int, val ident: String, val type: String, val name: String, val latitude_deg: Double, val longitude_deg: Double, val elevation_ft: Int,
                   val continent: String, val iso_country: String, val iso_region: String, val municipality: String, val scheduled_service: String,
                   val gps_code: String, val iata_code: String, val local_code: String, val home_link: String, val wikipedia_link: String, val keywords: String)
