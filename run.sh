javac -d bin -cp "lib/json.jar" src/frames/*.java src/main/*.java
java -cp bin:images:data main.Main
