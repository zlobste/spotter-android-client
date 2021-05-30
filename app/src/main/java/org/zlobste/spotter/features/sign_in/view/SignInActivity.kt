package org.zlobste.spotter.features.sign_in.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.zlobste.spotter.MainActivity
import org.zlobste.spotter.R
import org.zlobste.spotter.databinding.ActivitySignInBinding
import org.zlobste.spotter.features.sign_in.model.Credentials
import org.zlobste.spotter.features.sign_in.model.Users
import org.zlobste.spotter.identity.CredentialsProvider
import org.zlobste.spotter.util.ActivitiesUtil
import org.zlobste.spotter.util.ConnectivityInterceptor
import org.zlobste.spotter.util.ToastManager
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class SignInActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private val toastManager: ToastManager by instance()

    private val credentialsProvider: CredentialsProvider by instance()

    val isLoading = MutableLiveData(false)
    val fields: MutableMap<String, MutableLiveData<String>> = mutableMapOf(
        "email" to MutableLiveData(),
        "password" to MutableLiveData()
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (credentialsProvider.getCredentials() != null) {
            goToMainActivity()
        } else {
            setContentView(R.layout.activity_sign_in)

            val binding: ActivitySignInBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
            binding.lifecycleOwner = this
            binding.activity = this

            initButtons()
        }
    }

    private fun initButtons() {
        login_button.setOnClickListener {
            if (ConnectivityInterceptor.isOnline(this)) {
                ActivitiesUtil.hideKeyboard(this)
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(fields["email"]?.value!!)
                        .matches()
                ) {
                    emailInputLayout.error = resources.getString(R.string.error_invalid_email)
                } else {
                    emailInputLayout.error = null
                }
                signIn(
                    fields["email"]?.value!!,
                    fields["password"]?.value!!
                )
            } else {
                toastManager.short(R.string.no_connection)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        if (Users.usersList.containsKey(email) && Users.usersList[email] == password) {
            credentialsProvider.setCredentials(Credentials(email, password))
            goToMainActivity()
        } else {
            toastManager.short(getString(R.string.invalid_creds))
        }
    }

    private fun goToMainActivity() {
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            )
        )
        finish()
    }
}