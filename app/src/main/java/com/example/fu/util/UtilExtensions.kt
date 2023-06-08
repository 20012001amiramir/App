package com.example.fu.util

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.Dimension
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.fu.BuildConfig
import com.example.fu.R
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.cert.X509Certificate
import java.time.LocalDate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

inline fun <A, B> ifNotNull(a: A?, b: B?, block: (A, B) -> Unit): Any? =
    if (a != null && b != null) block(a, b) else null


inline fun <A, B, C> ifNotNull(a: A?, b: B?, c: C?, block: (A, B, C) -> Unit) {
    if (a != null && b != null && c != null) block(a, b, c)
}

/**
 * Finds [NavController] when using [FragmentContainerView].
 * See [issue](https://issuetracker.google.com/issues/142847973#comment4).
 */
fun AppCompatActivity.navController(@IdRes viewId: Int): NavController {
    return (supportFragmentManager.findFragmentById(viewId) as NavHostFragment).navController
}

// TODO: Seems like it is ui extension. Maybe move?
fun dpToPx(displayMetrics: DisplayMetrics, @Dimension(unit = Dimension.DP) dp: Float) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        displayMetrics
    )

//This is way to overcome stateflow distinctUntilChanged by default
//You need to emit ONLY not null values to make it work properly
var <T> MutableStateFlow<T?>.valueAsUnique: T?
    get() = value
    set(valueToSet: T?) {
        value = null
        value = valueToSet
    }

/**
 * Prevents navigator from crashing caused by duplicate clicks.
 * See: https://stackoverflow.com/questions/51060762/illegalargumentexception-navigation-destination-xxx-is-unknown-to-this-navcontr
 */

fun NavController.navigateSafe(currentDestinationId: Int, actionId: Int, args: Bundle? = null) {
    if (currentDestination?.id == currentDestinationId) {
        navigate(actionId, args)
    }
}

fun Context.openUrl(url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()

    customTabsIntent.intent.apply {
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    }

    customTabsIntent.launchUrl(this, Uri.parse(url))
}

fun OkHttpClient.Builder.trustAllCertificates(trustAll: Boolean) {
    val trustManagers = getTrustManagers(trustAll)

    val sslSocketFactory = try {
        javax.net.ssl.SSLContext.getInstance("TLS")
            .apply { init(null, trustManagers, null) }
            .socketFactory
    } catch (exception: GeneralSecurityException) {
//        robologs.Robolog.e(
//            "no tls ssl socket factory available",
//            robologs.Meta<Throwable>(exception)
//        )
        null
    }
}

fun getTrustManagers(trustAll: Boolean): Array<X509TrustManager>? =
    if (trustAll) {
        arrayOf(object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })
    } else {
        val x509TrustManager = try {
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                .apply { init(null as KeyStore?) }
                .trustManagers[0] as X509TrustManager
        } catch (exception: GeneralSecurityException) {
//            Robolog.e(
//                "no trust manager available",
//                Meta<Throwable>(exception)
//            )
            null
        }
        x509TrustManager?.let { arrayOf(it) }
    }

fun getSSLSocketFactory(trustManagers: Array<X509TrustManager>?): SSLSocketFactory? =
    try {
        SSLContext.getInstance("TLS")
            .apply { init(null, trustManagers, null) }
            .socketFactory
    } catch (exception: GeneralSecurityException) {
//        Robolog.e(
//            "no tls ssl socket factory available",
//            Meta<Throwable>(exception)
//        )
        null
    }

fun getTatarAffixNumber(number: Int): String =
    number.toString() +
            when (number % 10) {
                1, 2, 7, 8 -> {
                    "дән"
                }
                3, 4, 5 -> {
                    "тән"
                }
                6, 9 -> {
                    "дан"
                }
                0 -> {
                    when (number % 100) {
                        0, 20, 50 -> {
                            "дән"
                        }
                        10, 90 -> {
                            "нан"
                        }
                        30 -> {
                            "дан"
                        }
                        40, 60 -> {
                            "тан"
                        }
                        70 -> {
                            "тән"
                        }
                        80 -> {
                            "нән"
                        }
                        else -> ""
                    }
                }
                else -> ""
            }


fun versionAllowsToFullyColorizeNavBar(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}

fun <T> MutableList<T>.replaceWith(newElement: T, predicate: (T) -> Boolean) {
    val index = indexOf(firstOrNull(predicate))

    if (index != -1) {
        removeAt(index)
        add(index, newElement)
    }
}

fun getMinWidgetWidth(options: Bundle?): Int {
    return if (options == null || !options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH )
    ) {
        0
    } else {
        options.get(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) as Int
    }
}