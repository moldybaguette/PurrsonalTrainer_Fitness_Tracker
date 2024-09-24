package za.co.varsitycollege.st10204902.purrsonaltrainer.services

import retrofit2.Call
import retrofit2.http.GET
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.CatImage

interface CatsApiService {
    @GET("v1/images/search")
    fun getRandomCatImage(): Call<List<CatImage>>
}