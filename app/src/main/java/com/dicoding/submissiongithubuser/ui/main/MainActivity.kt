package com.dicoding.submissiongithubuser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissiongithubuser.R
import com.dicoding.submissiongithubuser.data.local.SettingPreferences
import com.dicoding.submissiongithubuser.data.response.ItemsItem
import com.dicoding.submissiongithubuser.databinding.ActivityMainBinding
import com.dicoding.submissiongithubuser.ui.detail.DetailUserActivity
import com.dicoding.submissiongithubuser.ui.favorite.FavoriteActivity
import com.dicoding.submissiongithubuser.ui.setting.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels<MainViewModel>{
        MainViewModel.Factory(SettingPreferences(this))
    }
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getThemeSetting().observe(this){
            if (it){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        supportActionBar?.hide()

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        viewModel.isLoading.observe(this@MainActivity){
            showLoading(it)
        }

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            searchBar.inflateMenu(R.menu.option_menu)
            binding.searchBar.setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.menu_favorite -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.menu_setting -> {
                        val intent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
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

            showLoading(true)
            if(viewModel.listUser.value==null) viewModel.setSearchUser(USERNAME)
            viewModel.listUser.observe(this@MainActivity){ user ->
                if (user != null){
                    setUserData(user)
                    showLoading(false)
                }
            }
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
    companion object {
        private const val USERNAME = "Arif"
    }
}