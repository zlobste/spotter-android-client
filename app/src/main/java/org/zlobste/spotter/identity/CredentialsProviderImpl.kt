package org.zlobste.spotter.identity

import android.content.Context
import com.google.gson.Gson
import org.zlobste.spotter.features.sign_in.model.Credentials

class CredentialsProviderImpl(context: Context) : CredentialsProvider {
    private val info = context.getSharedPreferences("allWalletInfo", Context.MODE_PRIVATE)

    private var credentialsInfo: Credentials? = null

    override fun setCredentials(credentials: Credentials?) {
        if (credentials == null) {
            info.edit().clear().apply()
        } else {
            val prefsEditor = info.edit()
            val newData = Gson().toJson(credentials)

            prefsEditor.putString("credentialsInfo", newData)
            prefsEditor.apply()
            Gson().fromJson(newData, Credentials::class.java)
        }
        credentialsInfo = credentials
    }

    override fun getCredentials(): Credentials? {
        if (credentialsInfo == null) {
            val wallet = info.getString("credentialsInfo", null)
            return Gson().fromJson(wallet, Credentials::class.java).also {
                credentialsInfo = it
            }
        }
        return credentialsInfo
    }
}