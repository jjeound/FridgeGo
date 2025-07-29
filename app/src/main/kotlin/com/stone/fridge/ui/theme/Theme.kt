package com.stone.fridge.ui.theme

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
import androidx.compose.ui.unit.sp
import com.stone.fridge.R

@Immutable
data class CustomColors(
    val iconSelected: Color,
    val iconDefault: Color,
    val iconPrimary: Color,
    val iconRed: Color,
    val primary: Color,
    val onPrimary: Color,
    val surface: Color,
    val onSurface: Color,
    val border: Color,
    val borderLight : Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val error: Color,
    val buttonSurface: Color,
    val buttonBorderUnfocused: Color,
    val buttonBorderFocused: Color,
    val textFieldSurface: Color,
    val textFieldBorder: Color,
    val placeholder: Color,
)

val LightCustomColors = CustomColors(
    iconSelected = Black,
    iconDefault = Grey300,
    iconPrimary = Blue100,
    iconRed = Red,
    primary = Blue100,
    onPrimary = White,
    surface = Grey500,
    onSurface = White,
    border = Grey300,
    borderLight = Grey400,
    textPrimary = Black,
    textSecondary = Grey300,
    textTertiary = Grey400,
    error = Red,
    buttonSurface = White,
    buttonBorderUnfocused = Grey400,
    buttonBorderFocused = Black,
    textFieldSurface = White,
    textFieldBorder = Black,
    placeholder = Grey300,
)
val DarkCustomColors = CustomColors(
    iconSelected = White,
    iconDefault = Grey300,
    iconPrimary = Blue100,
    iconRed = Red,
    primary = Blue100,
    onPrimary = White,
    surface = Black100,
    onSurface = Black200,
    border = Grey300,
    borderLight = Grey100,
    textPrimary = White,
    textSecondary = Grey300,
    textTertiary = Grey400,
    error = Red,
    buttonSurface = Black,
    buttonBorderUnfocused = Grey300,
    buttonBorderFocused = White,
    textFieldSurface = Black200,
    textFieldBorder = White,
    placeholder = Grey300,
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
    title1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp),
    title2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp),
    body1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    body2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    body3 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp),
    button1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp),
    button2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = 13.sp),
    caption1 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp),
    caption2 = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 10.sp)
)

val LocalCustomColors = staticCompositionLocalOf {
    LightCustomColors
}
val LocalCustomTypography = staticCompositionLocalOf {
    customTypography
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
}



