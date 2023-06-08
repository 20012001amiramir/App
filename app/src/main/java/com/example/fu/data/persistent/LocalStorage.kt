package com.example.fu.data.persistent

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.fu.util.fromJson
import com.example.fu.domain.profile.AccountInfo
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
@RequiresApi(Build.VERSION_CODES.M)
class LocalStorage @Inject constructor(
    context: Context,
    moshi: Moshi
) {
    private val pref: SharedPreferences = createPrefs(context)
    private val securePrefs: SharedPreferences  = createSecurePrefs(context)

    private val _accountsFlow = MutableStateFlow(dataAccounts)
    val accountsFlow: StateFlow<String?> = _accountsFlow

    private val _bookmarkIdsFlow = MutableStateFlow(dataBookmarkDiseaseIds)
    val bookmarkIds = _bookmarkIdsFlow.asStateFlow()

    private val _profileNameFlow = MutableStateFlow(profileName)
    val profileNameFlow = _profileNameFlow.asStateFlow()

    private val _profileImageFlow = MutableStateFlow(profileImage)
    val profileImageFlow = _profileImageFlow.asStateFlow()

    var dataAccounts : String?
        set(value) {
            securePrefs.edit{ putString(ACCOUNT_INFO, value) }
            _accountsFlow.value = value
            accountActiveCount = -1
        }
        get() = securePrefs.getString(ACCOUNT_INFO, DefaultValue.ACCOUNT_INFO)

    var accountActiveCount : Int?
        private set(value) {
            securePrefs.edit{
                putInt(
                    ACTIVE_ACCOUNT_COUNT,
                    dataAccounts?.fromJson<List<AccountInfo>>()?.filter {
                        it.refreshToken != null
                    }?.size ?: -1
                )
            }

        }
        get() = securePrefs.getInt(ACTIVE_ACCOUNT_COUNT, -1)


    var dataBookmarkDiseaseIds: String?
        set(value) {
            securePrefs.edit{ putString(BOOKMARK_IDS, value) }
            _bookmarkIdsFlow.value = value
        }
        get() = securePrefs.getString(BOOKMARK_IDS, DefaultValue.BOOKMARK_IDS)

    var profileName: String?
        set(value) {
            securePrefs.edit{ putString(PROFILE_NAME, value) }
            _profileNameFlow.value = value
        }
        get() = securePrefs.getString(PROFILE_NAME, "")

    var profileImage: String?
        set(value) {
            securePrefs.edit{ putString(PROFILE_IMAGE, value) }
            _profileImageFlow.value = value
        }
        get() = securePrefs.getString(PROFILE_IMAGE, "")

    var refreshToken: String?
        set(value) {
            securePrefs.edit { putString(REFRESH_TOKEN_KEY, value) }
        }
        get() = securePrefs.getString(REFRESH_TOKEN_KEY, null)

    var possibleRefreshToken: String?
        set(value) {
            securePrefs.edit { putString(POSSIBLE_REFRESH_TOKEN_KEY, value) }
        }
        get() = securePrefs.getString(POSSIBLE_REFRESH_TOKEN_KEY, null)

    var fcmToken: String?
        set(value) {
            securePrefs.edit { putString(FCM_TOKEN_KEY, value) }
        }
        get() = securePrefs.getString(FCM_TOKEN_KEY, null)

    var idProfile: String?
        set(value) {
            securePrefs.edit { putString(PROFILE_ID_KEY, value) }
        }
        get() = securePrefs.getString(PROFILE_ID_KEY, null)

    var buttonActivated: Boolean
        set(value) {
            securePrefs.edit { putBoolean(BUTTON_ACTIVATED, value) }
        }
        get() = securePrefs.getBoolean(BUTTON_ACTIVATED, false)

    var appIsActive: Boolean
        set(value) {
            securePrefs.edit { putBoolean(ACTIVE_APP, value) }
        }
        get() = securePrefs.getBoolean(ACTIVE_APP, false)

    var three: String?
        set(value) {
            securePrefs.edit { putString(THREE, value) }
        }
        get() = securePrefs.getString(THREE, "2.5")

    var four: String?
        set(value) {
            securePrefs.edit { putString(FOUR, value) }
        }
        get() = securePrefs.getString(FOUR, "3.5")

    var five: String?
        set(value) {
            securePrefs.edit { putString(FIVE, value) }
        }
        get() = securePrefs.getString(FIVE, "4.5")

    var nightMode: String?
        set(value) {
            securePrefs.edit { putString(NIGHT_MODE, value) }
        }
        get() = securePrefs.getString(NIGHT_MODE, null)

    var deleteState: Boolean
        set(value) {
            securePrefs.edit { putBoolean(DELETE_STATE, value) }
        }
        get() = securePrefs.getBoolean(DELETE_STATE, false)

    var haveIcon: Boolean
        set(value) {
            securePrefs.edit { putBoolean(HAVE_ICON, value) }
        }
        get() = securePrefs.getBoolean(HAVE_ICON, false)


    var urlImageProfile: String?
        set(value) {
            securePrefs.edit { putString(URL_IMAGE_PROFILE, value) }
        }
        get() = securePrefs.getString(URL_IMAGE_PROFILE, null)

    var isOnboardingFinished: Boolean
        set(value) {
            pref.edit { putBoolean(ONBOARDING_FINISHED_KEY, value) }
        }
        get() = pref.getBoolean(ONBOARDING_FINISHED_KEY, false)

    private fun createPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }


    private fun createSecurePrefs(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            SECURE_PREFS_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    var shouldConfirmUserIsActive: Boolean
        get() = pref.getBoolean(SHOULD_CONFIRM_USER_IS_ACTIVE, false)
        set(value) = pref.edit { putBoolean(SHOULD_CONFIRM_USER_IS_ACTIVE, value)}

    fun clear() {
        refreshToken = null
        possibleRefreshToken = null
    }

    private class JsonDelegate<T>(
        private val key: String,
        private val pref: SharedPreferences,
        private val adapter: JsonAdapter<T>
    ) : ReadWriteProperty<LocalStorage, T?> {

        override fun setValue(thisRef: LocalStorage, property: KProperty<*>, value: T?) {
            pref.edit {
                if (value == null) remove(key)
                else putString(key, adapter.toJson(value))
            }
        }

        override fun getValue(thisRef: LocalStorage, property: KProperty<*>): T? {
            return try {
                pref.getString(key, null)?.let { adapter.fromJson(it) }
            } catch (e: JsonDataException) {
                Timber.e(e)
                setValue(thisRef, property, null)
                null
            }
        }
    }

    object DefaultValue {

        const val BOOKMARK_IDS = """
            [
            ]
        """

        const val ACCOUNT_INFO = """
            [
            ]
        """

        const val CASHED_NOTIFICATION = """
            [
            ]
        """
    }

    companion object {
        private const val PREFS_NAME = "school_preferences"
        private const val URL_IMAGE_PROFILE = "URL_IMAGE_PROFILE"
        private const val SECURE_PREFS_NAME = "school_preferences_2"
        private const val CODE_VERIFIER_KEY = "CODE_VERIFIER_KEY"
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
        private const val POSSIBLE_REFRESH_TOKEN_KEY = "POSSIBLE_REFRESH_TOKEN_KEY"
        private const val PROFILE_ID_KEY = "PROFILE_ID_KEY"
        private const val BUTTON_ACTIVATED = "BUTTON_ACTIVATED"
        private const val ACTIVE_APP = "ACTIVE_APP"
        private const val NIGHT_MODE = "NIGHT_MODE"
        private const val HAVE_ICON = "HAVE_ICON"
        private const val THREE = "THREE"
        private const val FOUR = "FOUR"
        private const val FIVE = "FIVE"
        private const val DELETE_STATE = "DELETE_STATE"
        private const val FCM_TOKEN_KEY = "FCM_TOKEN_KEY"
        private const val ONBOARDING_FINISHED_KEY = "ONBOARDING_FINISHED_KEY"
        private const val LANGUAGE_TAG = "LANGUAGE_TAG"
        private const val SHOULD_CONFIRM_USER_IS_ACTIVE = "SHOULD_CONFIRM_USER_IS_ACTIVE"
        private const val ACCOUNT_INFO = "ACCOUNT_INFO"
        private const val ACTIVE_ACCOUNT_COUNT = "ACTIVE_ACCOUNT_COUNT"
        private const val CASHED_NOTIFICATION = "CASHED_NOTIFICATION"
        private const val ID_NOTIFICATION = "ID_NOTIFICATION"

        private const val BOOKMARK_IDS = "BOOKMARK_IDS"
        private const val PROFILE_NAME = "PROFILE_NAME"
        private const val PROFILE_IMAGE = "PROFILE_IMAGE"
    }
}