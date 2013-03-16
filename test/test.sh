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
javac -cp .:./lib/junit-4.10.jar ContactManagerAppTest.java
java -cp .:./lib/junit-4.10.jar org.junit.runner.JUnitCore ContactTest
java -cp .:./lib/junit-4.10.jar org.junit.runner.JUnitCore MeetingTest
java -cp .:./lib/junit-4.10.jar org.junit.runner.JUnitCore ContactManagerAppTest