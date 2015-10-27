
SHELL := /bin/bash

all: compile
	printf '\nBuild complete. Run with:\n$$ make run\n\n';

compile:
	mkdir -p build
	javac -d build -cp .:app/resources/ojdbc7.jar app/src/com/cmput291/rhanders_abradsha_dshin/*.java;

clean:
	rm -rf build;

run:
	java -cp build:app/resources/ojdbc7.jar com.cmput291.rhanders_abradsha_dshin.Main
