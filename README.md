# PSETGenerator

Project for transforming buildingSMART PSets into OWL ontologies (datatype properties) using [SPARQL Generate](http://ci.emse.fr/sparql-generate/)


To get it running:


```
git clone https://github.com/thesmartenergy/sparql-generate.git
cd sparql-generate
mvn install

cd ..
git clone https://github.com/w3c-lbd-cg/pset.git
cd pset
mvn compile exec:java -Dexec.mainClass="com.github.w3clbdcg.pset.GenerateVocabulary"
```

