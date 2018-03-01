const fs = require('fs');
const parse = require('csv-parse');
const stringify = require('csv-stringify');
const transform = require('stream-transform');
const PNG = require('node-png').PNG;

var input = fs.createReadStream('../workshop/long-cadence.csv');
var output = fs.createWriteStream('out.csv');
var parser = parse();
var stringifier = stringify();
var transformer = transform(function(data){
    return data;
});

var maximum = Number.NEGATIVE_INFINITY;
transformer.on('readable', function(){
    var row;
    while (row = transformer.read()) {
        var data = row.slice(1);
        var max = Math.max(...data);

        if (max > maximum) { maximum = max; }
    }
});
transformer.on('finish', function(){
    console.log(maximum);
});

input
    .pipe(parser)
    .pipe(transformer)
    .pipe(stringifier)
    .pipe(output);
