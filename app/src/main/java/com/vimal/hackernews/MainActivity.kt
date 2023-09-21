package com.vimal.hackernews

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.vimal.hackernews.adapter.CharacterAdapter
import com.vimal.hackernews.databinding.ActivityMainBinding
import com.vimal.hackernews.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()
    lateinit var adapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CharacterAdapter()
        val layoutManager = GridLayoutManager(this, 1)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter

        binding.progress.visibility = View.VISIBLE
        binding.recyclerview.visibility = View.GONE

//        lifecycleScope.launch {
//
//            viewModel.getAllIds().collect { response ->
//                if (response.isSuccessful) {
//                    val ids = response.body()
//                    if (ids != null) {
//                        // Handle the list of IDs here
//                        for (id in ids) {
//                            // Call the second API for each ID
//                            viewModel.getItem(id).collect { itemResponse ->
//                                if (itemResponse.isSuccessful) {
//                                    val item = itemResponse.body()
//                                    if (item != null) {
//                                        binding.progress.visibility = View.GONE
//                                        binding.recyclerview.visibility = View.VISIBLE
//                                        adapter.setCharacter(item)
//                                    }
//                                } else {
//                                    // Handle the error for the second API call
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    // Handle the error for the first API call
//                }
//            }
//        }

        viewModel.fetchTopStories()

        viewModel.loading.observe(this, Observer {

        })

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()
        }

        viewModel.characterList.observe(this) {
            binding.progress.visibility = View.GONE
            binding.recyclerview.visibility = View.VISIBLE
            adapter.setCharacter(it)
        }
    }
}