package app.nikhil.googlepaycoupons.ui.couponlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.nikhil.googlepaycoupons.data.Coupon
import app.nikhil.googlepaycoupons.databinding.LayoutCouponItemBinding
import app.nikhil.googlepaycoupons.ui.couponlist.CouponRecyclerView.CouponViewHolder

class CouponRecyclerView(
  private val couponItems: List<Coupon>,
  private val listener: CouponClickListener
) : RecyclerView.Adapter<CouponViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder =
    CouponViewHolder(LayoutCouponItemBinding.inflate(LayoutInflater.from(parent.context)))

  override fun getItemCount(): Int = couponItems.size

  override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
    holder.bind()
  }

  inner class CouponViewHolder(private val binding: LayoutCouponItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind() {
      val currentCoupon = couponItems[adapterPosition]
      binding.couponView.setOnClickListener { listener.onCouponClicked(currentCoupon, binding) }
    }
  }

  interface CouponClickListener {
    fun onCouponClicked(coupon: Coupon, couponItemBinding: LayoutCouponItemBinding)
  }
}