package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val xUser = "X"
    private val oUser = "O"

    private var slotsStatuses: HashMap<Int, String>? = null
    private var slotsImageButtons: Array<ImageButton>? = null
    private var winPositions: Map<Int, Array<Pair<Int, Int>>>? = null
    private var currentPlayerTurn: String? = null
    private var isGameEnded: Boolean? = null
    private var playAgainButton: Button? = null
    private var messageTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onInit()

        slotsImageButtons?.forEach { slot ->
            slot.setOnClickListener (::onSlotImageButtonClick)
        }

        playAgainButton?.setOnClickListener(::onPlayAgainButtonClick)
    }

    private fun onInit() {
        slotsImageButtons = arrayOf(
            findViewById(R.id.ibMainSlot1),
            findViewById(R.id.ibMainSlot2),
            findViewById(R.id.ibMainSlot3),
            findViewById(R.id.ibMainSlot4),
            findViewById(R.id.ibMainSlot5),
            findViewById(R.id.ibMainSlot6),
            findViewById(R.id.ibMainSlot7),
            findViewById(R.id.ibMainSlot8),
            findViewById(R.id.ibMainSlot9),
        )

        winPositions = mapOf(
            R.id.ibMainSlot1 to arrayOf(
                Pair(R.id.ibMainSlot2, R.id.ibMainSlot3),
                Pair(R.id.ibMainSlot4, R.id.ibMainSlot7),
                Pair(R.id.ibMainSlot5, R.id.ibMainSlot9),
            ),
            R.id.ibMainSlot2 to arrayOf(
                Pair(R.id.ibMainSlot1, R.id.ibMainSlot3),
                Pair(R.id.ibMainSlot5, R.id.ibMainSlot8),
            ),
            R.id.ibMainSlot3 to arrayOf(
                Pair(R.id.ibMainSlot1, R.id.ibMainSlot2),
                Pair(R.id.ibMainSlot5, R.id.ibMainSlot7),
                Pair(R.id.ibMainSlot6, R.id.ibMainSlot9),
            ),
            R.id.ibMainSlot4 to arrayOf(
                Pair(R.id.ibMainSlot1, R.id.ibMainSlot7),
                Pair(R.id.ibMainSlot5, R.id.ibMainSlot6),
            ),
            R.id.ibMainSlot5 to arrayOf(
                Pair(R.id.ibMainSlot1, R.id.ibMainSlot9),
                Pair(R.id.ibMainSlot2, R.id.ibMainSlot8),
                Pair(R.id.ibMainSlot3, R.id.ibMainSlot7),
                Pair(R.id.ibMainSlot4, R.id.ibMainSlot6),
            ),
            R.id.ibMainSlot6 to arrayOf(
                Pair(R.id.ibMainSlot3, R.id.ibMainSlot9),
                Pair(R.id.ibMainSlot4, R.id.ibMainSlot5),
            ),
            R.id.ibMainSlot7 to arrayOf(
                Pair(R.id.ibMainSlot1, R.id.ibMainSlot4),
                Pair(R.id.ibMainSlot3, R.id.ibMainSlot5),
                Pair(R.id.ibMainSlot8, R.id.ibMainSlot9),
            ),
            R.id.ibMainSlot8 to arrayOf(
                Pair(R.id.ibMainSlot2, R.id.ibMainSlot5),
                Pair(R.id.ibMainSlot7, R.id.ibMainSlot9),
            ),
            R.id.ibMainSlot9 to arrayOf(
                Pair(R.id.ibMainSlot1, R.id.ibMainSlot5),
                Pair(R.id.ibMainSlot3, R.id.ibMainSlot6),
                Pair(R.id.ibMainSlot7, R.id.ibMainSlot8),
            )
        )

        playAgainButton = findViewById(R.id.btnMainPlayAgain)
        messageTextView = findViewById(R.id.tvMainMessage)

        resetGame()
    }

    private fun resetGame() {
        slotsStatuses = HashMap()
        currentPlayerTurn = xUser
        isGameEnded = false
        messageTextView?.text = getString(R.string.x_turn_message)
        slotsImageButtons?.forEach { slot ->
            slot.setImageResource(0)
        }
    }

    fun onPlayAgainButtonClick(view: View) {
        resetGame()
        view.visibility = View.GONE
    }

    fun onSlotImageButtonClick(view: View) {
        val slotImageButton = view as ImageButton
        val slotId = view.id

        if (isGameEnded == true || slotsStatuses?.containsKey(slotId) == true) {
            return
        }

        currentPlayerTurnHandler(slotImageButton)

        if (checkWin(slotId)) {
            val winMessage = if (currentPlayerTurn == xUser) getString(R.string.x_win_message) else getString(R.string.o_win_message)
            endGame(winMessage)
            return
        }

        if(slotsStatuses?.size == slotsImageButtons?.size) {
            endGame(getString(R.string.draw_message))
            return
        }

        nextPlayerTurnHandler()
    }

    private fun currentPlayerTurnHandler(slotImageButton: ImageButton) {
        val slotId = slotImageButton.id

        slotsStatuses?.set(slotId, currentPlayerTurn!!)

        if (currentPlayerTurn == xUser) {
            slotImageButton.setImageResource(R.drawable.x)
            slotsStatuses?.set(slotId, xUser)
        } else {
            slotImageButton.setImageResource(R.drawable.o)
            slotsStatuses?.set(slotId, oUser)
        }
    }

    private fun nextPlayerTurnHandler() {
        if (currentPlayerTurn == xUser) {
            messageTextView?.text = getString(R.string.o_turn_message)
            currentPlayerTurn = oUser
        } else {
            messageTextView?.text = getString(R.string.x_turn_message)
            currentPlayerTurn = xUser
        }
    }

    private fun endGame(message: String) {
        messageTextView?.text = message
        isGameEnded = true
        playAgainButton?.visibility = View.VISIBLE
    }

    private fun checkWin(slotId: Int): Boolean {
        val isWin = winPositions?.get(slotId)?.any { currentPlayerTurn == slotsStatuses?.get(it.first) &&
                                                     currentPlayerTurn == slotsStatuses?.get(it.second)
        }

        return isWin ?: false
    }
}