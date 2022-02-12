package com.shivichu.passwordsaver.view.activity

import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.shivichu.passwordsaver.R
import com.shivichu.passwordsaver.databinding.ActivityBaseHomeBinding
import com.shivichu.passwordsaver.utils.SessionManager
import com.shivichu.passwordsaver.utils.TypefaceSpan
import com.shivichu.passwordsaver.view.fragment.AddNewTodoFragment
import com.shivichu.passwordsaver.view.fragment.HomeTodoListFragment
import com.shivichu.passwordsaver.view.fragment.TodoLoginFragment


class BaseHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseHomeBinding
    var navController: NavController? = null
    private var doubleBackToExitPressedOnce = false
    private var actionBar: Toolbar? = null
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_base_home)

        val window = getWindow()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.p_dark))
        }



        setUpActionBar()

        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))

        navController = Navigation.findNavController(this, R.id.myFragment)

    }

     fun setUpActionBar(text: String = "Home") {
        //change actionbar font
        val s = SpannableString(text)
        s.setSpan(
            TypefaceSpan(this, "AveriaSansLibre-Bold.ttf"),
            0,
            s.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.toolbar!!.setTitle(s)

    }

    override fun onBackPressed() {
        val navHost = supportFragmentManager.primaryNavigationFragment
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.fragments.get(0)?.let { fragment ->
                if (fragment is HomeTodoListFragment) {
                    finish()
                } else if (fragment is AddNewTodoFragment) {
                    navController?.navigate(R.id.homeTodoListFragment)
                }
                else if(fragment is TodoLoginFragment) {
                    finish()
                }
                else {

                }
            }
        }
    }
}