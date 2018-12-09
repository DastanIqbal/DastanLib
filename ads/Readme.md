### Setup

##### app build.gradle
```
buildscript {
    repositories {
        maven { url 'https://maven.fabric.io/public' }
    }

    dependencies {
        classpath 'io.fabric.tools:gradle:1.+'
    }
}

apply plugin: 'io.fabric'

repositories {
    mavenCentral()
    maven { url 'https://maven.fabric.io/public' }
}

android{
    compileOptions {
        sourceCompatibility 1.8
        targetCompatibility 1.8
    }
}

dependencies {
    implementation "com.dastanapps.dastanlib:ads:0.1-alpha"
}
```


#### project build.gradle
```
allprojects {
    repositories {
        maven {
            url 'https://jitpack.io'
        }
    }
}
```

#### extends App class to DastanAdsApp
```
class MyAPP : DastanAdsApp() {
    
}
```