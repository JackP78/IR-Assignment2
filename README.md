to run the search results run the following commands from commandline:

mvn clean package

java -jar target/IR-Assignment2-1.0-SNAPSHOT.jar

trec_eval -m runid -m map -m gm_map -m P.5 ./target/classes/qrels.assignment2.part1 ./results/Standard.test
