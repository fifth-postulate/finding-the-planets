# Detrend
Take a look the brightness graph you made in the preceding chapter.

![Brightness of Trappist-1](image/brightness.png)

Notice how the graph tends to flare up. This is a systemic problem that we
should correct. We are going to do that by finding what trend the graph is
following, and adjusting for that.

## Processing
Before, we processed each row individually. Now we need to operate on the entire
sequence. So instead iterating over each row, we are going to transform it
directly.

Because a `SimpleCsvReader` is an `Iterator` we can use our tricks on it. The
idiosyncrasies of the `SimpleCsvReader` make that we first need to unwrap a row.
Next we can map over the row of data and collect into a vector of tuples, the
entry being the time and the second entry being the brightness.

```rust
let raw: Vec<(f64, f64)> = reader
    .map(|r| r.unwrap())
    .map(data)
    .collect();
```

The function `data` has the following signature

```rust
fn data(row: Vec<String>) -> (f64, f64)
```

`data` is responsible for turning the raw columns of our CSV into `f64` brightness values,
and selecting the correct ones.

Up until now we never returned more than two or three values. For our current
plan we are going to return more. In order to keep track of our data, we are
going to create a `struct`.

```rust
struct DetrendData {
    time: f64,
    brightness: f64,
    trend: f64,
    difference: f64,
}
```

We have created a few entries, some familiar, some unfamiliar. `time` and
`brightness` are pretty self-explanatory. `difference` is intended to hold the
difference between the `brightness` and the `trend`.

But how do we calculate the trend?

### Strategies
Let us reflect on what we are trying to achieve. We have some data points
\\(y_{0}, y_{1}, \ldots, y_{n}\\). We have a model that predicts that these
values fluctuate around a given mean \\(Y\\), but for some reason or another, it
doesn't.

Instead the values fluctuate around some function \\(f\\), for which we don't
now the shape or form. This is called the _trend_.

Our goal is to approximate the trend function \\(f\\) by a function that we can
calculate from the data. Next we can analyze the actual signal by removing the
trend. In effect we will look at the de-trended signal \\(y_{0} - t(0), y_{1} -
t(1), \ldots, y_{n} - t(n)\\). Here \\(t\\) is our approximation for the trend.

We shall do this by providing the values of our approximation.

There are numerous strategies for determining the trend in a sequence of data.
Below you can find some we have selected for this workshop.

#### Weighted Trend
With the notations from the preceding section the weighted trend algorithm is as
follows. First you pick a parameter \\(\alpha\\) such that it lies between zero
and one, i.e. \\(0 \le \alpha \le 1\\).

Next we will explain how to calculate each point of our approximation to the
trend.

* \\(t_{0} = y_{0}\\). I.e. our first approximation is the first value of our
  sequence of data.
* \\(t_{i} = \alpha y_{i} + (1-\alpha) t_{i-1}\\). I.e. our trend tends towards
  the new value of our sequence, but is a but reluctant. It tends to stick to
  the previous values.
 
