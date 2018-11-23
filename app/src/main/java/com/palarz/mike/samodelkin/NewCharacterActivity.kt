package com.palarz.mike.samodelkin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_new_character.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

private const val CHARACTER_DATA_KEY = "CHARACTER_DATA_KEY"

private var Bundle.characterData
    get() = getSerializable(CHARACTER_DATA_KEY) as CharacterGenerator.CharacterData
    set(value) = putSerializable(CHARACTER_DATA_KEY, value)

class NewCharacterActivity : AppCompatActivity(), CoroutineScope {

    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var characterData = CharacterGenerator.generate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_character)
        job = Job()

        characterData = savedInstanceState?.characterData ?: CharacterGenerator.generate()

        displayCharacterData()

        generateButton.setOnClickListener {
            launch {
                characterData = fetchCharacterData()
                displayCharacterData()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.characterData = characterData
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun displayCharacterData() {
        characterData.run {
            nameTextView.text = name
            raceTextView.text = race
            dexterityTextView.text = dex
            wisdomTextView.text = wis
            strengthTextView.text = str
        }
    }
}
