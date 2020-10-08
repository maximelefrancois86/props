# PSETGenerator

Project for transforming buildingSMART PSets into OWL ontologies (datatype properties) using [SPARQL Generate](http://ci.mines-stetienne.fr/sparql-generate/)


To get it running:


```
git clone https://github.com/thesmartenergy/sparql-generate.git
cd sparql-generate
git checkout `git rev-list -1 --before="Sep 25 2017" master`
mvn install

cd ..
git clone https://github.com/w3c-lbd-cg/pset.git
cd pset
mvn compile exec:java -Dexec.mainClass="com.github.w3clbdcg.pset.GenerateVocabulary"
```

