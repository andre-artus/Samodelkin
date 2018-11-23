package com.palarz.mike.samodelkin

import kotlinx.coroutines.*
import java.io.Serializable
import java.net.URL

private const val CHARACTER_DATA_ENDPOINT = "https://http://chargen-api.herokuapp.com/"
private fun <T> List<T>.rand() = shuffled().first()

private fun Int.roll() = (0 until this)
        .map { (1..6).toList().rand() }
        .sum()
        .toString()

private val firstName = listOf("Eli", "Alex", "Sophie")
private val lastName = listOf("Lightweaver", "Greatfoot", "Oakenfield")

object CharacterGenerator {
    data class CharacterData(val name: String,
                             val race: String,
                             val dex: String,
                             val wis: String,
                             val str: String) : Serializable

    private fun name() = "${firstName.rand()} ${lastName.rand()}"

    private fun race() = listOf("dwarf", "elf", "human", "halfling").rand()

    private fun dex() = 4.roll()

    private fun wis() = 3.roll()

    private fun str() = 5.roll()

    fun generate() = CharacterData(name = name(),
                                    race = race(),
                                    dex = dex(),
                                    wis = wis(),
                                    str = str())

    fun fromApiData(apiData: String): CharacterData{
        val(race, name, dex, wis, str) = apiData.split(",")
        return CharacterData(name, race, dex, wis, str)
    }
}

fun fetchCharacterData(): Deferred<CharacterGenerator.CharacterData> {
    return GlobalScope.async(Dispatchers.IO) {
        val apiData = URL(CHARACTER_DATA_ENDPOINT).readText()
        CharacterGenerator.fromApiData(apiData)
    }
}

//suspend fun fetchCharacterData(): CharacterGenerator.CharacterData =
//        withContext(Dispatchers.IO) {
//            val apiData = URL(CHARACTER_DATA_ENDPOINT).readText()
//            CharacterGenerator.fromApiData(apiData)
//        }