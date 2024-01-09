package com.example.delightcart.Dialog

import android.app.Activity
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.example.delightcart.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Activity.setupBottomSheetDialog(onSendClick: (String) -> Unit) {

    val dialog = BottomSheetDialog(this, R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog_layout, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val edEmail: EditText = view.findViewById(R.id.resetPasswordEditText)
    val buttonSend: AppCompatButton = view.findViewById(R.id.resetPasswordSendButton)
    val buttonCancel: AppCompatButton = view.findViewById(R.id.resetPasswordCancelButton)

    buttonSend.setOnClickListener {
        val email = edEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    buttonCancel.setOnClickListener {
        dialog.dismiss()
    }
}