package com.example.untitled_capstone.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.untitled_capstone.R

@Immutable
data class CustomColors(
    val iconSelected: Color,
    val iconDefault: Color,
    val iconRed: Color,
    val primary: Color,
    val onPrimary: Color,
    val surface: Color,
    val onSurface: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTeritary: Color,
    val error: Color,
)

val LightCustomColors = CustomColors(
    iconSelected = Black,
    iconDefault = Grey300,
    iconRed = Red,
    primary = Blue100,
    onPrimary = White,
    surface = Grey500,
    onSurface = White,
    border = Grey300,
    textPrimary = Black,
    textSecondary = Grey300,
    textTeritary = Grey400,
    error = Red
)
val DarkCustomColors = CustomColors(
    iconSelected = White,
    iconDefault = Grey300,
    iconRed = Red,
    primary = Blue100,
    onPrimary = White,
    surface = Black100,
    onSurface = Black200,
    border = Grey300,
    textPrimary = White,
    textSecondary = Grey300,
    textTeritary = Grey400,
    error = Red
)

val fontFamily = FontFamily(
    Font(R.font.pretendard_black, FontWeight.Black),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_thin, FontWeight.Thin),
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight),
)

val customTypography =  CustomTypography(
    headline1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp),
    headline2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp),
    headline3 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp),
    title1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp),
    title2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp),
    body1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    body2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    body3 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp),
    button1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp),
    button2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = 13.sp),
    caption1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp),
    caption2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 10.sp)
)

@Immutable
data class CustomElevation(
    val bgPadding : Dp,
    val itemPadiing: Dp
)

val LocalCustomColors = staticCompositionLocalOf {
    LightCustomColors
}
val LocalCustomTypography = staticCompositionLocalOf {
    customTypography
}

val LocalCustomElevation = staticCompositionLocalOf {
    CustomElevation(
        bgPadding = Dp.Unspecified,
        itemPadiing = Dp.Unspecified
    )
}


@Composable
fun Untitled_CapstoneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkCustomColors
    } else {
        LightCustomColors
    }

    val customTypography =  CustomTypography(
        headline1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp),
        headline2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp),
        headline3 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp),
        title1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp),
        title2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp),
        body1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp),
        body2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp),
        body3 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp),
        button1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp),
        button2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = 13.sp),
        caption1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp),
        caption2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 10.sp)
    )

    CompositionLocalProvider(
        LocalCustomColors provides colors,
        LocalCustomTypography provides customTypography,
        LocalCustomElevation provides  CustomElevation(
            bgPadding = 20.dp,
            itemPadiing = 10.dp
        ),
        content = content
    )
}

object CustomTheme {
    val colors: CustomColors
        @Composable
        get() = LocalCustomColors.current
    val typography: CustomTypography
        @Composable
        get() = LocalCustomTypography.current
    val elevation: CustomElevation
        @Composable
        get() = LocalCustomElevation.current
}



