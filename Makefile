.PHONY: clean install test resolve

clean:
	mvn clean

install:
	mvn install

test:
	mvn test

resolve:
	mvn dependency:resolve