plugins {
    id("multiplatform-library-convention")
}

dependencies {
    androidMainImplementation(libs.mpp.kotlinx.coroutines)
    androidMainImplementation(libs.firebase.auth)

    commonMainImplementation(libs.io.github.ilyapavlovskii.kmm.koin)
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.firebase.auth)
    commonMainImplementation(libs.mpp.kotlinx.coroutines)
    commonMainImplementation(projects.common)
}