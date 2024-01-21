package com.example.delightcart.Dialog

import android.app.Activity
import android.view.View
import com.example.delightcart.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Activity.setupOrderConfirmedDialog() {

    val dialog = BottomSheetDialog(this, R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.order_confirmed_dialog_layout, null)
    dialog.setContentView(view)
    val behavior = BottomSheetBehavior.from(view.parent as View)
    behavior.peekHeight = resources.displayMetrics.heightPixels / 2
    dialog.show()
}