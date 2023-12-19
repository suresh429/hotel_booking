package com.example.myapplication.utils


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.text.format.Formatter.formatIpAddress
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.UnsupportedEncodingException
import java.text.DecimalFormat
import java.text.MessageFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.roundToInt


var pattern: Pattern? = null
var matcher: Matcher? = null

fun validate(password: String?): Boolean {
    val passwordPattern =
        "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*(?=.*[!-\\/:-@\\[-`{-~])).{6,20})"
    /*
        val passwordPattern = """^(?=.*[a-z])(?=.*[A-Z])(?=.*d)(?=.*[@$!%*?&])[A-Za-zd@$!%*?&]{6,}$"""
    */
    pattern = Pattern.compile(passwordPattern)
    matcher = pattern!!.matcher(password)
    return matcher!!.matches()
}

fun customChromeTab(uri: String, context: Context) {
    // initializing object for custom chrome tabs.
    val customIntent = CustomTabsIntent.Builder()
    val customTabsIntent = customIntent.build()
    customTabsIntent.intent.setPackage(CHROME_PACKAGE_NAME)
    customTabsIntent.launchUrl(context, Uri.parse(uri))

}

@SuppressLint("HardwareIds")
fun deviceId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

fun <A : Activity> Activity.startClearActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}


fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(it)
    }
}

fun View.snackBar(message: String?) {
    val snackBar = Snackbar.make(this, message.toString(), Snackbar.LENGTH_LONG)
    snackBar.show()
}
fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

fun View.isVisible(isShowLoading: Boolean, container: View) {
    if (isShowLoading) {
        this.visibility = View.VISIBLE
        container.visibility = View.GONE
    } else {
        this.visibility = View.GONE
        container.visibility = View.VISIBLE
    }
}

fun bytesToHex(bytes: ByteArray): String {
    val hexChars = CharArray(bytes.size * 2)
    for (i in bytes.indices) {
        val v = bytes[i].toInt() and 0xFF
        hexChars[i * 2] = "0123456789ABCDEF"[v ushr 4]
        hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
    }
    return String(hexChars)
}

private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}

fun fromHtml(source: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(source)
    }
}

fun TextView.htmlText(text: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setText(Html.fromHtml(text.replace("\n", "<br />"), Html.FROM_HTML_MODE_LEGACY))
    } else {
        setText(Html.fromHtml(text.replace("\n", "<br />")))
    }
}

fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()

}

/*fun View.snackBarTop(message: String) {
    val snackBarView = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    val view = snackBarView.view
    val params = snackBarView.view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.CENTER_HORIZONTAL
    view.layoutParams = params.apply { setMargins(10, 280, 20, 10) }

    view.background = ContextCompat.getDrawable(
        context,
        R.drawable.custom_drawable
    ) // for custom background
    snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackBarView.show()
}*/

@SuppressLint("NewApi")
fun showToast(context: Context?, info: String) {
    val toast = Toast.makeText(
        context,
        //Html.fromHtml("<font color='#F41B34' ><b>$info</b></font>", Html.FROM_HTML_MODE_LEGACY),
        Html.fromHtml("<font color='#ec1c24' ><b>$info</b></font>", Html.FROM_HTML_MODE_LEGACY),
        Toast.LENGTH_LONG
    )
    toast.view?.setBackgroundColor(Color.parseColor("#000000"))
    toast.setGravity(Gravity.BOTTOM, 0, 0)
    toast.show()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun String.containsAnyOfIgnoreCase(keywords: List<String>): Boolean {
    for (keyword in keywords) {
        if (this.contains(keyword, true)) return true
    }
    return false
}

fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
    if (view.layoutParams is MarginLayoutParams) {
        val p = view.layoutParams as MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        view.requestLayout()
    }
}

fun isValidEmail(email: String): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun createDirectory(dirName: String, context: Context): File {

    val file = File(context.getExternalFilesDir(null), "/$dirName")
    if (!file.exists()) {
        file.mkdir()
    }

    return file
}


