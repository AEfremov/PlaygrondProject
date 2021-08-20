package ru.efremov.playgrondproject

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.efremov.playgrondproject.adaptersample.*
import ru.efremov.playgrondproject.databinding.ActivityMainBinding
import ru.efremov.playgrondproject.expandablerw.listener.OnChildDataListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: GenreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animator: RecyclerView.ItemAnimator = binding.categoryListRv.itemAnimator!!
        if (animator is DefaultItemAnimator) {
            (animator as DefaultItemAnimator).supportsChangeAnimations = false
        }
        adapter = GenreAdapter(DataFactory.makeGenres())
        adapter.setOnChildDataListener(object : OnChildDataListener {
            override fun <T> obtainChildData(data: T) {
                val currentData = (data as Artist)
                Log.d("obtainChildData", currentData.toString())
                Toast.makeText(this@MainActivity, currentData.name, Toast.LENGTH_SHORT).show()
            }
        })
        binding.categoryListRv.layoutManager = LinearLayoutManager(this)
        binding.categoryListRv.adapter = adapter
    }
}