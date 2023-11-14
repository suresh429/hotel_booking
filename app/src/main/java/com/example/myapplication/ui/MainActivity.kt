package com.example.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.HomeDataResponse
import com.example.myapplication.model.MenuModel
import com.example.myapplication.ui.adapter.HomeAdapter
import com.example.myapplication.ui.adapter.MenuListAdapter
import com.example.myapplication.utils.UserPreferences
import com.example.myapplication.utils.startClearActivity
import com.example.myapplication.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.LinkedList
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var binding: ActivityMainBinding
    private var achievementsArray: LinkedList<HomeDataResponse> = LinkedList<HomeDataResponse>()
    private var adapter: HomeAdapter? = null

    @Inject
    lateinit var menuListAdapter: MenuListAdapter
    private val menuList = ArrayList<MenuModel>()

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


        menuList.add(
            MenuModel(
                resources.getString(R.string.terms_amp_conditions),
                R.drawable.ic_terms_and_conditions
            )
        )
        menuList.add(MenuModel(resources.getString(R.string.policies), R.drawable.ic_action_policy))
        menuList.add(
            MenuModel(
                resources.getString(R.string.about_us),
                R.drawable.ic_action_about
            )
        )
        menuList.add(
            MenuModel(
                resources.getString(R.string.logout),
                R.drawable.ic_logout
            )
        )

        menuListAdapter.submitList(menuList)
        binding.menuRecycler.adapter = menuListAdapter
        menuListAdapter.onItemClick = { it, _ ->

            // logout
            when (it.itemName) {
                resources.getString(R.string.logout) -> {
                    logout()
                }
                resources.getString(R.string.terms_amp_conditions) -> {
                    val intent = Intent(this, WebActivity::class.java)
                    //Create the bundle
                    val bundle = Bundle()
                    bundle.putString("title", resources.getString(R.string.terms_amp_conditions))
                    //bundle.putString("endPoint", BuildConfig.CHAT_BOAT_BASE_URL)
                    bundle.putString("endPoint", "file:///android_asset/Terms_Condotions.html")
                    intent.putExtras(bundle)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
                resources.getString(R.string.policies) -> {
                    val intent = Intent(this, WebActivity::class.java)
                    //Create the bundle
                    val bundle = Bundle()
                    bundle.putString("title", resources.getString(R.string.policies))
                    //bundle.putString("endPoint", BuildConfig.CHAT_BOAT_BASE_URL)
                    bundle.putString("endPoint", "file:///android_asset/Privacy-Policy.html")
                    intent.putExtras(bundle)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
                resources.getString(R.string.about_us) -> {
                    val intent = Intent(this, WebActivity::class.java)
                    //Create the bundle
                    val bundle = Bundle()
                    bundle.putString("title", resources.getString(R.string.about_us))
                    //bundle.putString("endPoint", BuildConfig.CHAT_BOAT_BASE_URL)
                    bundle.putString("endPoint", "file:///android_asset/about-us.html")
                    intent.putExtras(bundle)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }

            }

        }

    }

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("LOGOUT")
        builder.setMessage("Are you sure want to Logout ?")
        builder.setPositiveButton("Yes") { dialog, which ->
            lifecycleScope.launch {
                dialog.dismiss()
                userPreferences.clear()
                toast("Logout Successfully")
                startClearActivity(LoginActivity::class.java)

            }
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }


        builder.show()
    }

}