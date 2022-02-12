
import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


class DateTimeUtil {

    companion object {

        var DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
        var READABLE_DATE_TIME = "dd MMM yyyy hh:mm a"

        fun getCurrentDateAndTime(format: String): String {
            val calendar = Calendar.getInstance()
            val mdformat = SimpleDateFormat(
                format,
                Locale.getDefault()
            ).format(Calendar.getInstance().getTime())
            return mdformat.format(calendar.time)
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateTime(format : String, targetFormat : String, date : String) : String {

        if(date == null || date.equals("")) {
            return ""
        }

        val currentFormater = SimpleDateFormat(format)
        val targetFormater = SimpleDateFormat(targetFormat)

        try{
            val temp = currentFormater.parse(date)
            return targetFormater.format(temp)
        }
        catch (e : Exception) {
            e.printStackTrace()
            return date
        }
    }
}