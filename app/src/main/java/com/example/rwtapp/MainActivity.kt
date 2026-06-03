package com.example.rwtapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rwtapp.ui.NewsAdapter
import com.example.rwtapp.ui.NewsViewModel
import com.example.rwtapp.ui.UiState

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var etSearchQuery: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)
        etSearchQuery = findViewById(R.id.etSearchQuery)
        val btnSearch = findViewById<Button>(R.id.btnSearch)

        adapter = NewsAdapter { article ->
            val url = article.url.orEmpty().trim()
            if (url.isNotEmpty()) {
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_URL, url)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Article URL is not available", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        viewModel.newsState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    errorTextView.visibility = View.GONE
                }
                is UiState.Success -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    errorTextView.visibility = View.GONE
                    adapter.setArticles(state.articles)
                }
                is UiState.Error -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    errorTextView.visibility = View.VISIBLE
                    errorTextView.text = state.message
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        btnSearch.setOnClickListener {
            val query = etSearchQuery.text.toString().trim()
            val queryParam = if (query.isEmpty()) null else query
            viewModel.fetchNews(query = queryParam)
        }

        viewModel.fetchNews(country = "us", query = null)
    }
}