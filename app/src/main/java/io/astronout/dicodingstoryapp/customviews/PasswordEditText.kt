package io.astronout.dicodingstoryapp.customviews

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.utils.setDrawable

class PasswordEditText : AppCompatEditText {

    var isValid = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        hint = context.getString(R.string.label_password)
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        setTextAppearance(R.style.StoryAppTypographyStyles_Body1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(AUTOFILL_HINT_PASSWORD)
        }
        setDrawable(start = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24))
        compoundDrawablePadding = 16

        doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty() && text.toString().length < 8) {
                error = context.getString(R.string.error_password_not_valid)
                isValid = false
            } else {
                isValid = true
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        transformationMethod = PasswordTransformationMethod.getInstance()
    }

}