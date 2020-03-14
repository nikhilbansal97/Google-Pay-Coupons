package app.nikhil.googlepaycoupons.ui.couponoverlay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.nikhil.googlepaycoupons.R
import app.nikhil.googlepaycoupons.custom.CouponView.CouponScratchListener
import app.nikhil.googlepaycoupons.databinding.ActivityCouponOverlayBinding

class CouponOverlayActivity : AppCompatActivity(), CouponScratchListener {

  private lateinit var binding: ActivityCouponOverlayBinding
  private var isCouponScratched = false
  private var isRewardVisible = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_coupon_overlay)
    binding.rootLayout.setOnClickListener {
      handleOverlayClick()
    }
    binding.couponLayout.couponView.setCouponScratchListener(this)
  }

  private fun handleOverlayClick() {
    when {
      shouldShowReward() -> {
        binding.couponLayout.couponView.showReward()
        isRewardVisible = true
      }
      shouldFinish() -> {
        finishAfterTransition()
      }
    }
  }

  private fun shouldFinish() = isRewardVisible || !isCouponScratched

  private fun shouldShowReward() = isCouponScratched && !isRewardVisible

  override fun onCouponScratched() {
    isCouponScratched = true
  }

  override fun onStop() {
    binding.couponLayout.couponView.removeCouponScratchListener()
    super.onStop()
  }
}