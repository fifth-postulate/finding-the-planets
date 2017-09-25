# Brightness
We are going to detect the planets by observing drops in overall brightness.
Before we are able to do this, we need to calculate the brightness. That is
precisely the objective in this chapter.

We are going to create a CSV file with the first column the time of the
measurement and the second column the brightness at that time.

## Processing
For each row of data we would like to know how much Trappist-1 is radiating.
What we are going to do is the following.

Take a row of data and

1. Convert each value to a `f64`.
2. Sum all the values to get the overall brightness.

Converting values to a `f64` is something we did before. We are not going into
details for the conversion.

The summation of all the values can be written down very succinctly because the
[`Iterator`](https://doc.rust-lang.org/std/iter/) trait has a trick up it's
sleeve.

It defines a method `fold` with the following signature

```rust
fn fold<B, F>(self, init: B, f: F) -> B
    where
        F: FnMut(B, Self::Item) -> B
```

It takes something that implements the `Iterator` trait, a initial value called
`init` and repeatedly calls `f`. The function `f` accepts two arguments. At
first is accepts the initial `init` value and the first element the `Iterator`
produces. After that it accepts the previous call to `f` return value with the
next value of the iterator. A fold returns the final return value of the
function `f`.

We can use it to calculate the sum of all the brightness values. If we have our
raw `f64` values in the variable `raw`, we can determine the sum with

```rust
let sum: f64 = raw
    .iter()
    .fold(0f64, |acc, v| acc+v);
```

### Removing Background
If we take a look at one of the images

![Enlargement of an image of Trappist-1](image/trappist-1.0.large.png)

we see that the background is not pitch black. This means that the background
adds to the brightness, even though it does not contribute to the signal. So we
start our journey with something we will come very familiar with, we will clean
up our data.

What we are going to do is ignore the brightness value of anything below the
average brightness. This transforms the image from above into the image below.

![Enlargement of an image of Trappist-1 with background removed](image/trappist-1.0.nobg.large.png)

Still not perfect, but it is better than nothing.

In order to filter out the unwanted background we are going to need to know the
average. We already no the sum, we just calculated it, so the average can be
determined by

```rust
let average = sum / (row.len() as f64);
```

Calculating the contribution of the values above the average can still be done
succinctly. What we need to do is filter out the values that we want to sum.
I.e. the values above the average.

```rust
let filtered: f64 = raw
    .iter().
    .filter(|&v| *v >= average)
    .fold(0f64, |acc, v| acc+v)
```

## Graphing Results
Once you wrote your brightness results to a CSV file, they are ready for the
following step. But if you are like me you probably want to see your results.
This is where gnuplot comes in.

If you have saved your results as `brightness.csv`, the following gnuplot
session will plot your data.

```
set datafile separator ','

plot [2905:2985] "brightness.csv" using 1:2
```

We will annotate the above example a little, so that you can use gnuplot on your
own. The `simple_csv` library outputs CSV files with a comma as separator. This
difference from the default assumption of gnuplot. Luckily this can be remedied
with the first line.

The second line display the core of gnuplot; the `plot` command. The first
argument, i.e. `[2905:2985]`, defines the range on the x-axis. It is optional
and will be inferred by gnuplot if it isn't present. If there would be a second
argument of that form, i.e. `[min:max]`, that would be the range on the y-axis.
Here it is inferred.

The `"brightness.csv"` argument you probably recognize as the file you wrote
your data to. The `plot` command will use data in this file to plot.

The last refers to columns in the data. `using 1:2` tells the plot command to
plot point with the first column as x-coordinate and the second column as
y-coordinate.

For a more extensive explanation of gnuplot we refer you to the 
[gnuplot homepage](http://www.gnuplot.info/).

If you have gone to the trouble of outputting the brightness with and without
the background, your plot could look like the one below.

![Plot of brightness, with and without background contribution](image/brightness-both.png)

## Further Considerations
Take a look at your data and write down what stands out to you. Discuss this
with a neighbor.

There is an obvious gap in our data. This is where the Kepler satellite stopped
recording data due to a software reboot initiated by a cosmic ray event.
Although the data was lost, the satellite still operates nominally.

Furthermore there is a trend in the overall brightness, more pronounced in the
data with the background. This is also seen in our collage. We will have to
smooth out that trend and that is precisely what we will do next.
