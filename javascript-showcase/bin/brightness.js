const fs = require('fs');
const parse = require('csv-parse');
const stringify = require('csv-stringify');
const transform = require('stream-transform');

var input = fs.createReadStream('../workshop/long-cadence.csv');
var output = fs.createWriteStream('brightness.csv');
var parser = parse();
var stringifier = stringify();
var transformer = transform(function(data){
    const time = data[0];
    const brightness = data.slice(1);
    const sum = brightness
          .map(function(value){ return parseFloat(value); })
          .reduce(function(partial_sum, value){
              return partial_sum + value;
          }, 0);
    const average = sum / brightness.length;
    const filtered_sum = brightness
          .filter(function(value){
              return value >= average;
          })
          .reduce(function(partial_sum, value){
              return partial_sum + value;
          }, 0);
    return [time, filtered_sum];
});

input
    .pipe(parser)
    .pipe(transformer)
    .pipe(stringifier)
    .pipe(output);
