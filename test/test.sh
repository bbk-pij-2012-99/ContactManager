javac -cp ..:../lib/junit-4.10.jar ./*.java ../contactmanager/interfaces/*.java ../contactmanager/implementations/*.java
java -cp ..:../lib/junit-4.10.jar org.junit.runner.JUnitCore ContactTest
java -cp ..:../lib/junit-4.10.jar org.junit.runner.JUnitCore MeetingTest
java -cp ..:../lib/junit-4.10.jar org.junit.runner.JUnitCore ContactManagerAppTest