package io.github.kvwrapper

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.github.kvwrapper.account.AccountManager
import io.github.kvwrapper.base.AppContext
import io.github.kvwrapper.data.UsageData
import io.github.kvwrapper.data.UserInfo
import io.github.kvwrapper.databinding.ActivityMainBinding
import io.github.kvwrapper.util.onClick

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLaunchTime()

        refreshAccountInfoViews()

        binding.loginBtn.onClick {
            if (AccountManager.isLogin()) {
                AccountManager.logout()
                binding.switchAccountBtn.isEnabled = false
            } else {
                if (UsageData.lastLoginUid == 0L) {
                    AccountManager.login(10001L)
                } else {
                    AccountManager.login(UsageData.lastLoginUid)
                }
                binding.switchAccountBtn.isEnabled = true
            }
            binding.tipsTv.text = ""
            refreshAccountInfoViews()
        }

        binding.switchAccountBtn.onClick {
            if (AppContext.isLogin()) {
                if (AppContext.uid == 10001L) {
                    AccountManager.switchAccount(10002L)
                } else {
                    AccountManager.switchAccount(10001L)
                }
            }
            refreshAccountInfoViews()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun refreshAccountInfoViews() {
        binding.run {
            if (AccountManager.isLogin()) {
                switchAccountBtn.isEnabled = true
                loginBtn.text = getString(R.string.logout)
                accountInfoTv.visibility = View.VISIBLE
                userInfoTv.visibility = View.VISIBLE
                accountInfoTv.text = AccountManager.getUserAccountInfo()
                userInfoTv.text = AccountManager.formatUserInfo()
            } else {
                switchAccountBtn.isEnabled = false
                loginBtn.text = getString(R.string.login)
                accountInfoTv.visibility = View.GONE
                userInfoTv.visibility = View.GONE
            }
        }
    }

    private fun showLaunchTime() {
        val t = UsageData.launchCount + 1
        binding.tipsTv.text = getString(R.string.main_tips, t)
        UsageData.launchCount = t
    }
}