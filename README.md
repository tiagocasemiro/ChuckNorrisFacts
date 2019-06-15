
<img alt="Chuck Norris" src="./header.jpeg" style="width: 100%;">

[![Travis CI badge](https://img.shields.io/travis/tiagocasemiro/ChuckNorrisFacts.svg?label=Travis%20CI&style=flat)](https://codecov.io/gh/tiagocasemiro/ChuckNorrisFacts)&nbsp;&nbsp;
[![Codecov badge](https://img.shields.io/codecov/c/github/tiagocasemiro/ChuckNorrisFacts.svg?style=flat)](https://codecov.io/gh/tiagocasemiro/ChuckNorrisFacts)&nbsp;&nbsp;
[![codebeat badge](https://codebeat.co/badges/712156af-dacf-4291-aab1-78b4e498cd75)](https://codebeat.co/projects/github-com-tiagocasemiro-chucknorrisfacts-feature-create-project)

# Chuck Norris Facts  

#### DevOps integrations

**codebeat** - Coder analyzer <BR/>
<https://codebeat.co/projects/github-com-tiagocasemiro-chucknorrisfacts-feature-create-project>

**codecov** - Coverage dashboard<BR/>
<https://codecov.io/gh/tiagocasemiro/ChuckNorrisFacts>

**travis CI** - Continuos integration<BR/>
<https://travis-ci.com/tiagocasemiro/ChuckNorrisFacts>

**Gradle scans** - Build analyzer<BR/>
<https://scans.gradle.com/s/2qu66ijnw4vpm>

**Crashilytics** - Crash report<BR/>
<https://fabric.io/tiago-casemiros-projects/android/apps/com.chucknorrisfacts/dashboard>

<BR/> 
<BR/> 

#### Architecture

**Base architecture ( Model View Controller )**<BR/>
The base architecture of the application is the MVC, packet-segmented.<BR/>

Possessing 3 composite modules as follows. **App** contains **Domain** and **MOC** contains **Domain**.<BR/>
In the context of functional testing the **App** module contains the **Mock** module to have the ability to generate test jsons.<BR/>

All tests are independent of the external environment, including the integration tests.<BR/>
In order to make the integration tests independent, the Room was instantiated with save mode in memory, without persistence on disk. The retrofit was added an interceptor in the last processing layer to respond the requests with mocks.<BR/>

<BR/>
<BR/> 

#### Tests and Quality code

All tests were applied without interference from the external environment (App database on smartphone, Application server running), as explained above.<BR/>

The entire **commit / pull request** in the Master branche will execute all the tests of the application.<BR/>
When a **commit / pull request** changes the code coverage scenario, a bot comments with an updated coverage report.<BR/>

To run the tests manually, follow these steps on the terminal<BR/>
```sh
$ ./gradlew test
$ ./gradlew build jacocoTestReport
```

To manually upload test results.<BR/>
```sh
$ bash <(curl -s https://codecov.io/bash) -t  82b133d4-3371-4d9a-b720-845121f5ea5d
```

A code quality analysis is done at each commit / pull request in the master. The application's GPA is 3.73 of 4.0 <BR/>

Look at other famous open source projects, [https://codebeat.co/open-source/](https://codebeat.co/open-source/)<BR/>

Learn more about GPA, [https://hub.codebeat.co/docs/gpa-explained](https://hub.codebeat.co/docs/gpa-explained)<BR/>
<BR/> 
<BR/> 

#### Stack
+ **Koin** - Injection dependency
+ **Roon** - Orm local repository
+ **Retrofit** - Rest api client
+ **Kotlin corotines** - Threads manager
+ **Navegation fragment** - Navegation ui 
+ **Android View Animations** -  Animations
+ **Crashlytics** - Crash report
+ **Material design components** - Components ui
+ **Androidx emoj** -Emojs 😀
<BR/> 
<BR/> 

#### Build

+ Minimum version of Android is 21
+ Gradle plugin version 3.4.1
+ Gradle version 5.1.1
+ Jre version 1.8.0_152-release-1343-b01
+ Android Studio 3.4.1
+ Kotlin version 1.3.31

If you use the local mock. When starting the server, the ip of the created server will be printed on the console.<BR/>
Copy this ip and put in the String HOST configured in the gradle of the App Mole.<BR/>
```
// Example: 
buildConfigField("String", "HOST", "http:// <<<Enter here your ip>>>: 8080")
```