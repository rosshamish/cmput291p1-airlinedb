
all: compile jarup build-clean
	printf '\nJar built successfully.\nRun like: java -jar airlineDB-rhanders-abradsha-dshin.jar\n\n';

compile:
	pushd app/resources; jar xf ojdbc7.jar; popd;
	mkdir -p build/class
	javac -d build/class -cp app/resources/ app/src/com/cmput291/rhanders_abradsha_dshin/*.java;
	cp -r app/resources/oracle build/class
	pushd build/class; find . -name "*.class" > local.list; popd;

jarup:
	pushd build/class; jar cmf ../../META-INF/MANIFEST.MF airlineDB-rhanders-abradsha-dshin.jar \
		@local.list; cp airlineDB-rhanders-abradsha-dshin.jar ../../; popd;

build-clean:
	rm -rf app/resources/oracle;
	rm -rf app/resources/META-INF;
	rm -rf build;

clean: build-clean
	rm -f airlineDB-rhanders-abradsha-dshin.jar
