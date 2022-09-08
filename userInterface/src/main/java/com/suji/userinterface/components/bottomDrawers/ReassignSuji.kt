package com.suji.userinterface.components.bottomDrawers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.suji.domain.model.Athlete
import com.suji.sujitechnicalexercise.ui.theme.TextGrey
import com.suji.userinterface.R
import com.suji.userinterface.components.animations.animatedShimmer
import com.suji.userinterface.theme.dimensions

@Composable
fun ReassignSuji(
    oldAthlete: Athlete,
    newAthlete: Athlete,
    enabled : Boolean,
    reassign : () -> Unit
) {
    val colors = MaterialTheme.colors
    Column(
        Modifier
            .background(colors.background)
            .fillMaxWidth()
    ) {
        Text(
            "Reassign Athletes",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(12.dp)
        )
        Row(){
            Column(
                modifier = Modifier
                    .weight(0.45f)
                    .height(139.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                AsyncImage(
                    model = newAthlete.institutionLogoUrl,
                    contentDescription = "athlete",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(75.dp)
                        .padding(dimensions.medium)
                        .clip(CircleShape)
                        .background(animatedShimmer())
                )
                Text(
                    text = newAthlete.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.h6
                )
            }

            Column(
                modifier = Modifier
                    .weight(0.1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.data_exchange),
                        contentDescription = "Exchange",
                        tint = TextGrey
                    )
                }

            }

            Column(
                modifier = Modifier
                    .weight(0.45f)
                    .height(139.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                AsyncImage(
                    model = oldAthlete.institutionLogoUrl,
                    contentDescription = "athlete",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(75.dp)
                        .padding(dimensions.medium)
                        .clip(CircleShape)
                        .background(animatedShimmer())
                )
                Text(
                    text = oldAthlete.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.h6
                )
            }
        }


            Button(
                onClick = {reassign()},
                modifier = Modifier.fillMaxWidth().padding(dimensions.small),
                colors = ButtonDefaults.buttonColors(backgroundColor = colors.primary),
                enabled = enabled
            )
            {
                Text(
                    "Reassign Suji Device",
                    Modifier.padding(dimensions.small)
                )
            }


    }
}