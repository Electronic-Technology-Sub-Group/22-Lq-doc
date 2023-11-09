使用 Spring Boot 可以快速创建一个 Spring 应用程序，该应用程序可以直接运行、接入 Tomcat 或其他环境，自动配置（支持的）第三方库，不需要任何额外配置。

Spring Initializer 是 Spring 提供的 Spring 项目样板自动生成的工具，已集成到 Idea 等 IDE 中。可以通过 [Spring Initializr](https://start.spring.io/) 访问。生成的 gradle 项目通常包含以下内容：

```groovy
plugins {
	// ...
	id 'org.springframework.boot' version '3.2.0-SNAPSHOT'
	id 'io.spring.dependency-management' version '1.1.3'
}

repositories {
	mavenCentral()
	maven { url 'https://repo.spring.io/milestone' }
	maven { url 'https://repo.spring.io/snapshot' }
	// ...
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	// ...
}
```

在应用程序配置完成后，通过 `@SpringBootApplication` 标记应用程序主类，并在 main 函数中使用 `SpringApplication.run` 调用。

```kotlin
@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    SpringApplication.run(DemoApplication::class.java, *args)
}
```

或通过 `ClassPathXmlApplicationContext` 等通过 XML 直接创建 Context。

```kotlin
fun main() {
    val context = ClassPathXmlApplicationContext("hello.xml")
}
```
# REST App

一个 REST 服务器应用需要一个 `RestController` 类，使用 `@GetMapping` 指定路由

```kotlin
@SpringBootApplication
class RESTApplication

@RestController
class RESTControllerDemo {
    
    private val template = "Hello, %s!"
    private val counter = AtomicLong()
    
    @GetMapping("/")
    fun index() = "Greetings from Spring Boot!"
    
    @GetMapping("/greeting")
    fun Greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
        Greeting(counter.incrementAndGet(), String.format(template, name))
}

data class Greeting(val id: Long, val content: String)

fun main() {
    SpringApplication.run(RESTApplication::class.java)
}
```

