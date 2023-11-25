# FooSH - A Framework for outcome-oriented Smart Homes

FooSH can be used to dock onto any existing smart home system and integrate arbitrary mechanisms for application domain value predictions.<br>
It provides a REST API that let's clients setup the framework, fetch a list of smart devices from the smart home, and initiate outcome-oriented requests for various environment variables.

## Prerequisities
- Java 17
- Maven
- Network connection
- Access to a smart home API

## Configuration
Before using FooSH, one needs to implement
- a class that inherits `AbstractDevice` to provide a device class tailored for the smart home in use,
- at least one class that inherits `AbstractPredictionModel` to provide prediction features for an environment variable, and
- and setup the interfaces `ISmartHomeDeviceFetcher` and `ISmartHomeInstructionExecutor`.

## Run
After implementing the necessary classes and functions, the application can be started by executing
```
mvn spring-boot:run
```
in the root folder of the project.

## Testing
To ensure the correctness of the framework, one can run both the unit and integration tests with
```
mvn test
```
Please keep in mind, that the integration tests will fail if no smart home is set up and connected.
