package org.wcbn.www.wcbn_android


import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jsoup.Jsoup



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var playing: Boolean = false
    private var live: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //setupPermissions()


            val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        displayFragment(-1)
    }



    //Begin Permissions

    //End Permissions




    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    private fun displayFragment(id: Int) {
        val fragment = when (id) {
            R.id.nav_home -> {
                HomeFragment()
            }
            R.id.nav_history -> {
                HistoryFragment()
            }
            R.id.nav_schedule -> {
                ScheduleFragment()
            }
            else -> {
                HomeFragment()
            }
        }
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.relativeLayout, fragment)
                .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        displayFragment(item.itemId)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }




    //Buttons!
    fun buttonPlayPause(@Suppress("UNUSED_PARAMETER") view: View) {
        if (!playing) {
            playing = true
            Toast.makeText(this, "play clicky", Toast.LENGTH_SHORT).show()
            /*
            var doc = Jsoup.connect("http://app.wcbn.org/").get().run {
                select("div.rc").forEachIndexed { index, element ->
                    val titleAnchor = element.select("h3 a")
                    val title = titleAnchor.text()
                    val url = titleAnchor.attr("href")

                    println("$index. $title ($url)")
                }
            }
            */

        }
        else {
            playing = false
            live = false
            Toast.makeText(this, "pause clicky", Toast.LENGTH_SHORT).show()
            findViewById<RadioGroup>(R.id.liveGroup).clearCheck()

        }
    }


    fun buttonLive(@Suppress("UNUSED_PARAMETER") view: View) {
        if (!live) {
            live = true
            Toast.makeText(this, "live clicky", Toast.LENGTH_SHORT).show()
        }
    }
    //End of Buttons!
}

