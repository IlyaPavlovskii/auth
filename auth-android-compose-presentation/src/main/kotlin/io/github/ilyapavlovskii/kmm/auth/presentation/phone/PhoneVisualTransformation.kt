package io.github.ilyapavlovskii.kmm.auth.presentation.phone

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

internal object PhoneVisualTransformation : VisualTransformation {
    private const val FORMAT_PATTERN = "+%s"
    override fun filter(text: AnnotatedString): TransformedText {
        val formattedText = FORMAT_PATTERN.format(text)
        return TransformedText(AnnotatedString(formattedText), object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formattedText.length
            override fun transformedToOriginal(offset: Int): Int = text.length
        })
    }
}