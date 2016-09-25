all: src/view/Manning.class
%.class: %.java
	javac -Xlint:unchecked -d bin -cp lib/json-simple-1.1.1.jar:src/ $<
clean:
	find bin -iname *.class | xargs rm -f
run:
	java -cp .:bin/:lib/json-simple-1.1.1.jar view/Manning
