import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    `maven-publish`

}

android {
    namespace = "com.za.addresssuggestion"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.okhttp.logging)

    //test
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutinesTest)
}

val githubProps = Properties()
val localFile = file("${rootDir}/gradle-local.properties")

localFile.takeIf { it.exists() }?.inputStream()?.use {
    githubProps.load(it)
}

val githubUser: String? = githubProps["GITHUB_USER"] as String?
val githubToken: String? = githubProps["GITHUB_TOKEN"] as String?



afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.za"
                artifactId = "address-suggestion"
                version = "1.0.0"

                from(components["release"])

                pom {
                    name.set("Address Suggestion")
                    description.set("A lightweight Android library for French address autocompletion")
                    url.set("https://github.com/ahmedzer/fr-address-suggestion")
                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    developers {
                        developer {
                            id.set("za")
                            name.set("Ahmed Zerrouk")
                            email.set("ahmedzer59@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/ahmedzer/fr-address-suggestion.git")
                        developerConnection.set("scm:git:ssh://github.com/ahmedzer/fr-address-suggestion.git")
                        url.set("https://github.com/ahmedzer/fr-address-suggestion")
                    }
                }
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/ahmedzer/fr-address-suggestion")
                credentials {
                    username = githubUser
                    password = githubToken
                }
            }
        }
    }
}