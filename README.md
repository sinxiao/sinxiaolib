# sinxiaolib

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

#### 这是一个比较完善的基于MVP的Android框架。集成了Http请求和WebSocket请求。封装了大部分开发中需要用到的函数和方法，方便提高开发效率。
#### 没有集成MVVM框架和使用kotin。对于初创团队（没钱，工资低，招到的人能力也参差不齐），人员构成有些复杂，便于开发和推进。提高效率。

###  欢迎各类小白，学习和交流。


