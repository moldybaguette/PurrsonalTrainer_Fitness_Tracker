package za.co.varsitycollege.st10204902.purrsonaltrainer.services

import retrofit2.Call
import retrofit2.http.GET
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.CatFact
/**
 * Interface for the Cat Facts API service
 */
interface CatFactsApi {
    @GET("fact")
    fun getCatFact(): Call<CatFact>
}