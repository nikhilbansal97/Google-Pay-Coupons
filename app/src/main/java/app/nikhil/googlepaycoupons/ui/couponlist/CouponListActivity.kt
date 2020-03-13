package app.nikhil.googlepaycoupons.ui.couponlist

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.nikhil.googlepaycoupons.R
import app.nikhil.googlepaycoupons.R.layout
import app.nikhil.googlepaycoupons.data.Coupon
import app.nikhil.googlepaycoupons.databinding.ActivityCouponListBinding
import app.nikhil.googlepaycoupons.databinding.LayoutCouponItemBinding
import app.nikhil.googlepaycoupons.ui.couponlist.CouponRecyclerView.CouponClickListener
import app.nikhil.googlepaycoupons.ui.couponoverlay.CouponOverlayActivity
import app.nikhil.googlepaycoupons.utils.TestData
import java.text.NumberFormat
import java.util.Locale

class CouponListActivity : AppCompatActivity(), CouponClickListener {

  private lateinit var binding: ActivityCouponListBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initUI()
    setupAmountWithCurrency()
    setupCouponRecycler()
  }

  private fun setupCouponRecycler() {
    binding.couponRecycler.adapter =
      CouponRecyclerView(TestData.couponsList, this)
  }

  private fun initUI() {
    binding = DataBindingUtil.setContentView(
      this,
      layout.activity_coupon_list
    )
  }

  private fun setupAmountWithCurrency() {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    val amountText = numberFormat.format(600)
    binding.collapsingToolbarLayout.title = amountText
  }

  override fun onCouponClicked(coupon: Coupon, couponItemBinding: LayoutCouponItemBinding) {
    navigateToCouponOverlayActivity(coupon, couponItemBinding)
  }

  private fun navigateToCouponOverlayActivity(
    coupon: Coupon,
    couponItemBinding: LayoutCouponItemBinding
  ) {
    val intent = Intent(this, CouponOverlayActivity::class.java)
    val activityOptions = ActivityOptions
      .makeSceneTransitionAnimation(
        this,
        couponItemBinding.root,
        getString(R.string.coupon_transition_name)
      )

    startActivity(intent, activityOptions.toBundle())
  }
}
