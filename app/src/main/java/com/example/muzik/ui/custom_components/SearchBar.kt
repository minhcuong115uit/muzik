package com.example.muzik.ui.custom_components

import android.app.appsearch.SearchResult
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.data.models.Song
import com.example.muzik.ui.adapters.SearchResultAdapter
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel

class SearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    // Declare views
    private val editText: EditText
    private val searchIcon: ImageView
    private val recyclerView: RecyclerView
    private val resultContainer: ConstraintLayout
    private var playerViewModel: PlayerViewModel? = null

    // Declare the adapter for the RecyclerView
    private lateinit var adapter: SearchResultAdapter

    fun setPlayerViewModel(vm: PlayerViewModel) {
        playerViewModel = vm
        adapter = SearchResultAdapter(context, playerViewModel, mutableListOf())
        // Set up the RecyclerView
        recyclerView.adapter = adapter
    }

    init {
        // Inflate the layout
        LayoutInflater.from(context).inflate(R.layout.search_bar, this, true)

        // Find views
        editText = findViewById(R.id.editText)
        searchIcon = findViewById(R.id.searchIcon)
        recyclerView = findViewById(R.id.recyclerView)
        resultContainer = findViewById(R.id.result_container)
        // Add a TextWatcher to the EditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Get the current text entered by the user
                val searchText = s.toString()

                if (searchText.isNotEmpty()) {
                    // Perform the search and update the search results
                    val searchResults = performSearch(searchText)
                    Log.d("Search", "result ${searchResults.size}")
                    adapter.updateList(searchResults)

                    // Show the RecyclerView
                    resultContainer.visibility = View.VISIBLE
                } else {
                    // Clear the search results and hide the RecyclerView
                    adapter.updateList(emptyList())
                    resultContainer.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Add any additional methods or properties here

    private fun performSearch(searchText: String): List<Song> {
        val allSongs = playerViewModel?.getListSong()?.filter { song -> !song.isLocalSong }
        if (allSongs != null) {
            return allSongs.filter { song ->
                song.name.contains(searchText, ignoreCase = true)
            }
        }
        return mutableListOf()
    }
}
