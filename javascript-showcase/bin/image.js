const fs = require('fs');
const parse = require('csv-parse');
const stringify = require('csv-stringify');
const transform = require('stream-transform');

var input = fs.createReadStream('../workshop/long-cadence.csv');
var parser = parse();
var stringifier = stringify();
var transformer = transform(function(data){
    return data[0];
});

input
    .pipe(parser)
    .pipe(transformer)
    .pipe(stringifier)
    .pipe(process.stdout);
