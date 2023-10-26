package com.example.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.model.HomeDataResponse
import com.example.myapplication.ui.adapter.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.LinkedList

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var achievementsArray: LinkedList<HomeDataResponse> = LinkedList<HomeDataResponse>()
    private var adapter: HomeAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        achievementsArray.add(HomeDataResponse("Flights",R.drawable.airplane,"https://avoota.com/index.html"))
        achievementsArray.add(HomeDataResponse("Hotels",R.drawable.hotel,"https://avoota.com/hotel.html"))
        achievementsArray.add(HomeDataResponse("Bus",R.drawable.bus,"https://avoota.com/bus.html"))
        achievementsArray.add(HomeDataResponse("Holidays",R.drawable.holiday,"https://holidays.avoota.com/"))
        adapter = HomeAdapter(achievementsArray)
        binding.recycleView.adapter = adapter
        adapter?.onItemClick = { _, data ->
            val intent = Intent(this, WebActivity::class.java)
            //Create the bundle
            val bundle = Bundle()
            bundle.putString("title", data.name)
            //bundle.putString("endPoint", BuildConfig.CHAT_BOAT_BASE_URL)
            bundle.putString("endPoint", data.url)
            intent.putExtras(bundle)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }
}