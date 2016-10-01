all: src/view/Manning.class
%.class: %.java
	mkdir -p bin
	javac -Xlint:unchecked -d bin -cp lib/json-simple-1.1.1.jar:lib/jline-2.12.jar:src/ $<
jar:
	make
	jar -cfm manning.jar manifest.txt -C bin .
clean:
	find bin -iname *.class | xargs rm -f
run:
	java -cp .:bin/:lib/json-simple-1.1.1.jar:lib/jline-2.12.jar view/Manning $(args)
