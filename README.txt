This program runs on Java and thus needs an up to date version of JDK and a JRE. It can
be compiled by using the command line or by using an IDE. If using the latter, maven will
be required when importing the project. The pom.xml file describes the necessary
dependencies (json-simple), and handles the external library without the need for the
user to manually add it to the project. If using the former, javac will compile the
Index.java and Tuple.java files and java will run the Index.class and Tuple.class files.