# Fitting 
We have created a plot of the median.

![The median of the filtered brightness](image/median.png)

We would like to find planets in it. Finding planets amounts to selecting a
transit curve that nicely fits our data. We our going to divide that task in the
following sub-tasks.

1. Generating a transit curve series
2. Iterating over all transit curve parameters
3. Scoring each candidate transit curve and selecting the best

Let us create a module for this as well. We will call it `fit`.

## Transit Curves

![Multiple periods of a transit curves](image/period.png)

The above image shows a typical transit curve where the planet transits the host
star multiple times. From this diagram we can learn about the parameters that
make up such a transit.

Below we list the parameters important in our transit curve.

1. **Period**. The time between the start of one transit and the start of the
   next transit. 
2. **Base**. Height of the line, when no planet transits. This is often
   normalized, but because of the choices we made, we need this parameter. 
3. **Depth**. How far the luminosity drops when the planets transits. This is
   related to the size of the planet.
4. **Duration**. How long the luminosity stays at full depth.
5. **Decay**. How much time it takes the luminosity to go from the `base` to
   `depth`. In our model the attack, i.e. time it takes the luminosity to go
   from `depth` back to `base`, and decay are the same.
6. **Phase**. Where in the period does the periodic function start.

Below you find a `struct` and an implemented constructor that can track this
data.

```rust
pub struct Transit {
    period: f64,
    base: f64,
    depth: f64,
    duration: f64,
    decay: f64,
    phase: f64,
}

impl Transit {
    fn new((period, base, depth, duration, decay, phase): (f64,f64,f64,f64,f64,f64)) -> Transit {
        Transit { period, base, depth, duration, decay, phase }
    }
}
```

Notice that the `new` function accepts a tuple of floating point numbers. We
will use this when we iterate over the parameters.

What we also want to know is, when we have got a `time`, what is the
corresponding value of this transit curve. For that we are going to implement a
`value` method on `Transit

What we also want to know is, when we have got a `time, what is the
corresponding value of this transit curve. For that we are going to implement a
`value method on `Transit`.

The interesting times are

1. Before the decay. The value should be `base`
2. During the decay. The value should linearly interpolate between `base` and
   `base` - `depth`.
3. During full transit. The value should be `base` - `depth`
4. During the attack. The value should linearly interpolate between `base` -
   `depth` and `base`.
5. After the transit. The value should be `base`.

Implement the above logic into a `value` method for `Transit`.

## Iterate Parameters
Our transit curve has five parameters. We would like to generate candidate
transit curves and check how well they fit our data. This can be accomplished by
iterating over the five parameters, and mapping them into a transit curve.

### FloatIterator
We will first focus on an iterator for a single `f64`. We want all floating
point numbers between a `start` and `finish`, increasing each new number with a
certain `step`. We will create a `struct` that keeps track of where we are.

```rust
pub struct FloatIterator {
    start: f64,
    finish: f64,
    step: f64,
    current: u64,
}
```

Implementing a `new` constructor should set the `current` to `0` and accept
`start`, `finish` and step as parameters.

Next we need to implement `Iterator` for `FloatIterator`. We must import
`std::iter::Iterator` so that we can easily reference it in our code. In the
`next` method of the `Iterator` trait we need to decide if we need to return a
`Some` or a `None`. This depends on the our intended return value. I.e. if the
value `start + step * current` is less then or equal to our `finish`.

```rust
impl Iterator for FloatIterator {
    type Item = f64;

    fn next(&mut self) -> Option<Self::Item> {
        let value = self.start + self.step * (self.current as f64);

        if value <= self.finish {
            self.current += 1;

            Some(value)
        } else {
            None
        }
    }
}
```

This wraps up our `FloatIterator`.

### Exemplar TupleIterator
Next we are going to create a `TupleIterator`. It is going to show all the
necessary tools to create the actual `TupleIterator` we want, without getting
distracted by the tedious details.

Because we want to express multiple times a range of floating point numbers we
are interested in, we are going to create a `struct` to keep track of `start`,
`finish` and `step`.

```rust
pub struct FloatRange {
    start: f64,
    finish: f64,
    step: f64,
}
```

implementing a `new` constructor for `FloatRange` is nothing more than excepting
the correct parameters and passing them in the struct. Having a `FloatRange`
allows us to ask it for the value belonging to a certain index. Let's extend the
implementation of `FloatRange` with an `index` function. The actual
implementation looks very familiar.

```rust
fn index(&self, index: u64) -> Option<f64> {
    let value = self.start + self.step * (index as f64);

    if value <= self.finish {
        Some(value)
    } else {
        None
    }
}
```

Now that we can express the floating point range we are interested in, we can
use that in our `TupleIterator`. The responsibility of the `TupleIterator` is to
keep track of two indices into two separate `FloatIterator`. Because we need to
be able to "restart" the `FloatIterator` we are not actually use a
`FloatIterator`. Instead we choose to do the iterating our selves.

We start with a structure that will keep track for us.

```rust
pub struct TupleIterator {
    first: FloatRange,
    second: FloatRange,
    current: (u64, u64),
}
```

It looks a lot like the `FloatIterator`. The main difference is that we need to
keep track of two different ranges, and two different indices into these
iterators. Implementing a new is just like the `FloatIterator` little more than
accepting the correct parameters and initializing the current indices to
`(0,0)`.

Now for implementing `Iterator` for `TupleIterator`. It comes down to keeping
track of the right indices. Let's look at the implementation.

```rust
impl Iterator for TupleIterator {
    type Item = (f64, f64);

