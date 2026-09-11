package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()

        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        onDeleteCity = { cityRepository.deleteCity(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

class CityRepository {
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow",
        "Sydney", "Berlin", "Vienna", "Tokyo",
        "Beijing", "Osaka", "New Delhi"
    )

    val cities: List<String>
        get() = _cities

    fun addCity(city: String) {
        _cities.add(city)
    }

    fun deleteCity(city: String) {
        _cities.remove(city)
    }
}

@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf("")}
    var addingCity by remember { mutableStateOf(false)}

    fun selectedCity(city: String) {
        if (city == selected) selected = ""
        else selected = city
    }
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            // now only show up when we are adding city
            if (addingCity) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City Name") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                        // change the click effect to only make it so addingCity is true
                        addingCity = true
                        newCityName = ""
                }
            ) {
                Text("Add City")
            }

            if (addingCity) {
                Spacer(modifier = Modifier.width(8.dp))

                // move the adding city part after
                Button(
                    onClick = {
                        if (newCityName.isNotBlank()) {
                            onAddCity(newCityName)
                            newCityName = ""
                            // make sure addingCity returns to false
                            addingCity = false
                        }
                    }
                ) {
                    Text("Confirm")
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (selected.isNotBlank()){
                        onDeleteCity(selected)
                        selected = ""
                    }
                }
            ) {
                Text("Delete City")
            }
        }
        Text("Selected city $selected")
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(cities) { city ->
                CityRow(city = city, selected=::selectedCity)
            }
        }
    }
}

@Composable
fun CityRow(city: String, selected : (String) -> Unit){
    var selectedRow by remember { mutableStateOf(false)  }
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .selectable(
                selected = selectedRow,
                onClick = {
                    selectedRow = !selectedRow
                    selected(city)
                }
            )
    )
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListyCityTheme {
        Greeting("Android")
    }
}