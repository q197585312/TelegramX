package tw.nekogram.utils

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.TextView
import org.telegram.messenger.AndroidUtilities
import org.telegram.messenger.FileLog
import org.telegram.ui.ActionBar.BaseFragment
import org.telegram.ui.Components.URLSpanNoUnderline
import java.util.regex.Matcher
import java.util.regex.Pattern

object StrUtil {

    private val urlPattern = Pattern.compile("@[a-zA-Z\\d_]{1,32}")

    @JvmStatic
    fun setText(fragment: BaseFragment?, textView: TextView, text: String) {

        var stringBuilder: SpannableStringBuilder? = null

        if (fragment != null) {

            try {
                val matcher: Matcher = urlPattern.matcher(text)
                while (matcher.find()) {
                    if (stringBuilder == null) {
                        stringBuilder = SpannableStringBuilder(text)
                        textView.movementMethod = AndroidUtilities.LinkMovementMethodMy()
                    }
                    var start = matcher.start()
                    val end = matcher.end()
                    if (text.get(start) != '@') {
                        start++
                    }
                    val url: URLSpanNoUnderline = object : URLSpanNoUnderline(text.subSequence(start + 1, end).toString()) {
                        override fun onClick(widget: View) {
                            fragment.messagesController.openByUserName(url, fragment, 1)
                        }
                    }
                    stringBuilder.setSpan(url, start, end, 0)
                }
            } catch (e: Exception) {
                FileLog.e(e)
            }
        }

        textView.text = stringBuilder ?: text
    }

}