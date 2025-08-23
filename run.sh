javac -cp "lib/*" -d bin src/frames/*.java src/controller/*.java
java -cp bin:images:data controller.Main
