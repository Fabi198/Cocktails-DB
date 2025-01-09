package com.example.username.cocktailsdbcompose.presentation.drawerMenu

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.username.cocktailsdbcompose.BuildConfig
import com.example.username.cocktailsdbcompose.R
import com.example.username.cocktailsdbcompose.navigation.AppScreens
import com.example.username.cocktailsdbcompose.presentation.dialogs.SimpleYNDialog
import com.example.username.cocktailsdbcompose.presentation.viewModel.DrawerMenuViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(drawerState: DrawerState?, viewModel: DrawerMenuViewModel = hiltViewModel(), navController: NavHostController, scope: CoroutineScope) {
    val showFastPreferences by viewModel.showFastPreferences.collectAsState()
    val languageList by viewModel.languageList.collectAsState()
    val savedLanguage by viewModel.savedLanguage.collectAsState()
    val authState by viewModel.authState.collectAsState(initial = false)
    val areResetFavoritesCocktails = rememberSaveable { mutableStateOf(false) }

    ConstraintLayout (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
            .fillMaxSize()
    ) {
        val (title, mainIcon, divider, glassSection, kindSection, catSection, letterSection, myAccountSection, resetFavorites, instructionsLanguage, divider2, googleBtn, preferencesBtn) = createRefs()
        val guidelineTop = createGuidelineFromTop(24.dp)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(title) {
                    top.linkTo(mainIcon.top)
                    bottom.linkTo(mainIcon.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            text = if (!showFastPreferences) "Buscar por filtros" else "Ajustes Rápidos",
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold, FontWeight.Bold)),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        IconButton(
            onClick = {
                scope.launch { drawerState?.close() }
                navController.navigate(route = AppScreens.MainScreen.route) {
                    popUpTo(AppScreens.MainScreen.route) { inclusive = true }
                }
            },
            modifier = Modifier
                .constrainAs(mainIcon) {
                    top.linkTo(guidelineTop)
                    start.linkTo(parent.start)
                }
        ) {
            Icon(
                imageVector = Icons.Outlined.Home,
                contentDescription = "Go To Home"
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .constrainAs(divider) {
                    top.linkTo(mainIcon.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        if (!showFastPreferences) {
            ExpandableSection(
                modifier = Modifier.constrainAs(glassSection) {
                    top.linkTo(divider.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                title = "Buscar por vaso",
                drawerState = drawerState,
                viewModel = viewModel
            ) {
                val stateGlassesDrawer by viewModel.stateGlassesDrawer.collectAsState()
                LazyColumn {
                    items(stateGlassesDrawer) {
                        SubItemRow(it.glassDTO) {
                            scope.launch { drawerState?.close() }
                            navController.navigate(AppScreens.SearchScreen.route + "/${it.glassDTO.replace(" ", "_")}" + "/1")
                        }
                    }
                }
            }
            ExpandableSection(
                modifier = Modifier.constrainAs(kindSection) {
                    top.linkTo(glassSection.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                title = "Buscar por tipo",
                drawerState = drawerState,
                viewModel = viewModel
            ) {
                val stateKindsDrawer by viewModel.stateKindsDrawer.collectAsState()
                LazyColumn {
                    items(stateKindsDrawer) {
                        SubItemRow(it.strAlcoholic) {
                            scope.launch { drawerState?.close() }
                            navController.navigate(AppScreens.SearchScreen.route + "/${it.strAlcoholic.replace(" ", "_")}" + "/2")
                        }
                    }
                }
            }
            ExpandableSection(
                modifier = Modifier.constrainAs(catSection) {
                    top.linkTo(kindSection.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                title = "Buscar por categoria",
                drawerState = drawerState,
                viewModel = viewModel
            ) {
                val stateCategoriesDrawer by viewModel.stateCategoriesDrawer.collectAsState()
                LazyColumn {
                    items(stateCategoriesDrawer) {
                        SubItemRow(it.strCategory) {
                            scope.launch { drawerState?.close() }
                            val safeString = when (it.strCategory) {
                                "Other / Unknown", "Coffee / Tea" -> { it.strCategory.replace(" / ", "%20#%20") }
                                "Punch / Party Drink" -> { "Punch%20#%20Party_Drink" }
                                else -> { it.strCategory.replace(" ", "_") }
                            }
                            navController.navigate(AppScreens.SearchScreen.route + "/$safeString" + "/3")
                        }
                    }
                }
            }
            ExpandableSection(
                modifier = Modifier.constrainAs(letterSection) {
                    top.linkTo(catSection.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                title = "Buscar por letra",
                drawerState = drawerState,
                viewModel = viewModel
            ) {
                val upperCaseAlphabet: List<Char> = listOf('A', 'B', 'C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')
                LazyRow(
                    modifier = Modifier.padding(start = 42.dp, end = 8.dp)
                ) {
                    items(upperCaseAlphabet) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    scope.launch { drawerState?.close() }
                                    navController.navigate(AppScreens.SearchScreen.route + "/$it" + "/4")
                                },
                            text = it.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold, FontWeight.Normal)),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
            if (authState) {
                ExpandableSection(
                    modifier = Modifier.constrainAs(myAccountSection) {
                        top.linkTo(letterSection.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    title = "Mi Cuenta",
                    drawerState = drawerState,
                    viewModel = viewModel
                ) {
                    LazyColumn {
                        item {
                            SubItemRow("Mis Cocteles") {
                                scope.launch { drawerState?.close() }
                                navController.navigate(AppScreens.SearchScreen.route + "/Guardados" + "/5")
                            }
                            SubItemRow("Historial") {
                                scope.launch { drawerState?.close() }
                                navController.navigate(AppScreens.SearchScreen.route + "/Recientes" + "/6")
                            }
                        }
                    }
                }
            }
        } else {
            if (authState) {
                val (text, icon) = createRefs()
                ConstraintLayout (
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(resetFavorites) {
                        top.linkTo(divider.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                        .clickable {
                            areResetFavoritesCocktails.value = true
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 32.dp)
                            .fillMaxWidth()
                            .constrainAs(text) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                            },
                        text = "Restablecer favoritos",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold, FontWeight.Bold)),
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Icon(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .constrainAs(icon) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                            },
                        painter = painterResource(R.drawable.baseline_restore_24),
                        contentDescription = null
                    )
                }
            }
            val (text, savedLanguageText) = createRefs()
            ConstraintLayout (
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(instructionsLanguage) {
                        if (authState) {
                            top.linkTo(resetFavorites.bottom)
                        } else {
                            top.linkTo(divider.bottom)
                        }
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                ExpandableSection(
                    modifier = Modifier.constrainAs(text) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    },
                    title = "Idioma de instrucciones",
                    drawerState = drawerState,
                    viewModel = viewModel
                ) {
                    LazyColumn {
                        items(languageList) {
                            SubItemRow(it) {
                                viewModel.changeLanguage(it)
                            }
                        }
                    }
                }
                Box(modifier = Modifier
                    .padding(end = 14.dp, top = 8.dp)
                    .constrainAs(savedLanguageText) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .border(1.dp, colorResource(R.color.orange), RoundedCornerShape(12.dp))
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        text = savedLanguage,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold, FontWeight.Bold)),
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

            }
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .constrainAs(divider2) {
                    bottom.linkTo(googleBtn.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        AuthenticationButton(modifier = Modifier
            .constrainAs(googleBtn) {
                bottom.linkTo(preferencesBtn.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(Brush.verticalGradient(colors = listOf(colorResource(R.color.graySuperDark), colorResource(R.color.grayDark)), 0.0f, 100.0f), RoundedCornerShape(12.dp))
            .border(1.dp, colorResource(R.color.orange), RoundedCornerShape(12.dp)),
            authState = authState, onGetCredentialResponse = { credential ->
                viewModel.onSignInWithGoogle(credential)
            }, onCloseSession = {
                viewModel.signOut()
            })
        Button (
            modifier = Modifier
                .constrainAs(preferencesBtn) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
                .fillMaxWidth()
                .padding(top = 4.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
                .background(Brush.verticalGradient(colors = listOf(colorResource(R.color.graySuperDark), colorResource(R.color.grayDark)), 0.0f, 100.0f), RoundedCornerShape(12.dp))
                .border(1.dp, colorResource(R.color.orange), RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
            onClick = {
                viewModel.changeShowFastPreferences()
            }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = if (!showFastPreferences) Icons.Filled.Settings else Icons.Filled.Search,
                    tint = colorResource(R.color.similYellow),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = if (!showFastPreferences) "Ajustes Rápidos" else "Buscar por filtros",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold, FontWeight.Normal)),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }

    if (areResetFavoritesCocktails.value) {
        SimpleYNDialog(
            title = "¿Desea resetear los favoritos?",
            btnYesText = "Sí",
            btnNoText = "No",
            onDismiss = {
                areResetFavoritesCocktails.value = false
            },
            onClickYes = {
                viewModel.onClickResetFavoritesCocktails()
                areResetFavoritesCocktails.value = false
                scope.launch { drawerState?.close() }
                navController.navigate(route = AppScreens.MainScreen.route) {
                    popUpTo(AppScreens.MainScreen.route) { inclusive = true }
                }
            },
            onClickNo = {
                areResetFavoritesCocktails.value = false
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationButton(modifier: Modifier, authState: Boolean, onGetCredentialResponse: (Credential) -> Unit, onCloseSession: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)
    val areClosingSession = rememberSaveable { mutableStateOf(false) }

    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
        onClick = {
            if (!authState) {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                coroutineScope.launch {
                    try {
                        val result = credentialManager.getCredential(request = request, context = context)
                        onGetCredentialResponse(result.credential)
                    } catch (e: Exception) {
                        Log.i("rtef", e.message.toString())
                    }
                }
            } else {
                areClosingSession.value = true
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.google_icon),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = if (!authState) "Acceder con Google" else "Cerrar sesión",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold, FontWeight.Normal)),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }

    if (areClosingSession.value) {
        SimpleYNDialog(
            title = "¿Desea cerrar sesión?",
            btnYesText = "Sí",
            btnNoText = "No",
            onDismiss = {
                areClosingSession.value = false
            },
            onClickYes = {
                onCloseSession()
                areClosingSession.value = false
            },
            onClickNo = {
                areClosingSession.value = false
            }
        )
    }
}

@Composable
fun SubItemRow(subtitle: String, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        val resourceImage = when (subtitle) {
            "Cocktail glass" -> {
                R.drawable.glass_highball
            }

            "Highball glass" -> {
                R.drawable.glass_cocktail
            }

            "Old-fashioned glass" -> {
                R.drawable.glass_old_fashioned
            }

            "Whiskey Glass" -> {
                R.drawable.glass_whiskey
            }

            "Collins glass" -> {
                R.drawable.glass_collins
            }

            "Pousse cafe glass" -> {
                R.drawable.glass_pousse_cafe
            }

            "Champagne flute" -> {
                R.drawable.glass_champagne_flute
            }

            "Whiskey sour glass" -> {
                R.drawable.glass_whiskey_sour
            }

            "Cordial glass" -> {
                R.drawable.glass_cordial
            }

            "Brandy snifter" -> {
                R.drawable.glass_snifter
            }

            "White wine glass" -> {
                R.drawable.glass_white_wine
            }

            "Hurricane glass" -> {
                R.drawable.glass_hurricane
            }

            "Shot glass" -> {
                R.drawable.glass_shot
            }

            "Irish coffee cup" -> {
                R.drawable.glass_irish_beer_mug
            }

            "Pint glass" -> {
                R.drawable.glass_pint
            }

            "Wine Glass" -> {
                R.drawable.glass_white_wine
            }

            "Beer mug" -> {
                R.drawable.glass_beer_mug
            }

            "Margarita/Coupette glass" -> {
                R.drawable.glass_whiskey_sour
            }

            "Beer pilsner" -> {
                R.drawable.glass_pilsner_beer
            }

            "Margarita glass" -> {
                R.drawable.glass_whiskey_sour
            }

            "Martini Glass" -> {
                R.drawable.glass_cocktail
            }

            "Balloon Glass" -> {
                R.drawable.glass_balloon
            }

            "Coupe Glass" -> {
                R.drawable.glass_champagne_coupe
            }

            else -> {
                0
            }
        }

        if (resourceImage > 0) {
            Image(
                painter = painterResource(resourceImage),
                contentDescription = subtitle,
                modifier = Modifier
                    .padding(start = 30.dp)
                    .size(14.dp)
            )
        }
        Text(
            modifier = Modifier.padding(start = if (resourceImage > 0) 8.dp else 50.dp),
            text = subtitle,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold, FontWeight.Normal)),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun ExpandableSection(
    modifier: Modifier = Modifier,
    title: String,
    drawerState: DrawerState?,
    viewModel: DrawerMenuViewModel,
    content: @Composable () -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    if (drawerState?.isClosed == true) isExpanded = false
    Column(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
            .fillMaxWidth()
    ) {
        ExpandableSectionTitle(isExpanded = isExpanded, title = title, viewModel = viewModel)

        AnimatedVisibility(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onSecondaryContainer)
                .fillMaxWidth(),
            visible = isExpanded
        ) {
            content()
        }
    }
}

@Composable
fun ExpandableSectionTitle(modifier: Modifier = Modifier, isExpanded: Boolean, title: String, viewModel: DrawerMenuViewModel) {

    val icon = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown

    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            imageVector = icon,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary),
            contentDescription = "null"
        )
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.montserrat_bold, FontWeight.Bold)),
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimary
        )

        if (title == "Mi Cuenta") {
            AsyncImage(
                modifier = Modifier.padding(horizontal = 10.dp).size(24.dp).clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(viewModel.getProfilePhoto())
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}