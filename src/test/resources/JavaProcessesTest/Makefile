all: helloworld.jar

helloworld.jar: MANIFEST.MF HelloWorld.class
	jar cfm $@ $+

HelloWorld.class: HelloWorld.java
	javac $+

clean:
	rm -f HelloWorld.class
	rm -f helloworld.jar
