package io.astronout.dicodingstoryapp.customviews

import android.content.Context
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.utils.setDrawable

class EmailEditText: AppCompatEditText {

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
        hint = context.getString(R.string.label_email)
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        setTextAppearance(R.style.StoryAppTypographyStyles_Body1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setAutofillHints(AUTOFILL_HINT_EMAIL_ADDRESS)
        }
        setDrawable(start = ContextCompat.getDrawable(context, R.drawable.ic_baseline_email_24))
        compoundDrawablePadding = 16

        doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                error = context.getString(R.string.error_email_not_valid)
                isValid = false
            } else {
                isValid = true
            }
        }
    }
}