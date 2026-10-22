package com.socialhub.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var root: LinearLayout
    private lateinit var home: LinearLayout
    private lateinit var webContainer: LinearLayout
    private lateinit var webView: WebView
    private lateinit var progress: ProgressBar
    private lateinit var status: TextView
    private lateinit var header: TextView
    private var active = 0
    private var showingHome = true

    private data class Platform(val name: String, val url: String, val symbol: String, val color: Int, val subtitle: String)
    private val platforms = listOf(
        Platform("Facebook", "https://m.facebook.com/", "f", Color.rgb(24, 119, 242), "Posts · Videos · Messages"),
        Platform("Instagram", "https://www.instagram.com/", "◎", Color.rgb(193, 53, 132), "Reels · Photos · Messages"),
        Platform("TikTok", "https://www.tiktok.com/", "♪", Color.rgb(20, 20, 20), "Videos · Comments"),
        Platform("YouTube", "https://m.youtube.com/", "▶", Color.rgb(255, 0, 0), "Videos · Shorts"),
        Platform("X", "https://x.com/", "𝕏", Color.rgb(0, 0, 0), "Posts · Replies · Messages")
    )

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(15, 23, 42)
        window.navigationBarColor = Color.rgb(15, 23, 42)

        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(248, 250, 252))
        }
        header = TextView(this).apply {
            text = "Social Hub"
            textSize = 24f
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(15, 23, 42))
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(20), dp(12), dp(20), dp(12))
        }
        root.addView(header, LinearLayout.LayoutParams(-1, dp(62)))

        home = buildHome()
        root.addView(home, LinearLayout.LayoutParams(-1, 0, 1f))

        webContainer = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; visibility = View.GONE }
        progress = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply { visibility = View.GONE; max = 100 }
        webContainer.addView(progress, LinearLayout.LayoutParams(-1, dp(3)))
        status = TextView(this).apply {
            text = "Internet ya website ka masla ho sakta hai. Refresh karein."
            textSize = 13f
            setTextColor(Color.DKGRAY)
            setPadding(dp(12), dp(6), dp(12), dp(6))
            visibility = View.GONE
        }
        webContainer.addView(status)
        webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = false
        webView.settings.setSupportMultipleWindows(false)
        webView.settings.mediaPlaybackRequiresUserGesture = true
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress.progress = newProgress
                progress.visibility = if (newProgress in 1..99) View.VISIBLE else View.GONE
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val uri = request?.url ?: return false
                val scheme = uri.scheme ?: return true
                if (scheme != "https" && scheme != "http") {
                    return try { startActivity(Intent(Intent.ACTION_VIEW, uri)); true }
                    catch (_: Exception) { Toast.makeText(this@MainActivity, "Yeh link open nahin ho saka", Toast.LENGTH_SHORT).show(); true }
                }
                return false
            }
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: android.webkit.WebResourceError?) {
                if (request?.isForMainFrame == true) status.visibility = View.VISIBLE
            }
            override fun onPageFinished(view: WebView?, url: String?) { status.visibility = View.GONE }
        }
        webContainer.addView(webView, LinearLayout.LayoutParams(-1, 0, 1f))
        root.addView(webContainer, LinearLayout.LayoutParams(-1, 0, 1f))
        root.addView(buildBottomBar(), LinearLayout.LayoutParams(-1, dp(58)))
        setContentView(root)
        showHome()
    }

    private fun buildHome(): LinearLayout {
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(22), dp(18), dp(18))
            setBackgroundColor(Color.rgb(248, 250, 252))
        }
        val title = TextView(this).apply {
            text = "Choose your platform"
            textSize = 20f
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            setTextColor(Color.rgb(15, 23, 42))
        }
        page.addView(title)
        val hint = TextView(this).apply {
            text = "Your social world, all in one place"
            textSize = 14f
            setTextColor(Color.rgb(100, 116, 139))
            setPadding(0, dp(5), 0, dp(20))
        }
        page.addView(hint)

        val grid = GridLayout(this).apply { columnCount = 2; useDefaultMargins = false }
        platforms.forEachIndexed { index, platform ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(8), dp(18), dp(8), dp(16))
                background = rounded(Color.WHITE, dp(18), Color.rgb(226, 232, 240), dp(1))
                elevation = dp(2).toFloat()
                isClickable = true
                isFocusable = true
                setOnClickListener { openPlatform(index) }
            }
            val icon = TextView(this).apply {
                text = platform.symbol
                textSize = if (platform.name == "Instagram") 36f else 32f
                typeface = Typeface.create("sans-serif", Typeface.BOLD)
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                background = rounded(platform.color, dp(20))
            }
            card.addView(icon, LinearLayout.LayoutParams(dp(66), dp(66)))
            val name = TextView(this).apply {
                text = platform.name
                textSize = 16f
                typeface = Typeface.create("sans-serif", Typeface.BOLD)
                setTextColor(Color.rgb(15, 23, 42))
                gravity = Gravity.CENTER
                setPadding(0, dp(10), 0, dp(3))
            }
            card.addView(name)
            val sub = TextView(this).apply {
                text = platform.subtitle
                textSize = 10f
                setTextColor(Color.rgb(100, 116, 139))
                gravity = Gravity.CENTER
            }
            card.addView(sub)
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = dp(160)
                columnSpec = GridLayout.spec(index % 2, 1f)
                setMargins(dp(5), dp(5), dp(5), dp(7))
            }
            grid.addView(card, params)
        }
        page.addView(grid)
        val note = TextView(this).apply {
            text = "Note: Platform ke kuch login, video ya messaging features WebView mein limited ho sakte hain."
            textSize = 12f
            setTextColor(Color.rgb(100, 116, 139))
            setPadding(dp(3), dp(16), dp(3), 0)
        }
        page.addView(note)
        return page
    }

    private fun buildBottomBar(): LinearLayout {
        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.WHITE)
            elevation = dp(8).toFloat()
        }
        val homeButton = Button(this).apply {
            text = "⌂  Home"
            isAllCaps = false
            setOnClickListener { showHome() }
        }
        val backButton = Button(this).apply {
            text = "← Back"
            isAllCaps = false
            setOnClickListener { if (!showingHome && webView.canGoBack()) webView.goBack() else showHome() }
        }
        val refreshButton = Button(this).apply {
            text = "↻ Refresh"
            isAllCaps = false
            setOnClickListener { if (!showingHome) webView.reload() }
        }
        listOf(homeButton, backButton, refreshButton).forEach { bar.addView(it, LinearLayout.LayoutParams(0, dp(52), 1f)) }
        return bar
    }

    private fun openPlatform(index: Int) {
        active = index.coerceIn(platforms.indices)
        showingHome = false
        home.visibility = View.GONE
        webContainer.visibility = View.VISIBLE
        header.text = "Social Hub  ·  ${platforms[active].name}"
        status.visibility = View.GONE
        webView.loadUrl(platforms[active].url)
    }

    private fun showHome() {
        showingHome = true
        home.visibility = View.VISIBLE
        webContainer.visibility = View.GONE
        header.text = "Social Hub"
    }

    private fun rounded(color: Int, radius: Int, strokeColor: Int? = null, strokeWidth: Int = 0): GradientDrawable = GradientDrawable().apply {
        setColor(color)
        cornerRadius = radius.toFloat()
        if (strokeColor != null && strokeWidth > 0) setStroke(strokeWidth, strokeColor)
    }

    override fun onBackPressed() {
        if (!showingHome && webView.canGoBack()) webView.goBack() else if (!showingHome) showHome() else super.onBackPressed()
    }

    override fun onDestroy() {
        if (::webView.isInitialized) {
            (webView.parent as? ViewGroup)?.removeView(webView)
            webView.stopLoading()
            webView.destroy()
        }
        super.onDestroy()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
