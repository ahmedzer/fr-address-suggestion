# 🇫🇷 Address Suggestion (France)

A lightweight Android library for **French address autocompletion**  
powered by the official French government API.

➡️ [https://api-adresse.data.gouv.fr](https://api-adresse.data.gouv.fr)

---

## ✨ Features

- 🇫🇷 France-only address suggestions
- ⚡ Fast autocomplete
- 🧩 Jetpack Compose ready
- 🔌 No API key required
- 📦 Easy to integrate

---

## 📦 Installation & Usage

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/ahmedzer/fr-address-suggestion")
            credentials {
                username = "YOUR_GITHUB_USERNAME"
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
// Gradle installation
dependencies {
    implementation("com.za:address-suggestion:1.0.0")
}

// --- Usage Example ---

// 1️⃣ Create a ViewModel (app-side)
class AddressViewModel : ViewModel() {

    private val autocomplete = AddressAutocomplete()

    val suggestions = mutableStateListOf<AddressSuggestion>()

    fun search(query: String) {
        viewModelScope.launch {
            suggestions.clear()
            suggestions.addAll(
                autocomplete.getSuggestions(query)
            )
        }
    }
}

// 2️⃣ Use it in a Jetpack Compose screen
@Composable
fun UserInfoPageContent(
    uiState: AccountCreationUiState,
    onValueChange: (field: String, value: String) -> Unit,
    viewModel: AddressViewModel = viewModel()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            AddressDropdownField(
                value = uiState.ownerAddress,
                onValueChange = {
                    onValueChange("ownerAddress", it)
                    viewModel.search(it)
                },
                suggestions = viewModel.suggestions,
                onSelect = { selected ->
                    onValueChange("ownerAddress", selected.label)
                    onValueChange("ownerCity", selected.city)
                    onValueChange("ownerPostalCode", selected.postalCode)
                },
                inputField = { value, onChange ->
                    TextField(
                        value = value,
                        onValueChange = onChange,
                        label = { Text("Adresse") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    }
}

// Notes:
// - The library is France-only
// - Public API, no API key required
// - Debounce input (~300ms) to avoid excessive API calls
// - ViewModel/state management is handled by the app
// - Compose UI is stateless and fully customizable