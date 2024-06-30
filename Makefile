.PHONY: clean install build test resolve sam_invoke

clean:
	mvn clean

install:
	mvn install

test:
	mvn test

resolve:
	mvn dependency:resolve

build:
	mvn clean install -B

sam_build:
	sam build -t tradey-technical-analysis.yml --use-container

sam_invoke:
	sam local invoke tradeytechnicalanalysis -t tradey-technical-analysis.yml