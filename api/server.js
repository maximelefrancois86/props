const express = require('express');
const wdk = require('wikidata-sdk');
const rp = require('request-promise');
const bodyParser = require('body-parser');  //For parsing request body

var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
// app.use(cors()); //Cross Origin Resource Sharing

//Either get port from environment variable or config file
app.set('port', (process.env.PORT || 3000));

//Static files location
app.use(express.static('./dist'));



/**
 * ROUTES
 */
app.get('/id/:id', (req, res) => {

    var wikiId = req.params.id;

    const url = wdk.getWikidataIdsFromWikipediaTitles(wikiId);

    res.setHeader('content-type', 'text/turtle');

    const makeRequest = async () => {
        try {
            // Get wikidata id
            var data = await rp(url);
            
            var jsonData = JSON.parse(data);
            var id = Object.keys(jsonData.entities)[0];

            var query = `
            PREFIX props: <https://w3id.org/props#>
            PREFIX owl: <http://www.w3.org/2002/07/owl#>
            
            CONSTRUCT {
                props:${wikiId} a owl:Class ;
                    rdfs:subClassOf schema:Property ;
                    rdfs:label ?label ;
                    schema:description ?description .
            }
            WHERE {
                BIND(wd:${id} as ?item)
                ?item rdfs:label ?label ;
                    schema:description ?description .
            }`;

            // Do construct query on resource
            var queryResult = await rp({uri: 'https://query.wikidata.org/sparql', qs: {query: query}, headers: {'Accept': 'text/turtle'}});

            // Return the data
            res.send(queryResult);
        } catch(err) {
            res.status(500).send(err);
        }
    }

    makeRequest();

});


//Handle errors
app.use((err, req, res, next) => {
    if(!err){
        err = 'Something broke!';
    }
    res.send(err);
});

app.listen(app.get('port'), () => {
    console.log('app running on port', app.get('port'));
});
