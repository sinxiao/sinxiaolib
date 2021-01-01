# sinxiaolib

# [![](https://jitpack.io/v/sinxiao/sinxiaolib.svg)](https://jitpack.io/#sinxiao/sinxiaolib)

## To get a Git project into your build:

### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
 ```
  
###  Step 2. Add the dependency
  
  ```
  dependencies {
	        implementation 'com.github.sinxiao:sinxiaolib:1.1.0'
	}
  ```
  
### That's it! The first time you request a project JitPack checks out the code, builds it and serves the build artifacts (jar, aar).

#### 主工程(App)需要依赖以下依赖。
```

    implementation 'org.apache.commons:commons-lang3:3.9'
    implementation 'org.apache.commons:commons-collections4:4.3'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.12.0'
    implementation 'com.fasterxml.jackson.core:jackson-core:2.12.0'
    implementation 'com.fasterxml.jackson.core:jackson-annotations:2.12.0'

    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'androidx.appcompat:appcompat:1.0.0'
    implementation 'androidx.recyclerview:recyclerview:1.0.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'

    implementation 'androidx.multidex:multidex:2.0.0'
    //协程
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.4'
    // alternatively, just ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
    implementation "androidx.core:core-ktx:+"
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"


    implementation('com.squareup.okhttp3:okhttp:4.8.0')
    implementation 'com.squareup.okhttp3:logging-interceptor:4.10.0-RC1'
    implementation('com.wang.avi:library:2.1.3')
    def room_version = "2.2.5"

    annotationProcessor "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-runtime:$room_version"

```

#### 这是一个比较完善的基于MVP的Android框架。集成了Http请求和WebSocket请求。封装了大部分开发中需要用到的函数和方法，方便提高开发效率。
#### 没有集成MVVM框架和使用kotin。对于初创团队（没钱，工资低，招到的人能力也参差不齐），人员构成有些复杂，便于开发和推进。提高效率。

###  欢迎各类编程爱好者，学习和交流。


