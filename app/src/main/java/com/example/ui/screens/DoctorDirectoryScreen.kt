package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.mock.Doctor
import com.example.ui.components.RecommendDoctorCard
import com.example.ui.components.TopBarWithBackAndMore
import com.example.ui.state.DoctorAppUiState
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SlateSecondary

@Composable
fun DoctorDirectoryScreen(
  uiState: DoctorAppUiState,
  onSearchQueryChanged: (String) -> Unit,
  onSpecialtyToggled: (String) -> Unit,
  onClearFilters: () -> Unit,
  onDoctorClick: (Doctor) -> Unit,
  onBookNowClick: (Doctor) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  BackHandler { onBack() }

  LazyColumn(
    modifier = modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),
    contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item {
      Box(modifier = Modifier.padding(horizontal = 20.dp)) {
        TopBarWithBackAndMore(
          title = "All Specialists",
          onBack = onBack,
        )
      }
    }

    item {
      Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(52.dp),
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
      ) {
        Row(
          modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(Icons.Rounded.Search, contentDescription = null, tint = SlateSecondary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(10.dp))
          Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (uiState.searchQuery.isEmpty()) {
              Text(
                text = stringResource(R.string.search_doctor_hint),
                style = MaterialTheme.typography.bodyLarge,
                color = SlateSecondary,
              )
            }
            BasicTextField(
              value = uiState.searchQuery,
              onValueChange = onSearchQueryChanged,
              singleLine = true,
              textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
              cursorBrush = SolidColor(IndigoPrimary),
              modifier = Modifier.fillMaxWidth().testTag("directory_search_input"),
            )
          }
          if (uiState.searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchQueryChanged("") }, modifier = Modifier.size(32.dp)) {
              Icon(Icons.Rounded.Close, contentDescription = "Clear", tint = SlateSecondary)
            }
          }
        }
      }
    }

    item {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        item {
          FilterChip(
            selected = uiState.selectedSpecialtyId == null,
            onClick = onClearFilters,
            label = { Text("All") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = IndigoPrimary,
              selectedLabelColor = Color.White,
            ),
          )
        }
        items(uiState.specialties, key = { it.id }) { spec ->
          FilterChip(
            selected = uiState.selectedSpecialtyId == spec.id,
            onClick = { onSpecialtyToggled(spec.id) },
            label = { Text(spec.name) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = IndigoPrimary,
              selectedLabelColor = Color.White,
            ),
          )
        }
      }
    }

    items(uiState.filteredDoctors, key = { it.id }) { doctor ->
      Box(modifier = Modifier.padding(horizontal = 20.dp)) {
        RecommendDoctorCard(
          doctor = doctor,
          onCardClick = { onDoctorClick(doctor) },
          onVisitNowClick = { onBookNowClick(doctor) },
        )
      }
    }
  }
}
