plugins {
    id("multiplatform-library-convention")
}

dependencies {
    commonMainImplementation(libs.io.github.ilyapavlovskii.kmm.koin)
    commonMainImplementation(libs.koin.core)
    commonMainImplementation(libs.mpp.kotlinx.coroutines)
}