    fn next(&mut self) -> Option<Self::Item> {
        let (first_index, second_index) = self.current;

        match self.first.index(first_index) {
            Some(first_value) => {
                match self.second.index(second_index) {
                    Some(second_value) => {
                        self.current = (first_index, second_index + 1);

                        Some((first_value, second_value))
                    },

                    None => {
                        self.current = (first_index + 1, 0);

                        self.next()
                    },
                }
            },

            None => None,
        }
    }
}
```

Reading the code, we discover that we determine two values, one for each
`FloatRange`. If the first `FloatRange` return `None` we are done. If it returns
some value, we see what the second `FloatRange` returns. If that also returns
some value, we do the following things.

1. Increment the second index.
2. Return the tuple of values.
 
If the second `FloatRange` doesn't return some value, we know that it exhausted
its range. We then increment the first index, resetting the second index. In
effect we want to the second iterator from scratch, but with a new first value.
Next we use the power of recursion to determine what the value should be with
the new indices. 

Although it doesn't look pretty, it does its job. It is your task to extend this
example to iterate over all transit parameters.

## Score
We are going to score a `Transit` with the 
[_least squares_](https://en.wikipedia.org/wiki/Least_squares) method. This
method sums the squares of the difference between two series. That is easier
said than done. Lets look at following code.

```rust
pub fn least_squares(xs: &Vec<f64>, ys: &Vec<f64>) -> f64 {
    xs.iter().zip(ys)
        .map(|(a,b)| (a-b).powi(2))
        .sum()
}
```

We define a function names `least_squares` that accepts to vectors of floating
points numbers. Next we recognize our dear friend: the `iter` method. We use it
on the first vector and zip it with the second vector. On the vector of pairs we
map the function that calculates the squared difference. We finish with summing
all the numbers, getting our result.

With all the parts in place we are ready to start processing.

## Processing
We need to compare candidate transit curves with our median, so we need to read our `median.csv`. Because we would like to process the times and the values separedly we use the `unzip` trick we learned earlier.

```rust
let (times, values): (Vec<f64>, Vec<f64>) = raw
    .iter()
    .cloned()
    .unzip();
```

Processing consist for a big part of a main loop that iterates over our transit parameters. This is depend on a number of `FloatRange`s and this is where you can shine. By looking at your `median.csv` data you can guess good ranges, and with some luck you will find planets.

We need to keep track of the best transit curve. So we initialize variables before our main loop.

```rust
let mut best_score = f64::MAX;
let mut best_transit: Option<Transit> = None;
```

In order to make use of the `f64::MAX` we need to import `std::f64`. Not that the best score will actually be the lowest value, so it is save to initialize it to the maximum value.

Inside our loop, we can create a transit curve from the parameters.

```rust
let transit = Transit::new(parameters);
```

The `transit` can be used to determine the values at the times we observed by mapping over the `times` and using the `value` method.

```rust
let transit_values: Vec<f64> = times.iter().map(|t| transit.value(t)).collect();
```

Scoring is little more than calling the right function.

```rust
let score = least_squares(&transit_values, &values);
```

Now that we have the score, we need to compare it with the best score we now about, and update our best candidate accordingly.

```rust
if score < best_score {
    best_score = score;
    best_transit = Some(transit.clone());
}
```

When the loop finishes we would like to know which transit is the best. So we prepare to print it to console, and calculate the actual values, which you can write to a CSV file.

```rust
let best_transit = best_transit.unwrap();
let best_transit_values: Vec<f64> = times.iter().map(|t| best_transit.value(t)).collect();
println!("{:?}", best_transit);

let result = times.iter().zip(best_transit_values);
```

Lets fly through our candidates and see what planet you can find.