fun codeFromPhone(phone: String): String {

    var countryCode: String? = null
    var phone1: String? = null
    if (phone.length == 10) {
        phone1 = phone
    } else if (phone.length > 10) {
        val newStr = phone.substring(phone.length - 10)
        val separated = phone.split(newStr.trim { it <= ' ' }).toTypedArray()
        val str1 = separated[0]
        countryCode = str1.trim { it <= ' ' }
        phone1 = newStr.trim { it <= ' ' }
    }
    return countryCode.toString()
}

fun phoneFromCode(phone: String): String {
    // var countryCode: String? = null
    var phone1: String? = null
    if (phone.length == 10) {
        phone1 = phone
    } else if (phone.length > 10) {
        val newStr = phone.substring(phone.length - 10)
        val separated = phone.split(newStr.trim { it <= ' ' }).toTypedArray()
        val str1 = separated[0]
        //  countryCode = str1.trim { it <= ' ' }
        phone1 = newStr.trim { it <= ' ' }
    }
    return phone1.toString().replace("ull)(null)","").replace("+(null)(null)","")
}


fun Double.toWords(language: String, country: String): String {
    val formatter = MessageFormat(
        "{0,spellout,currency}",
        Locale(language, country)
    )
    return formatter.format(arrayOf(this))
}

fun format(amount:Double):String{
    val numberFormat = DecimalFormat("#,###.00")
    return numberFormat.format(amount)
}

@SuppressLint("SimpleDateFormat")
fun timeMillisToDate(date: Long): String {
    val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
    return simpleDateFormat.format(date)
}
 fun covertTimeToDate(date: Long): String {
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    val dateString = simpleDateFormat.format(date)
    return String.format("%s", dateString)
}


@SuppressLint("SimpleDateFormat")
fun gMeetTimeMillisToDate(date: Long): String {
    val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a")
    return simpleDateFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
fun timeMillisToSimpleDate(date: Long): String {
    // val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
    val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
    return simpleDateFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
fun timeMillisToSimpleDate2(date: Long): String {
    // val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
    val simpleDateFormat = SimpleDateFormat("MMM-yyyy")
    return simpleDateFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
fun checkDate(startDate: String, endDate: String): Boolean {
    val dfDate = SimpleDateFormat("dd-MMM-yyyy")
    var check = false
    if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
        check = true // If start date is before end date
    } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
        check = true //If two dates are equal
    } else {
        check = false; //If start date is after the end date
    }
    return check
}

@SuppressLint("SimpleDateFormat")
fun dateToFormat(date: String): String {
    var simpleDateFormat1 = SimpleDateFormat("dd-MMM-yyyy")
    val newDate = simpleDateFormat1.parse(date)
    simpleDateFormat1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    return newDate?.let { simpleDateFormat1.format(it) }.toString()
}


@SuppressLint("SimpleDateFormat")
fun formatToDate(date: String): String {
    var simpleDateFormat1: SimpleDateFormat? = null
    var newDate: Date? = null
    try {
        simpleDateFormat1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        newDate = simpleDateFormat1.parse(date)
        //simpleDateFormat1 = SimpleDateFormat("MMMM Do yyyy")
        simpleDateFormat1 = SimpleDateFormat("MMMM dd yyyy")
    } catch (e: ParseException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }

    return newDate?.let { simpleDateFormat1?.format(it) }.toString()
}

@SuppressLint("SimpleDateFormat")
fun currentDateTime(): String {
    val sdf = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss aa")
    return sdf.format(Date())
}


/*fun getAge(dobString: String): Int {
    var date: Date? = null
    @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("dd-MMM-yyyy")
    try {
        date = sdf.parse(dobString)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    if (date == null) return 0
    val dob = Calendar.getInstance()
    val today = Calendar.getInstance()
    dob.time = date
    val year = dob[Calendar.YEAR]
    val month = dob[Calendar.MONTH]
    val day = dob[Calendar.DAY_OF_MONTH]
    dob[year, month + 1] = day
    var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
    if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
        age--
    }
    return age
}*/

@SuppressLint("SimpleDateFormat")
fun getAge(dobString: String): String {
    val currentDate = Calendar.getInstance()
    val myFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
    var birthdate: Date? = null
    try {
        birthdate = myFormat.parse(dobString)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val time = currentDate.time.time / 1000 - birthdate!!.time / 1000
    val years = time.toFloat().roundToInt() / 31536000
    val months = (time - years * 31536000).toFloat().roundToInt() / 2628000
    val day = (time - months * 2628000).toFloat().roundToInt() / 86400

    var age = ""
    age = if (years <= 0) {
        "$months month"
    } /*else if (months <= 0) {
        "$day day"
    }*/ else {
        "$years year"
    }

    if (age != "1 month") {
        age += "s"
    } /*else if (age != "1 day") {
        age += "s"
    }*/ else if (age != "1 year") {
        age += "s"
    } else {
        age = age
    }

    Log.d("TAG", "getAge 2 : $age")

    return age
}

fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path =
        MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
    return Uri.parse(path)
}

fun Context.getBitmap(uri: Uri): Bitmap =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) ImageDecoder.decodeBitmap(
        ImageDecoder.createSource(
            this.contentResolver,
            uri
        )
    )
    else MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
    var width = image.width
    var height = image.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(image, width, height, true)
}

