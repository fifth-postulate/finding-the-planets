const fs = require('fs');
const parse = require('csv-parse');
const stringify = require('csv-stringify');
const transform = require('stream-transform');
const PNG = require('node-png').PNG;

const BASE = 11;
const HORIZONTAL_TILES = 61;
const VERTICAL_TILES = 59;
const WIDTH = HORIZONTAL_TILES * BASE;
const HEIGHT = VERTICAL_TILES * BASE;
const SIZE = WIDTH * HEIGHT;
const MAX = 3925;

var input = fs.createReadStream('../workshop/long-cadence.csv');
var output = fs.createWriteStream('out.csv');
var parser = parse();
var stringifier = stringify();
var transformer = transform(function(data){
    return data;
});

var png = new PNG({
    width: WIDTH,
    height: HEIGHT,
    filter: -1
});

var row_index = 0;
transformer.on('readable', function(){
    var row;
    while (row = transformer.read()) {
        var data = row.slice(1);
        var offset_X = row_index % HORIZONTAL_TILES;
        var offset_Y = Math.floor(row_index/ HORIZONTAL_TILES);
        data.forEach(function(value, original_index){
            var offset_x = original_index % BASE;
            var offset_y = Math.floor(original_index / BASE);
            var target_index = offset_Y * (BASE * HORIZONTAL_TILES * BASE) +
                offset_X * BASE +
                offset_y * (HORIZONTAL_TILES * BASE) +
                offset_x;
            var gray = value/MAX * 255;
            png.data[4*target_index + 0] = gray;
            png.data[4*target_index + 1] = gray;
            png.data[4*target_index + 2] = gray;
            png.data[4*target_index + 3] = 0xff;
        });

        row_index++;
    }
});
transformer.on('finish', function(){
    png.pack().pipe(fs.createWriteStream('resources/collage.png'));
});

input
    .pipe(parser)
    .pipe(transformer)
    .pipe(stringifier)
    .pipe(output);
