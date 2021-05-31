package org.zlobste.spotter

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.toolbar_with_image.*
import kotlinx.android.synthetic.main.toolbar_with_image.view.*
import org.zlobste.spotter.databinding.ActivityMainBinding
import org.zlobste.spotter.features.analytics.view.AnalyticsFragment
import org.zlobste.spotter.features.drunk_drivers.DrunkDriversFragment
import org.zlobste.spotter.features.my_drivers.view.MyDriversFragment
import org.zlobste.spotter.features.sign_in.view.SignInActivity
import org.zlobste.spotter.identity.CredentialsProvider
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private lateinit var binding: ActivityMainBinding
    private val credentialsProvider: CredentialsProvider by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        initToolbar()
        initBottomNavBar()
    }

    private fun initToolbar() {
        toolbar_with_image.title_text_view.text = getString(R.string.my_drivers)
        toolbar_with_image.log_out_imageView.setOnClickListener {
            credentialsProvider.setCredentials(null)

            showLogOutDialog()
        }
    }

    private fun initBottomNavBar() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {

            val selectedFragment: Fragment = when (it.itemId) {
                R.id.drivers -> {
                    MyDriversFragment.getInstance()
                }
                R.id.drunk_drivers -> {
                    DrunkDriversFragment.getInstance()
                }
                R.id.analytics -> {
                    AnalyticsFragment.getInstance()
                }
                else -> return@setOnNavigationItemSelectedListener false
            }

            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment).commit()

            return@setOnNavigationItemSelectedListener true
        }
        binding.bottomNavigation.selectedItemId = R.id.drivers
    }


    private fun showLogOutDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.sign_out_confirmation)
            .setPositiveButton(R.string.yes) { _, _ ->
                returnToSignIn()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun returnToSignIn() {
        startActivity(
            Intent(
                this,
                SignInActivity::class.java
            )
        )
        finish()
    }
}