fun getRealPathFromUri(context: Context, contentUri: Uri?): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        cursor.getString(column_index)
    } finally {
        cursor?.close()
    }
}

@SuppressLint("Range")
fun getFileName(context: Context, uri: Uri): String {

    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor.use {
            if (cursor?.moveToFirst() == true) {
                return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
    }

    return uri.path?.substring(uri.path!!.lastIndexOf('/') + 1) ?: ""
}


fun ipAddress(context: Context): String {
    val wm = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return formatIpAddress(wm.connectionInfo.ipAddress)
}

fun markButtonEnableDisable(color: Int, isEnable: Boolean, context: Context, button: Button) {
    button.isEnabled = isEnable
    button.setTextColor(ContextCompat.getColor(context, R.color.white))
    button.setBackgroundColor(ContextCompat.getColor(context, color))
}



@SuppressLint("SimpleDateFormat")
fun dateToTimeMillis(dateFormat: String?): Long {
    var date = Date()
    val formatter = SimpleDateFormat("dd-MMM-yyyy")
    try {
        formatter.parse(dateFormat).also { date = it }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return date.time
}

@SuppressLint("SimpleDateFormat")
fun gmeetDateToTimeMillis(dateFormat: String?): Long {
    var date = Date()
    val formatter = SimpleDateFormat("dd-MMMM-yyyy hh:mm:ss a")
    try {
        formatter.parse(dateFormat).also { date = it }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return date.time
}


fun createAudioRecordings(pathName: String, context: Context): String {
    val contextWrapper = ContextWrapper(context)
    val music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
    val file = File(music, pathName)
    return file.path
}

val PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    mutableListOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.RECORD_AUDIO

    )
} /*else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
    mutableListOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO

    )
}*/ else {
    mutableListOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO

    )
}

val PERMISSIONS_CONVERSATIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    mutableListOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA

    )
} else {
    mutableListOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA

    )
}

fun getFileExtension(uri: Uri, context: Context): String? {
    val contentResolver: ContentResolver = context.contentResolver
    val mimeTypeMap = MimeTypeMap.getSingleton()
    // Return file Extension
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
}

fun convertUTF8ToString(s: String): String? {
    var out: String? = null
    out = try {
        String(s.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
    } catch (e: UnsupportedEncodingException) {
        return null
    }
    return out
}


/*fun isRecordingOk(context: Context?): Boolean {
    return ContextCompat.checkSelfPermission(
        context!!,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED
}

fun requestRecording(activity: Activity?) {
    ActivityCompat.requestPermissions(
        activity!!,
        arrayOf<String>(Manifest.permission.RECORD_AUDIO),
        AllConstants.RECORDING_REQUEST_CODE
    )
}*/
