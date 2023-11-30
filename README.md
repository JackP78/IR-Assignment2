to run the search results run the following commands from commandline:

mvn clean package

java -jar target/IR-Assignment2-1.0-SNAPSHOT.jar

trec_eval -m runid -m map -m gm_map -m P.5 ./target/classes/qrels.assignment2.part1 ./results/FinalResults.test

trec_eval -m runid -m map -m gm_map -m P.5 ./target/classes/qrels.assignment2.part1 ./results/Multi.test

(0.2014)

trec_eval -m runid -m map -m gm_map -m P.5 ./target/classes/qrels.assignment2.part1 ./results/LMD.test

(0.1975)

trec_eval -m runid -m map -m gm_map -m P.5 ./target/classes/qrels.assignment2.part1 ./results/Classic.test

(0.0680)

trec_eval -m runid -m map -m gm_map -m P.5 ./target/classes/qrels.assignment2.part1 ./results/BM25.test

(0.1812)
