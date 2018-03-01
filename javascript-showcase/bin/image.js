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

var image_index = 0;
transformer.on('readable', function(){
    var row;
    while (row = transformer.read()) {
        var png = new PNG({
            width: 11,
            height: 11,
            filter: -1
        });

        var data = row.slice(1);
        var max = Math.max(...data);
        data.forEach(function(value, index){
            var gray = value/max * 255;
            png.data[4*index + 0] = gray;
            png.data[4*index + 1] = gray;
            png.data[4*index + 2] = gray;
            png.data[4*index + 3] = 0xff;
        });

        png.pack().pipe(fs.createWriteStream('resources/image-' + image_index + '.png'));
        image_index++;
    }
});

input
    .pipe(parser)
    .pipe(transformer)
    .pipe(stringifier)
    .pipe(output);
