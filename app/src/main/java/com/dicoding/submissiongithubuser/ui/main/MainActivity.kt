package com.dicoding.submissiongithubuser.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dicoding.submissiongithubuser.R
import com.dicoding.submissiongithubuser.data.response.ItemsItem
import com.dicoding.submissiongithubuser.databinding.ActivityMainBinding
import com.dicoding.submissiongithubuser.databinding.ItemUserBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    companion object {
        private const val TAG = "MainViewModel"
        private const val USERNAME = "Arif"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        supportActionBar?.hide()

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener{ textView, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    val newValue = searchView.text.toString()
                    searchBar.setText(newValue)
                    viewModel.setSearchUser(newValue)
                    searchView.hide()
                    Toast.makeText(this@MainActivity, newValue, Toast.LENGTH_SHORT).show()
                    true
                } else {
                    false
                }
            }

            if(viewModel.listUser.value!=null) viewModel.setSearchUser("fahaz")
            viewModel.listUser.observe(this@MainActivity){ user ->
                if (user != null){
                    setUserData(user)
                    showLoading(false)
                }
            }

            /*
            viewModel.getSearchUser().observe(this@MainActivity, {
                if (it != null){
                    adapter.currentList
                    showLoading(false)
                }
            })
             */
        }

        viewModel.listUser.observe(this){ user ->
            setUserData(user)
        }
    }


    private fun setUserData(userData: ArrayList<ItemsItem>){
        adapter.submitList(userData)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}