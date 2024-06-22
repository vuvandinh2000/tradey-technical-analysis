# tradey-aws


## Pre-commit
```shell
sam build --template tradey-market-info.yml --use-container
```

## Invoke with event
```shell
sam local invoke HelloWorldFunction -e events/event.json
```

## Guide
- Download and install dependencies from `pom.yml`
```shell
mvn dependency:resolve
```
- Compile source, run tests
```shell
mvn clean install
```