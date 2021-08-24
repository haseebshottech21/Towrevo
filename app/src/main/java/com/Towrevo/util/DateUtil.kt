package com.Towrevo

import com.Towrevo.util.DATE_FORMAT_20
import com.Towrevo.util.DATE_FORMAT_3
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Util class for Date manipulations
 */
object DateUtil {

    fun getCurrentDate(DATE_FORMAT: String): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val today = Calendar.getInstance().time
        return dateFormat.format(today)
    }

    fun getDate(DATE_FORMAT: String, year: Int, month: Int, day: Int): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_20)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        val chosenDate = cal.time
        return dateFormat.format(chosenDate)
    }

    fun getDate1(DATE_FORMAT: String, year: Int, month: Int, day: Int): Date {
        val dateFormat = SimpleDateFormat(DATE_FORMAT)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        return cal.time
    }

    fun getDateAPI(year: Int, month: Int, day: Int): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_3)
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        return dateFormat.format(cal.time)
    }

    fun getViewDate(date: String): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_3)
        val dateFormat1 = SimpleDateFormat(DATE_FORMAT_20)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateFormat1.timeZone = TimeZone.getTimeZone("UTC")
        val chosenDate = dateFormat.parse(date)
        return dateFormat1.format(chosenDate)
    }

    fun compareToCurrentDate(date:String) : Boolean
    {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val d: Date?
        val d1: Date?
        val dateToday=Date()
        val formatDate =SimpleDateFormat("yyyy-MM-dd").format(dateToday)
        d = format.parse(date)
        d1 = format.parse(formatDate)
        if(d1 < d){// not expired
            return false
        }else if(d.compareTo(d1)==0){// both date are same
            if(d.time < d1.time){// not expired
                return false
            }else if(d.time == d1.time){//expired
                return true
            }else{//expired
                return true
            }
        }else{//expired
            return true
        }
    }

    fun StringToDate(date:String) : Date
    {
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.parse(date)

    }

    fun getDateInLocal(date: String): Calendar {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("UTC");
        var d: Date? = null;
        try {
            d = formatter.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace();
        }

        val fmtOut = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmtOut.timeZone = TimeZone.getDefault();
        val localDate = fmtOut.format(d);
        val cal = Calendar.getInstance()
        cal.time = fmtOut.parse(localDate)
        return cal
    }

}
