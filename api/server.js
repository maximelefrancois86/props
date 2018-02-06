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

    var id = req.params.id;

    const url = wdk.getWikidataIdsFromWikipediaTitles(id);

    res.setHeader('content-type', 'text/turtle');

    rp(url).then(d => {
        // Get the wikidata id
        var data = JSON.parse(d);
        var id = Object.keys(data.entities)[0];

        // // Get data about entity from wikidata
        // var url = 'https://www.wikidata.org/entity/'+id;

        var query = `
        PREFIX props: <https://w3id.org/props#>
        CONSTRUCT {
            ?item a props:Property ;
                rdfs:label ?label .
        }
        WHERE {
            BIND(wd:Q487005 as ?item)
            ?item rdfs:label ?label
        }
        `

        return rp({uri: 'https://query.wikidata.org/sparql', qs: {query: query}, headers: {'Accept': 'text/turtle'}});
    }).then(data => {
        // For now just return the whole thing
        res.send(data);
    })
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
