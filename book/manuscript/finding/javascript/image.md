# Image
Now that we have our data in a CSV file, we are operating on it. The first thing
that we should do is make an image.

## Artist Impression
![An artist impression of Trappist-1](image/artist-impression.jpg)

Often artists are commissioned to create a stunning visualization of new findings.
This is also the case with the Trappist-1 news. Above you find an artist
impression of Trappist-1.

The downside of this is that we could loose track of the actual data that is
used. In order to get a sense of awe for the search of exo-planets, we are
creating our own impression.

## Creating an image
So go ahead and start a new JavaScript file named `image.js` in the `bin`
directory of your project.

### Reading Data
We will be reading our data from CSV. We will use the `csv` packege for
that. In order to use it include the following lines in `image.js`.

```javascript
const parse = require('csv-parse');
const stringify = require('csv-stringify');
const transform = require('stream-transform');

var parser = parse();
var stringifier = stringify();
var transformer = transform(function(data){
    return data;
});
```

The `parser`, `transformer` and `stringifier` can be piped together. So we need
something to act as a source and a drain. We are going to read from file and
write to file so we include de `fs` module.

```javascript
const fs = require('fs');
```

And in the script we add.

```javascript
var input = fs.createReadStream('../long-cadence.csv');
var output = fs.createWriteStream('out.csv');
```

Notice that we are not handling errors in a graceful way. We are just going to
arrange everything correctly and hope for the best.

We now can pipe the input, via our chain into the output.

```javascript
input
    .pipe(parser)
    .pipe(transformer)
    .pipe(stringifier)
    .pipe(output);
```

This is our processing pipeline. You will probably use it for a lot, so make
sure that you understand what is going on. If you run it, nothing really
interesting is going on. Basically we just copied the original data. Let's
change that.

### Processing Data
The transformer can be used to change the data but we are not going to do that
just now. Instead we want to operate on each row that gets piped through our
pipeline.

We can do that with the following lines of code.

```javascript
transformer.on('readable', function(){
    var row;
    while (row = transformer.read()) {
        // process a row
    }
});

```

Our CSV file contains rows of floating point numbers. The first value is the
time of the recording and the rest are image values, one for each pixel of a
11x11 image.

So inside the transformer while loop, i.e. where we process a row, we are going
to create a PNG file. Before we can do that we need to require the `node-png`
module.

```javascript
const PNG = require('node-png').PNG;
```

Creating the PNG file by calling the constructor with the correct options.

```javascript
var png = new PNG({
    width: 11,
    height: 11,
    filter: -1
});
```

Now we are ready to process our row. What we are going to do is map these
measurements onto a gray scale that we can save as an image. We do this by
determining the maximum measurement, determining the relative measurement
compared to the maximum, and scaling it the an integer range from 0 to 255.

```javascript
var data = row.slice(1);
var max = Math.max(...data);
data.forEach(function(value, index){
    var gray = value/max * 255;
    png.data[4*index + 0] = gray;
    png.data[4*index + 1] = gray;
    png.data[4*index + 2] = gray;
    png.data[4*index + 3] = 0xff;
});
```
Finally, we write our image.

```javascript
png.pack().pipe(fs.createWriteStream('out.png'));
```

## Our Image
It is finally time to make our own impression of Trappist-1. Use `node` to run
your code.

```shell
> node bin/image.js
```

Which creates

![Actual Trappist-1 photo](image/trappist-1.0.png)

## Appreciate the Image
At first glance the image can be a little underwhelming. But it is precisely
this image that blew my mind! Being accustomed to the marvelous artist
impression, when I learned about the actual data was 11x11 pixels I was hooked.
How could anyone extract so much information from so little data?

![10 times enlargement of actual Trappist-1 photo](image/trappist-1.0.large.png)

I had to know and I hope you want to know too!

## Further Considerations
* Make a bigger image with larger "pixels".
* Make an entire series of images, one for each row.
* Make a GIF or movie of the images.
