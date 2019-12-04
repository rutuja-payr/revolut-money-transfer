## Money Transfer
Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.

#### Technology used
- Maven : Building project
- Java 8 : programming language 
- JaCoCo : code coverage report
- Groovy using Spock : Unit testing
- Spark : Api Programming
- H2 : In-memory DB
 
#### Steps to run the project

> Prerequisite
- Maven 
- Java 8 or higher 

> To install dependencies

    mvn clean install 
    
> To compile unit test cases 

    mvn test-compile

> To run test

    mvn test    
    
#### Architectural points and Terminologies used in the project

Spark is a free and open-source software web application framework and domain-specific language written in Java

JUnit (version 4.12) is a framework that allows us to write both unit and integration tests.

Spock Core (version 1.0-groovy-2.4). Spock is a testing and specification framework for Java and Groovy applications.

Groovy All (version 2.4.4). Groovy is a dynamic programming language for the JVM.

The JaCoCo code coverage report will be generated at target/site/jacoco/*


> Comments in code 

Entire code styling is influenced by Clean Code principle - Robert Martin
Which says
'Truth can only be found in one place: the code’.
So you may not found any comments anywhere in the project.
Keeping in mind that git can be used to versioning of file and method, class names should be kept as self explanatory.

However, if you need comments on each file. I can do that too.

> Design principles used in Project :

- SOLID (single responsibility, open-closed, Liskov subsitution, interface segragation, dependency inversion) principle
- Composition over inheritance
- DRY(Don’t repeat yourself)
- KISS(Keep it simple, stupid)
- Event based programming
- and some experience principle ;)   
