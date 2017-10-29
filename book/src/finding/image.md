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
So go ahead and start a new Rust file named `image.rs` in the `src/bin`
directory of your project.

### Reading Data
We will be reading our data from CSV. We will use the crate `simple_csv` for
that. In order to use it include the following lines in `image.rs`.

```rust
extern crate simple_csv;

use simple_csv::SimpleCsvReader;
```

The `SimpleCsvReader` expects some sort of `BufReader`, a buffered reader. We
can create one from a `File`. So include the following modules.


```rust
use std::fs::File;
use std::io::BufReader;
```

And in the main function add.

```rust
let f = File::open("../long-cadence.csv").unwrap();
let buf = BufReader::new(f);
```

Notice that we are not handling errors in a graceful way. We are just going to
arrange everything correctly and hope for the best.

With the `buf` we can create a CSV reader and read the first row of our data.

```rust
let mut reader = SimpleCsvReader::new(buf);
let row = reader.next_row().unwrap().unwrap();
```

The unsightly double `unwrap` at the end comes from the interplay of the
`Iterator` trait that has a `next` function that returns an `Option`, and the
way `simple_csv` parses lines in CSV files. Don't worry about it now, just make
a mental note.

### Processing Data
Our CSV file contains rows of floating point numbers. But the `simple_csv` crate
returns a slice of Strings. We will need to turn those Strings into floating
point numbers before we can properly process them.

We do this by iterating over the `row`. Remember how the first column
contained the time? We don't need it now so we will drop it for the moment.

```rust
let mut current_row = row.iter();
current_row.next(); // dropping time
```

Next we can transform all the measurements in floating point numbers. We can do
that by using the `FromStr` trait. Import it with `use std::str::FromStr`. It
provides a method `from_str` that transforms `&str` into an other type. 


```rust
let raw: Vec<f64> = current_row
    .map(|s| f64::from_str(s).unwrap())
    .collect();
```

Note we need to include a `use std::str::FromStr;` line at the top of our file.

What we are going to do is map these measurements onto a gray scale that we can
save as an image. We do this by determining the maximum measurement, determining
the relative measurement compared to the maximum, and scaling it the an integer
range from 0 to 255.

The following lines achieve this.

```rust
let maximum = raw
    .iter()
    .fold(std::f64::MIN, |acc, v| acc.max(*v));
let data: Vec<u8> = raw
    .iter()
    .map(|s| s/maximum)
    .map(|s| 255.0 * s)
    .map(|s| s.floor() as u8)
    .collect();
```

It uses a method `fold` with the following signature

```rust
fn fold<B, F>(self, init: B, f: F) -> B
    where
        F: FnMut(B, Self::Item) -> B
```

It takes something that implements the `Iterator` trait, a initial value called
`init` and repeatedly calls `f`. The function `f` accepts two arguments. At
first it accepts the initial `init` value and the first element the `Iterator`
produces. After that it accepts the previous call to `f` return value with the
next value of the iterator. A fold returns the final return value of the
function `f`.

## Writing data
Now that we have the gray-scale data, it is time to write it as an image. For
this we will use the `png` crate. Before we can use it add

```rust
extern crate png;
```

To the top of the source file. We also need to include an import statement that
makes our live working with PNGs easier.

```rust
use png::HasParameters;
```

We are going to save the PNG into our working directory. Because the `png` crate
expects a `BufWriter` we will have to include the following modules.

```rust
use std::env;
use std::io::{BufWriter, BufReader};
```

Notice that we already had imported the `BufReader` module. With these imports
we can create a `BufWriter` in one fell swoop.

```rust
let mut path = env::current_dir().unwrap();
path.push(format!("trappist-1.{}.png", 0));
let file = File::create(path).unwrap();
let ref mut w = BufWriter::new(file);
```

Now we can hand over this `BufWriter` to a PNG `Encoder`, configure it to our
liking, create a PNG `Writer` and write the data.

```rust
let mut encoder = png::Encoder::new(w, 11, 11);
encoder.set(png::ColorType::Grayscale).set(png::BitDepth::Eight);
let mut writer = encoder.write_header().unwrap();
writer.write_image_data(data.as_slice()).unwrap();
```

## Our Image
It is finally time to make our own impression of Trappist-1. Use `cargo` to
build and run your code.

```shell
> cargo build
> cargo run --bin image
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
