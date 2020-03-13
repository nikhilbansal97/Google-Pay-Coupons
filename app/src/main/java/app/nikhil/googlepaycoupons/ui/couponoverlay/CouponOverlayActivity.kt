package app.nikhil.googlepaycoupons.ui.couponoverlay

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.nikhil.googlepaycoupons.R
import app.nikhil.googlepaycoupons.databinding.ActivityCouponOverlayBinding

class CouponOverlayActivity : AppCompatActivity() {

  private lateinit var binding: ActivityCouponOverlayBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_coupon_overlay)
    binding.rootLayout.setOnClickListener { finishAfterTransition() }
    binding.couponLayout.rootCardLayout.setOnClickListener {
      Toast.makeText(this, "Coupon clicked", Toast.LENGTH_SHORT).show()
    }
  }
}