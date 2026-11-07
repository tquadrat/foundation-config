This library provides the annotations for the definition of a *Configuration Bean Specification* that is the input to the generation of an *Configuration Bean*.

It also provides 

Refer also to [`foundation-config-ap`](https://tquadrat.github.io/foundation-config-ap/)

### Documentation

- [Javadoc Reference](https://tquadrat.github.io/foundation-config/javadoc/index.html)
- Maven POM
    ```xml
    <dependency>
      <groupId>org.tquadrat.library</groupId>
      <artifactId>org.tquadrat.foundation.config</artifactId>
      <version>0.25.15</version>
    </dependency>
    ```
- Gradle
    ```Groovy
    dependencies {
      …
  
      //---* The tquadrat configuration stuff *-------------------------------
      implementation 'org.tquadrat.library:org.tquadrat.foundation.config:0.25.15'
      annotationProcessor 'org.tquadrat.tool:org.tquadrat.foundation.config.ap:0.25.15'
      …
    }  //  dependencies
    ```
  The current version for the annotation processor can be different from that for this component.

---

Last updated: 2026-11-07T17:46:08.93624168+01:00[Europe/Berlin]