# tradey-aws


## Pre-commit
```shell
sam build --template tradey-market-info.yml --use-container
```

## Invoke with event
```shell
sam local invoke HelloWorldFunction -e events/event.json
```
