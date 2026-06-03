package com.example.rwtapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.rwtapp.R
import com.example.rwtapp.data.model.Article

class NewsAdapter(
    private val onArticleClick: (Article) -> Unit
) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private val articles = mutableListOf<Article>()

    fun setArticles(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view, onArticleClick)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    class ArticleViewHolder(
        itemView: View,
        private val onArticleClick: (Article) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.articleImage)
        private val titleTextView: TextView = itemView.findViewById(R.id.articleTitle)
        private val descTextView: TextView = itemView.findViewById(R.id.articleDescription)

        fun bind(article: Article) {
            // Safe null/empty checks
            val title = article.title.orEmpty().trim()
            val description = article.description.orEmpty().trim()
            val imageUrl = article.urlToImage.orEmpty().trim()

            // Handle title
            titleTextView.text = if (title.isEmpty()) "No Title Available" else title

            // Handle description
            if (description.isEmpty()) {
                descTextView.visibility = View.GONE
            } else {
                descTextView.visibility = View.VISIBLE
                descTextView.text = description
            }

            // Load image using Glide with placeholder and rounded corners transformation
            Glide.with(itemView.context)
                .load(imageUrl)
                .transform(CenterCrop(), RoundedCorners(16))
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(imageView)

            itemView.setOnClickListener {
                onArticleClick(article)
            }
        }
    }
}
