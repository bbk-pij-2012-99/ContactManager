javac -cp .:./lib/junit-4.10.jar ContactManager.java
javac -cp .:./lib/junit-4.10.jar Contact.java
javac -cp .:./lib/junit-4.10.jar Meeting.java
javac -cp .:./lib/junit-4.10.jar PastMeeting.java
javac -cp .:./lib/junit-4.10.jar FutureMeeting.java
javac -cp .:./lib/junit-4.10.jar ContactManagerImpl.java
javac -cp .:./lib/junit-4.10.jar ContactImpl.java
javac -cp .:./lib/junit-4.10.jar MeetingImpl.java
javac -cp .:./lib/junit-4.10.jar ContactTest.java
javac -cp .:./lib/junit-4.10.jar MeetingTest.java
# javac -cp .:./lib/junit-4.10.jar ContactManagerTest.java
javac -cp .:./lib/junit-4.10.jar ContactManagerAppTest.java
# javac -cp .:./lib/junit-4.10.jar -Xlint:unchecked IOWriteTest.java
# javac -cp .:./lib/junit-4.10.jar IOReadTest.java
java -cp .:./lib/junit-4.10.jar org.junit.runner.JUnitCore ContactTest
java -cp .:./lib/junit-4.10.jar org.junit.runner.JUnitCore MeetingTest
# java -cp .:./lib/junit-4.10.jar org.junit.runner.JUnitCore ContactManagerTest
java -cp .:./lib/junit-4.10.jar org.junit.runner.JUnitCore ContactManagerAppTest
# java -cp .:./lib/junit-4.10.jar org.junit.runner.JUnitCore IOWriteTest
# java -cp .:./lib/junit-4.10.jar org.junit.runner.JUnitCore IOReadTest