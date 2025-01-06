package com.example.username.cocktailsdbcompose.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import com.example.username.cocktailsdbcompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleYNDialog(title: String, btnYesText: String, btnNoText: String, onDismiss: () -> Unit, onClickYes: () -> Unit, onClickNo: () -> Unit) {
    BasicAlertDialog(
    onDismissRequest = {
        onDismiss()
    }
    ) {
        ConstraintLayout (modifier = Modifier
            .size(height = 100.dp, width = 50.dp)
            .background(colorResource(R.color.graySuperDark), RoundedCornerShape(12.dp))
            .border(1.dp, colorResource(R.color.orange), RoundedCornerShape(12.dp))
        ) {
            val (titleDialog, yes, no) = createRefs()

            Text(
                modifier = Modifier.constrainAs(titleDialog) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                    .padding(top = 10.dp),
                text = title,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 5,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    onClickYes()
                },
                modifier = Modifier.constrainAs(yes) {
                    top.linkTo(titleDialog.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(no.start)
                    bottom.linkTo(parent.bottom)
                }
                    .border(1.dp, colorResource(R.color.orange), RoundedCornerShape(12.dp))
                    .width(90.dp)
                    .height(30.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Transparent,
                    containerColor = Color.Transparent
                )

            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = btnYesText,
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    onClickNo()
                },
                modifier = Modifier.constrainAs(no) {
                    top.linkTo(titleDialog.bottom)
                    start.linkTo(yes.end)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                    .border(1.dp, colorResource(R.color.orange), RoundedCornerShape(12.dp))
                    .width(90.dp)
                    .height(30.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Transparent,
                    containerColor = Color.Transparent
                )

            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = btnNoText,
                    color = Color.White,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}