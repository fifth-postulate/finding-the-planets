# Detrend
Take a look the brightness graph you made in the preceding chapter.

![Brightness of Trappist-1](image/brightness-nobackground.png)

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
Below you can find a strategy we have selected for this workshop.

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
 
Let's implement this algorithm. With our `DetrendData` structure, we have an
opportunity to directly implement the different branches of our algorithm. We
start an `impl` block for `DetrendData`.

```rust
impl DetrendData {

}
```

Next we are going to translate the first branch of the algorithm. Since our data
gets delivered to us in the form of a `(f64, f64)` pair, we better accept it as
an argument.

```rust
fn initial((time, brightness): (f64, f64)) -> DetrendData {
    DetrendData {
        time: time,
        brightness: brightness,
        trend: brightness,
        difference: 0f64,
    }
}
```

It is little more than putting things in the right place. Next we will use the
current `DetrendData` that we have, and use it to determine what the next
`DetrendData` should be. Because this depends on the new data and the parameter
\\(\alpha\\), we better accept them both.

```rust
fn next(&self, (time, brightness): (f64, f64), alpha: f64) -> DetrendData {
    let trend = alpha * brightness + (1f64 - alpha) * self.trend;
    DetrendData {
        time: time,
        brightness: brightness,
        trend: trend,
        difference: brightness - trend,
    }
}
```

We calculate the `trend` as described in the algorithm, and calculate the
difference from the brightness and the freshly calculated trend. With a
convenience method that turns the `DetrendData` into a `Vec<String>` we are
ready to calculate our entire trend.

We will collect our data in a vector of `DetrendData`. Because we are going to
incrementally add new entries to it, it needs to be mutable.

```rust
let mut sequence: Vec<DetrendData> = vec!();
```

We also keep track of the last calculated `DetrendData` in a mutable variable
called `data`. Because we haven't calculated any value yet, its type is
`Option<DetrendData>`.

```rust
let mut data: Option<DetrendData> = None
```

This has the added benefit that we can differentiate between when to initialize
data, and when to calculate the next data, during our iteration over our raw data.

```rust
for candidate in raw {
    match data {
        None => {
            data = Some(DetrendData::initial(candidate))
        } 
        
        Some(previous) => {
            let next = previous.next(candidate, alpha);
            sequence.push(previous);
            data = Some(next)
        }
    }
}
```

## Further Considerations
How does the weighted detrend behave for known functions? Try to plot an step
function, i.e. a series that starts out 0 and than is 1 through out, and detrend
it.

What other kind of detrend strategies can you come up with?
