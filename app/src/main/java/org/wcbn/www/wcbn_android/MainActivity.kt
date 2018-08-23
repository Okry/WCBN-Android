package org.wcbn.www.wcbn_android


import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Looper
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jsoup.Jsoup


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val streamURL = "http://floyd.wcbn.org:8000/wcbn-hd.mp3"
    private val semestersURL = "https://app.wcbn.org/semesters/10"
    private val semestersJson = "$semestersURL.json"
    private var playing: Boolean = false
    private var live: Boolean = true
    private var mediaPlayer: MediaPlayer = MediaPlayer()


    // Initialization on app start
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        displayFragment(-1)
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    // Fragment management
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
    // End of fragment management


    // Button management
    // This button plays and pauses the music from the stream, initializes mediaPlayer on app start as well
    fun buttonPlayPause(@Suppress("UNUSED_PARAMETER") view: View) {
        if (!playing) {
            playing = true
            if (live) {
                mediaPlayer.setDataSource(streamURL)
                mediaPlayer.prepare()
            }
            mediaPlayer.start()
        }
        else {
            playing = false
            live = false
            mediaPlayer.pause()
            findViewById<RadioGroup>(R.id.liveGroup).clearCheck()
        }
    }

    // This button resets the WCBN stream if it is not live and resumes the music
    fun buttonLive(@Suppress("UNUSED_PARAMETER") view: View) {
        if (!live) {
            live = true
            Toast.makeText(this, "live clicky", Toast.LENGTH_SHORT).show()
            mediaPlayer.reset()
            if (!playing) {
                findViewById<Button>(R.id.playerButton).performClick()
            }
            else {
                mediaPlayer.setDataSource(streamURL)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }
    }

    // Experimental Button!
    // This button will stop stream, pause music, and set to live
    fun buttonStop(@Suppress("UNUSED_PARAMETER") view: View) {
        if (playing) {
            findViewById<Button>(R.id.playerButton).performClick()
        }
        mediaPlayer.stop()
        live = true
        findViewById<RadioButton>(R.id.liveButton).isChecked
    }


    // You can call these using doAsync { ... }
    // This is a test function for future HTML parsing
    private fun testParseHTML() {
        //1. Fetching the HTML from a given URL
        Jsoup.connect(semestersURL).get().run {
            //2. Parses and scrapes the HTML response
            select("div.rc").forEachIndexed { index, element ->
                val titleAnchor = element.select("h3 a")
                val title = titleAnchor.text()
                val url = titleAnchor.attr("href")
                //3. Dumping Search Index, Title and URL on the stdout.
                //println("$index. $title ($url)")
                Looper.prepare()
                toast(url)
                Looper.loop()
            }
        }
    }
}

