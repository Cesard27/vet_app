package com.componentes.vet_app.view.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = buttonSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = buttonPrimary

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Vet_appTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val gradientBackground = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val biggerDimension = maxOf(size.height, size.width)
        return RadialGradientShader(
            colors = listOf(backgroundSecondary, backgroundPrimary),
            center = size.center,
            radius = biggerDimension/0.7f,
            colorStops = listOf(0f, 0.95f)
        )
    }
}

val gradientButtonNav = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val biggerDimension = maxOf(size.height, size.width)
        return RadialGradientShader(
            colors = listOf(buttonSecondary, buttonPrimary),
            center = size.center,
            radius = biggerDimension/0.9f,
            colorStops = listOf(0f, 0.95f)
        )
    }
}

val gradientButtonDiag = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val biggerDimension = maxOf(size.height, size.width)
        return RadialGradientShader(
            colors = listOf(accentPrimary, whiteAccent),
            center = size.center,
            radius = biggerDimension/0.4f,
            colorStops = listOf(0f, 0.95f)
        )
    }
}

@Composable
fun ButtonSaveComponent(column: Int,text: String, white: Boolean, value: String, navController: NavController, onClick: ()->Unit){
    var modifier: Modifier = Modifier
    if (column == 1){
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
    }else if(column == 2){
        modifier = Modifier
            .width(180.dp)
            .heightIn(48.dp)
    }
    Button(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = gradientButtonNav,
                    shape = RoundedCornerShape(25.dp)
                ),
            contentAlignment = Alignment.Center
        ){
            textContent(text = text, white)
        }
    }
}
@Composable
fun navButton(navController: NavController, route: String, column: Int, text: String, white: Boolean){

    var modifier: Modifier = Modifier
    if (column == 1){
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
    }else if(column == 2){
        modifier = Modifier
            .width(180.dp)
            .heightIn(48.dp)
    }
    Button(
        onClick = {
            navController.navigate( route = route){
                popUpTo(route){
                    inclusive = true
                }
            }
        },
        modifier = modifier,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = gradientButtonNav,
                    shape = RoundedCornerShape(25.dp)
                ),
            contentAlignment = Alignment.Center
        ){
            textContent(text = text, white)
        }
    }
}

@Composable
fun customButton(text: String, white: Boolean, onClick: () -> Unit){
    Button(
        onClick = onClick, // Aquí se especifica la acción a realizar cuando se hace clic en el botón
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = gradientButtonDiag,
                    shape = RoundedCornerShape(25.dp)
                ),
            contentAlignment = Alignment.Center
        ){
            textContent(text = text, white)
        }
    }
}



// Components for text
@Composable
fun textTitle(text: String, white: Boolean){
    Text(
        text = text,
        style = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            fontSize = 20.sp,
            letterSpacing = 0.sp,
            lineHeight = 27.sp,
            color = colorSelection(white)
        ))
}

@Composable
fun textContent(text: String, white: Boolean){
    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.sp,
            lineHeight = 27.sp,
            color = colorSelection(white)
        ))
}

@Composable
fun textSpecs(text: String, white: Boolean){
    Text(
        text = text,
        style = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Normal,
            fontSize = 12.sp,
            letterSpacing = 0.sp,
            lineHeight = 27.sp,
            color = colorSelection(white)
        ))
}

fun colorSelection(white: Boolean) : Color{
    if (white){
        return whiteAccent

    }else{
        return Color(0xFF000000)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun dropDownMenu(
    list : List<String> = listOf("op4","op3","op2","op1"),
    selectedValue: String,
    onValueChange: (String) -> Unit
){

    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {isExpanded = !isExpanded},
    ){
        TextField(
            modifier = Modifier.menuAnchor(),
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            textStyle = TextStyle(fontFamily = Roboto),
            shape = RoundedCornerShape(50.dp)
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {isExpanded = false},
            modifier = Modifier.background(accentPrimary)) {
            list.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = { textSpecs(text = text, true) },
                    onClick = {
                        onValueChange(list[index])
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }

}

@Composable
fun customTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue(value)) }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it.text)
        },
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier.fillMaxWidth()
    )
}


//@Composable
//fun SingleSelectDialog() {
//    val radioOptions = listOf("op1", "op2", "op3")
//    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        radioOptions.forEach { text ->
//            Row(
//                Modifier
//                    .fillMaxWidth()
//                    .selectable(
//                        selected = (text == selectedOption),
//                        onClick = {
//                            onOptionSelected(text)
//                        }
//                    )
//                    .padding(vertical = 8.dp)
//            ){
//                RadioButton(
//                    selected = (text == selectedOption),
//                    onClick = {onOptionSelected(text)}
//                )
//                textContent(text, false)
//            }
//        }
//    }
//}
//
//@Composable
//fun showSingleSelectDialog(
//    title: String?,
//    state: MutableState<Boolean>,
//    content: @Composable (() -> Unit)? = null
//){
//    AlertDialog(
//        onDismissRequest = {
//            state.value = false
//        },
//        title = title?.let {
//            {
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    textContent(text = title, false)
//                    Divider(modifier = Modifier.padding(bottom = 8.dp))
//                }
//            }
//        },
//        text = content,
//        dismissButton = {
//            TextButton(onClick = {state.value = false}){
//                textContent("Cancel", false)
//            }
//        },
//        confirmButton = {
//            Button(
//                onClick = {state.value = false},
//                modifier = Modifier.width(80.dp).heightIn(48.dp),
//                contentPadding = PaddingValues(),
//                colors = ButtonDefaults.buttonColors(Color.Transparent)){
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(accentPrimary)
//                        .heightIn(48.dp),
//                        contentAlignment = Alignment.Center) {
//
//                    textContent("Ok", true)
//                }
//            }
//        },
//        modifier = Modifier.padding(vertical = 8.dp)
//    )
//}
//
//@Composable
//fun accentSelectedButton(){
//    val showDialog = remember { mutableStateOf(false) }
//    val title = remember { mutableStateOf("no Filter") }
//
//    Button(
//        onClick = { showDialog.value = true },
//        modifier = Modifier.width(250.dp).heightIn(48.dp),
//        contentPadding = PaddingValues(),
//        colors = ButtonDefaults.buttonColors(Color.Transparent)
//    ){
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .heightIn(48.dp)
//                .background(
//                    brush = gradientButtonDiag,
//                    shape = RoundedCornerShape(10.dp)
//                ),
//            contentAlignment = Alignment.Center
//        ){
//            textContent("$title", true)
//        }
//    }
//
//    if (showDialog.value) {
//        showSingleSelectDialog(state = showDialog, title = "$title", content = { SingleSelectDialog() })
//    }
//}