package ae.sinxiao.android.selectimage

import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by silence on 2020-03-08
 * Describe:
 */
object TimeUtils {

    val DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss"


    /**
     * 将时间戳转为时间字符串
     *
     * @param millis 毫秒时间戳
     * @return 时间字符串
     */
    fun millis2String(millis: Long): String {
        return SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault()).format(Date(millis))
    }

    /**
     * 将时间戳转为时间字符串
     *
     * @param millis  毫秒时间戳
     * @param pattern 时间格式
     * @return 时间字符串
     */
    fun millis2String(millis: Long, pattern: String): String {
        var millis = millis
        if (millis.toString().length == 10) {
            // 如果是秒，则转换为毫秒
            millis = millis * 1000
        }
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(millis))
    }


    /**
     * 将时间字符串转为时间戳
     *
     * @param time 时间字符串
     * @return 毫秒时间戳
     */
    fun string2Millis(time: String): Long {
        return string2Millis(time, DEFAULT_PATTERN)
    }

    /**
     * 将时间字符串转为时间戳
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 毫秒时间戳
     */
    fun string2Millis(time: String, pattern: String): Long {
        try {
            return SimpleDateFormat(pattern, Locale.getDefault()).parse(time)!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return -1
    }


    /**
     * 将时间字符串转为Date类型
     *
     * @param time 时间字符串
     * @return Date类型
     */
    fun string2Date(time: String): Date {
        return string2Date(time, DEFAULT_PATTERN)
    }

    /**
     * 时间戳转换成时间字符窜
     *
     * @param format 时间格式
     * @param time   时间戳
     * @return
     */
    fun date2String(format: String, time: Long): String {
        val d = Date(time)
        val sf = SimpleDateFormat(format, Locale.CHINA)
        return sf.format(d)
    }

    /**
     * 将时间字符串转为Date类型
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return Date类型
     */
    fun string2Date(time: String, pattern: String): Date {
        return Date(string2Millis(time, pattern))
    }


    /**
     * 将Date类型转为时间字符串
     *
     * @param date Date类型
     * @return 时间字符串
     */
    fun date2String(date: Date): String {
        return date2String(date, DEFAULT_PATTERN)
    }

    /**
     * 将Date类型转为时间字符串
     *
     * @param date    Date类型
     * @param pattern 时间格式
     * @return 时间字符串
     */
    fun date2String(date: Date, pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
    }

    /**
     * 将Date类型转为时间字符串
     *
     * @param date    Date类型
     * @param pattern 时间格式
     * @return 时间字符串
     */
    fun date2String(date: Long, pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
    }


    /**
     * 将Date类型转为时间戳
     *
     * @param date Date类型
     * @return 毫秒时间戳
     */
    fun date2Millis(date: Date): Long {
        return date.time
    }

    /**
     * 将时间戳转为Date类型
     *
     * @param millis 毫秒时间戳
     * @return Date类型
     */
    fun millis2Date(millis: Long): Date {
        return Date(millis)
    }

    fun dateFormat(time: String, pattern: String): String {
        val date = string2Date(time, pattern)
        return date2String(date, pattern)
    }

    fun dateFormat(time: String, originPattern: String, desPattern: String): String {
        val date = string2Date(time, originPattern)
        return date2String(date, desPattern)
    }

    /**
     * 判断是否为润年
     *
     * @param year 年份
     * @return 是否闰年
     */
    fun isLeapYear(year: Int): Boolean {
        val gc = Calendar.getInstance() as GregorianCalendar
        return gc.isLeapYear(year)
    }

    /**
     * 判断是否闰年
     *
     * @param date Date类型
     * @return 是否闰年
     */
    fun isLeapYear(date: Date): Boolean {
        val cal = Calendar.getInstance()
        cal.time = date
        val year = cal.get(Calendar.YEAR)
        return isLeapYear(year)
    }

    /**
     * 判断是否闰年
     *
     * @param millis 毫秒时间戳
     * @return 是否闰年
     */
    fun isLeapYear(millis: Long): Boolean {
        return isLeapYear(millis2Date(millis))
    }


    private val CHINESE_ZODIAC = arrayOf("猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊")

    /**
     * 获取生肖
     *
     * @param time 时间字符串
     * @return 生肖
     */
    fun getChineseZodiac(time: String): String {
        return getChineseZodiac(string2Date(time, DEFAULT_PATTERN))
    }

    /**
     * 获取生肖
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 生肖
     */
    fun getChineseZodiac(time: String, pattern: String): String {
        return getChineseZodiac(string2Date(time, pattern))
    }

    /**
     * 获取生肖
     *
     * @param date Date类型时间
     * @return 生肖
     */
    fun getChineseZodiac(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        return CHINESE_ZODIAC[cal.get(Calendar.YEAR) % 12]
    }

    /**
     * 获取生肖
     *
     * @param millis 毫秒时间戳
     * @return 生肖
     */
    fun getChineseZodiac(millis: Long): String {
        return getChineseZodiac(millis2Date(millis))
    }

    /**
     * 获取生肖
     *
     * @param year 年
     * @return 生肖
     */
    fun getChineseZodiac(year: Int): String {
        return CHINESE_ZODIAC[year % 12]
    }


    private val ZODIAC =
        arrayOf("水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座")
    private val ZODIAC_FLAGS = intArrayOf(20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22)

    /**
     * 获取星座
     *
     * @param time 时间字符串
     * @return 生肖
     */
    fun getZodiac(time: String): String {
        return getZodiac(string2Date(time, DEFAULT_PATTERN))
    }

    /**
     * 获取星座
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 生肖
     */
    fun getZodiac(time: String, pattern: String): String {
        return getZodiac(string2Date(time, pattern))
    }

    /**
     * 获取星座
     *
     * @param date Date类型时间
     * @return 星座
     */
    fun getZodiac(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return getZodiac(month, day)
    }

    /**
     * 获取星座
     *
     * @param millis 毫秒时间戳
     * @return 星座
     */
    fun getZodiac(millis: Long): String {
        return getZodiac(millis2Date(millis))
    }

    /**
     * 获取星座
     *
     * @param month 月
     * @param day   日
     * @return 星座
     */
    fun getZodiac(month: Int, day: Int): String {
        return ZODIAC[if (day >= ZODIAC_FLAGS[month - 1])
            month - 1
        else
            (month + 10) % 12]
    }

    /**
     * 得到当前的时间，精确到毫秒,共14位 返回格式:yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    fun getCurrentTime(): String {
        val nowDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(nowDate)
    }

    /**
     * 得到当前的年份 返回格式:yyyy
     *
     * @return String
     */
    fun getCurrentYear(): String {
        val nowDate = Date()

        val formatter = SimpleDateFormat("yyyy")
        return formatter.format(nowDate)
    }

    /**
     * 得到当前的月份 返回格式:MM
     *
     * @return String
     */
    fun getCurrentMonth(): String {
        val nowDate = Date()

        val formatter = SimpleDateFormat("MM")
        return formatter.format(nowDate)
    }

    /**
     * 得到当前的日期 返回格式:dd
     *
     * @return String
     */
    fun getCurrentDay(): String {
        val nowDate = Date()

        val formatter = SimpleDateFormat("dd")
        return formatter.format(nowDate)
    }

    /**
     * 得到当前的时间 返回格式:HH:mm:
     *
     * @return String
     */
    fun getCurrentHoursMinutes(): String {
        val nowDate = Date()
        val formatter = SimpleDateFormat("HH:mm")
        return formatter.format(nowDate)
    }

    /**
     * 得到当前的时间，精确到毫秒,共14位 返回格式:yyyyMMddHHmmss
     *
     * @return String
     */
    fun getCurrentTime2(): String {
        val nowDate = Date()
        val formatter = SimpleDateFormat("yyyyMMddHHmmss")
        return formatter.format(nowDate)
    }

    /**
     * 转换字符（yyyy-MM-dd）串日期到Date
     *
     * @param date
     * @return
     */
    fun convertToDate(date: String): Date? {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        try {
            return formatter.parse(date)
        } catch (e: Exception) {
//            LogUtils.d(e.message)
            return null
        }

    }

    /**
     * 转换日期到字符换yyyy-MM-dd
     *
     * @param date
     * @return
     */
    fun convertToString(date: Date): String? {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        try {
            return formatter.format(date)
        } catch (e: Exception) {
//            LogUtils.d(e.message)
            return null
        }

    }

    /**
     * 得到当前的时间加上输入年后的时间，精确到毫秒,共19位 返回格式:yyyy-MM-dd:HH:mm:ss
     *
     * @return String
     */
    fun getCurrentTimeAddYear(addyear: Int): String {
        val nowDate = Date()

        var currentYear = TimeUtils.getCurrentYear()
        currentYear = (Integer.parseInt(
            TimeUtils
                .getCurrentYear()
        ) + addyear).toString()

        val formatter = SimpleDateFormat("-MM-dd:HH:mm:ss")
        val currentTime = formatter.format(nowDate)
        return currentYear + currentTime
    }

    /**
     * 得到当前的日期,共10位 返回格式：yyyy-MM-dd
     *
     * @return String
     */
    fun getCurrentDate(): String {
        val nowDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(nowDate)
    }

    /**
     * 得到当前的日期,共8位 返回格式：yyyyMMdd
     *
     * @return String
     */
    fun getDate8Bit(): String {
        val nowDate = Date()
        val formatter = SimpleDateFormat("yyyyMMdd")
        return formatter.format(nowDate)
    }

    /**
     * 得到当前日期加上某一个整数的日期，整数代表天数
     *
     * @param currentdate String 格式 yyyy-MM-dd
     * @param add_day     int
     * @return yyyy-MM-dd
     */
    fun addDay(currentdate: String, add_day: Int): String? {
        var gc: GregorianCalendar? = null
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val year: Int
        val month: Int
        val day: Int

        try {
            year = Integer.parseInt(currentdate.substring(0, 4))
            month = Integer.parseInt(currentdate.substring(5, 7)) - 1
            day = Integer.parseInt(currentdate.substring(8, 10))

            gc = GregorianCalendar(year, month, day)
            gc.add(GregorianCalendar.DATE, add_day)

            return formatter.format(gc.time)
        } catch (e: Exception) {
//            LogUtils.d(e.message)
            return null
        }

    }

    /**
     * 得到当前月份的第一天日期
     *
     * @param period yyyy-MM
     */
    fun getStartDateInPeriod(period: String): String? {
        val df = SimpleDateFormat("yyyy-MM")
        try {
            if (df.parse(period) == null) {
                return null
            }
        } catch (e: ParseException) {
//            LogUtils.d(e.message)
            return null
        }

        val year = Integer.parseInt(period.substring(0, 4))
        val month = Integer.parseInt(period.substring(5, 7))
        val cl = Calendar.getInstance()
        cl.set(year, month - 1, 1)
        return df.format(cl.time)

    }

    /**
     * 得到当前月份的最后一天
     *
     * @param period yyyy-MM
     * @return
     */
    fun getEndDateInPeriod(period: String): String? {
        val df = SimpleDateFormat("yyyy-MM")
        try {
            if (df.parse(period) == null) {
                return null
            }
        } catch (e: ParseException) {
//            LogUtils.d(e.message)
            return null
        }

        val year = Integer.parseInt(period.substring(0, 4))
        val month = Integer.parseInt(period.substring(5, 7))
        val cl = Calendar.getInstance()
        cl.set(year, month - 1, 1)
        cl.add(Calendar.MONTH, 1)
        cl.add(Calendar.DATE, -1)
        return df.format(cl.time)
    }

    /**
     * 将YYYYMMDD形式改成YYYY-MM-DD
     *
     * @param str
     * @return
     */
    fun convertStandard(str: String?): String? {
        var timeStr: String? = null
        if (str == null || str == "") {
            timeStr = null
        } else {
            var format = SimpleDateFormat("yyyyMMDD")
            try {
                val date = format.parse(str)
                format = SimpleDateFormat("yyyy-MM-dd")
                timeStr = format.format(date!!)
            } catch (e: ParseException) {
//                LogUtils.d(e.message)
                timeStr = null
            }

        }
        return timeStr
    }

    /**
     * 将YYYY-MM-DD形式改成YYYYMMDD
     *
     * @param str
     * @return
     */
    fun convert8Bit(str: String?): String? {
        var timeStr: String? = null
        if (str == null || str == "") {
            timeStr = null
        } else {
            var format = SimpleDateFormat("yyyy-MM-dd")
            try {
                val date = format.parse(str)
                format = SimpleDateFormat("yyyyMMDD")
                timeStr = format.format(date!!)
            } catch (e: ParseException) {
//                LogUtils.d(e.message)
                timeStr = null
            }

        }
        return timeStr
    }

    /**
     * 转换时间yyyy-MM-dd HH:mm:ss到毫秒数
     *
     * @param str
     * @return
     */
    fun convertLong(str: String): Long {
        val dfs = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var currentTime: Date? = null
        var time: Long = -1
        try {
            currentTime = dfs.parse(str)
            time = currentTime!!.time
        } catch (e: ParseException) {
//            LogUtils.d(e.message)
        }

        return time
    }

    fun formatDateString(time: String): String {
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time)
        } catch (e: ParseException) {
//            LogUtils.d(e.message)
        }

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(date!!)
    }

    fun formatTimeString2(time: String): String {
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time)
        } catch (e: ParseException) {
//            LogUtils.d(e.message)
        }

        val formatter = SimpleDateFormat("HH:mm:ss")
        return formatter.format(date!!)
    }

    fun formatTimeString(time: String): String {
        var date: Date? = null
        try {
            date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time)
        } catch (e: ParseException) {
//            LogUtils.d(e.message)
        }

        val formatter = SimpleDateFormat("HH:mm")
        return formatter.format(date!!)
    }

    /**
     * 将long形式改成yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @param zone
     * @return
     */
    fun formatYmdHms(time: Long, zone: Int): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", zone))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成yyyy-MM-dd
     *
     * @param time
     * @param zone
     * @return
     */
    fun formatYmd(time: Long, zone: Int): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", zone))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成MM-dd HH:mm:ss
     *
     * @param time
     * @param zone
     * @return
     */
    fun formatMdHms(time: Long, zone: Int): String {
        val formatter = SimpleDateFormat("MM-dd HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", zone))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成MM-dd HH:mm:ss
     *
     * @param time
     * @param zone
     * @return
     */
    fun formatHms(time: Long, zone: Int): String {
        val formatter = SimpleDateFormat("HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", zone))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成MM-dd HH:mm
     *
     * @param time
     * @param zone
     * @return
     */
    fun formatHm(time: Long, zone: Int): String {
        val formatter = SimpleDateFormat("HH:mm")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", zone))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成mm:ss
     *
     * @param time
     * @param zone
     * @return
     */
    fun formatMs(time: Long, zone: Int): String {
        val formatter = SimpleDateFormat("mm:ss")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", zone))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成pattern
     *
     * @param time
     * @param pattern
     * @param zone
     * @return
     */
    fun format(time: Long, pattern: String, zone: Int): String {
        val formatter = SimpleDateFormat(pattern)
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", 8))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    fun formatYmdHms(time: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", 8))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成yyyy-MM-dd
     *
     * @return
     */
    fun formatYmd(time: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", 8))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    fun formatMdHms(time: Long): String {
        val formatter = SimpleDateFormat("MM-dd HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", 8))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成HH:mm:ss
     *
     * @param time
     * @return
     */
    fun formatHms(time: Long): String {
        val formatter = SimpleDateFormat("HH:mm:ss")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", 8))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成HH:mm
     *
     * @param time
     * @return
     */
    fun formatHm(time: Long): String {
        val formatter = SimpleDateFormat("HH:mm")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", 8))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成mm:ss
     *
     * @param time
     * @return
     */
    fun formatMs(time: Long): String {
        val formatter = SimpleDateFormat("mm:ss")
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", 8))
        return formatter.format(Date(time))
    }

    /**
     * 将long形式改成pattern
     *
     * @param time
     * @param pattern
     * @return
     */
    fun format(time: Long, pattern: String): String {
        val formatter = SimpleDateFormat(pattern)
        formatter.timeZone = TimeZone.getTimeZone(String.format("GMT+%02d:00", 8))
        return formatter.format(Date(time))
    }

    /**
     * 计算距今的时间
     *
     * @param time
     * @return
     */
    fun formatRecentTime(time: String?): String? {
        if (null == time || "" == time) {
            return ""
        }
        var commentTime: Date? = null
        var currentTime: Date? = null
        val dfs = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            commentTime = dfs.parse(time)
            currentTime = Calendar.getInstance().time
        } catch (e: ParseException) {
//            LogUtils.d(e.message)
            return null
        }

        val between = (currentTime!!.time - commentTime!!.time) / 1000// 除以1000是为了转换成秒

        val year = between / (24 * 3600 * 30 * 12)
        val month = between / (24 * 3600 * 30)
        val week = between / (24 * 3600 * 7)
        val day = between / (24 * 3600)
        val hour = between % (24 * 3600) / 3600
        val minute = between % 3600 / 60
        val second = between % 60 / 60

        if (year != 0L) {
            val sb = StringBuffer()
            sb.append(year).append("年前")
            return sb.toString()
        }
        if (month != 0L) {
            val sb = StringBuffer()
            sb.append(month).append("个月前")
            return sb.toString()
        }
        if (week != 0L) {
            val sb = StringBuffer()
            sb.append(week).append("周前")
            return sb.toString()
        }
        if (day != 0L) {
            val sb = StringBuffer()
            sb.append(day).append("天前")
            return sb.toString()
        }
        if (hour != 0L) {
            val sb = StringBuffer()
            sb.append(hour).append("小时前")
            return sb.toString()
        }
        if (minute != 0L) {
            val sb = StringBuffer()
            sb.append(minute).append("分钟前")
            return sb.toString()
        }
        if (second != 0L) {
            val sb = StringBuffer()
            sb.append(second).append("秒前")
            return sb.toString()
        }

        return ""
    }

    /**
     * 转化为中文时间格式
     *
     * @param time HH:mm:ss
     * @return
     */
    fun getZhTimeString(time: String): String {
        val str = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (str.size == 3) {
            (Integer.valueOf(str[0]).toString() + "小时" + Integer.valueOf(str[1])
                    + "分" + Integer.valueOf(str[2]) + "秒")
        } else if (str.size == 2) {
            (Integer.valueOf(str[0]).toString() + "分" + Integer.valueOf(str[1])
                    + "秒")
        } else {
            Integer.valueOf(str[0]).toString() + "秒"
        }
    }

    /**
     * 获取格式化日期yyyy-MM-dd
     *
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @return
     */
    fun getFormatDate(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
        val nf = DecimalFormat("00")
        return (year.toString() + "-" + nf.format((monthOfYear + 1).toLong()) + "-"
                + nf.format(dayOfMonth.toLong()))
    }

    /**
     * 获取格式化时间HH:mm
     *
     * @param hourOfDay
     * @param minute
     * @return
     */
    fun getFormatTime(hourOfDay: Int, minute: Int): String {
        val nf = DecimalFormat("00")
        return nf.format(hourOfDay.toLong()) + ":" + nf.format(minute.toLong())
    }

    /**
     * 格式化为应用 常见显示格式 当前天显示时间，其他显示年月日
     *
     * @param strTime yyyy-MM-dd HH:mm:ss
     * @return
     */
    fun formatLatelyTime(strTime: String?): String? {
        if (null == strTime || "" == strTime) {
            return ""
        }
        var dfs = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var currentTime: Date?
        var commentTime: Date?
        var str: String?
        try {
            currentTime = Calendar.getInstance().time
            commentTime = dfs.parse(strTime)
            if (currentTime!!.year == commentTime!!.year
                && currentTime.month == commentTime.month
                && currentTime.date == commentTime.date
            ) {
                dfs = SimpleDateFormat("HH:mm")
                str = dfs.format(commentTime)
            } else if (currentTime.year == commentTime.year
                && currentTime.month == commentTime.month
                && currentTime.date != commentTime.date
            ) {
                dfs = SimpleDateFormat("MM-dd")
                str = dfs.format(commentTime)
            } else if (currentTime.year == commentTime.year && currentTime.month != commentTime.month) {
                dfs = SimpleDateFormat("yyyy-MM")
                str = dfs.format(commentTime)
            } else {
                dfs = SimpleDateFormat("yyyy-MM-DD")
                str = dfs.format(commentTime)
            }
        } catch (e: ParseException) {
//            LogUtils.d(e.message)
            return null
        }

        return str
    }

    /**
     * 判断是同一天
     *
     * @param time1
     * @param time2
     * @return
     */
    fun isSameDay(time1: Long, time2: Long): Boolean {
        val calDateA = Calendar.getInstance()
        calDateA.timeInMillis = time1

        val calDateB = Calendar.getInstance()
        calDateB.timeInMillis = time2

        return (calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
            .get(Calendar.DAY_OF_MONTH))
    }

    /**
     * 判断是同一月
     *
     * @param time1
     * @param time2
     * @return
     */
    fun isSameMonth(time1: Long, time2: Long): Boolean {
        val time1Ca = Calendar.getInstance()
        time1Ca.firstDayOfWeek = Calendar.MONDAY
        time1Ca.timeInMillis = time1
        val time2Ca = Calendar.getInstance()
        time2Ca.firstDayOfWeek = Calendar.MONDAY
        time2Ca.timeInMillis = time2
        return time1Ca.get(Calendar.YEAR) == time2Ca.get(Calendar.YEAR) && time1Ca.get(Calendar.MONTH) == time2Ca.get(
            Calendar.MONTH
        )
    }

    /**
     * 判断是同一周
     *
     * @param time1
     * @param time2
     * @return
     */
    fun isSameWeek(time1: Long, time2: Long): Boolean {
        val time1Ca = Calendar.getInstance()
        time1Ca.firstDayOfWeek = Calendar.MONDAY
        time1Ca.timeInMillis = time1
        val time2Ca = Calendar.getInstance()
        time2Ca.firstDayOfWeek = Calendar.MONDAY
        time2Ca.timeInMillis = time2
        return time1Ca.get(Calendar.WEEK_OF_YEAR) == time2Ca.get(Calendar.WEEK_OF_YEAR)
    }

    /**
     * 当天剩余时间
     * 返回值是秒
     */
    fun getTimeRemaining(): Int {
        val curDate = Calendar.getInstance()
        val tommorowDate: Calendar = GregorianCalendar(
            curDate[Calendar.YEAR], curDate[Calendar.MONTH], curDate[Calendar.DATE] + 1, 0, 0, 0
        )
        return (tommorowDate.timeInMillis - curDate.timeInMillis).toInt() / 1000
    }

}