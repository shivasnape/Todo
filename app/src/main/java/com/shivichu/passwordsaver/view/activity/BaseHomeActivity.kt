package com.shivichu.passwordsaver.view.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.shivichu.passwordsaver.R
import com.shivichu.passwordsaver.database.entity.Todo
import com.shivichu.passwordsaver.databinding.ActivityBaseHomeBinding
import com.shivichu.passwordsaver.receiver.AlarmReceiver
import com.shivichu.passwordsaver.utils.SessionManager
import com.shivichu.passwordsaver.utils.TypefaceSpan
import com.shivichu.passwordsaver.view.fragment.AddNewTodoFragment
import com.shivichu.passwordsaver.view.fragment.HomeTodoListFragment
import com.shivichu.passwordsaver.view.fragment.TodoLoginFragment
import com.shivichu.passwordsaver.view.fragment.UpdateTodoFragment
import java.text.SimpleDateFormat
import java.util.*


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
                else if(fragment is UpdateTodoFragment){
                    navController?.navigate(R.id.homeTodoListFragment)
                }
                else {
                    // empty
                }
            }
        }
    }

    fun startAlarmBroadcastReceiver(context: Context, todoItem: Todo) {

        Log.e("Tag", Gson().toJson(todoItem))
        val timeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val todoTime = todoItem.date.plus(" ").plus(todoItem.time)
        Log.e("Tag",todoTime.toString())
        val formatted = timeFormat.parse(todoTime)

        val cal = Calendar.getInstance()
        cal.time = formatted

        Log.e("Tag",formatted.toString())

        Log.e("Tag",cal.get(Calendar.HOUR_OF_DAY).toString())
        Log.e("Tag",cal.get(Calendar.MINUTE).toString())


        val _intent = Intent(context, AlarmReceiver::class.java)
        _intent.putExtra("todo",Gson().toJson(todoItem))
        val pendingIntent = PendingIntent.getBroadcast(context, 0, _intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        /* calendar[Calendar.HOUR_OF_DAY] = 8
         calendar[Calendar.MINUTE] = 20
         calendar[Calendar.SECOND] = 0*/

        calendar[Calendar.HOUR_OF_DAY] = cal.get(Calendar.HOUR_OF_DAY)
        calendar[Calendar.MINUTE] = cal.get(Calendar.MINUTE)
        calendar[Calendar.SECOND] = 0

        //alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
        if(todoItem.type == 1) {
            //daily
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
        else {
            //weekly
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
            )
        }
    }

}