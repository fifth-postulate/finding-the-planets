# Filter
Take a look the detrended brightness graph you made in the preceding chapter.

![Detrended brightness of Trappist-1](image/detrend.png)

There are clear bands of data. I.e. regions where most of the data-points lie.
But what also stands out enormous are
[outliers](https://en.wikipedia.org/wiki/Outlier). For example, most points are
below 18000, but some shoot out all the way to 28000. They are clearly
erroneous.

There are various reasons how these outliers can occur. Some are the results of
satellite maneuvers. What ever there origin, in this chapter we will filter
those outliers. 

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

Now that we have our data, we can filter it. Our plan is to keep a vector of
results that we will fill with data-points that we want to keep. We will keep
track of the current data-point, who's type is `Option<(f64,f64)>` because
before we start iterating over our data we do not have a current data-point.

While iterating over our candidate data-points we compare it to our current
data-point. If the difference in brightness fall between predetermined threshold
we add our current point to the result and update our current data-point.

All in all, something along the lines of

```rust
let mut result: Vec<(f64, f64)> = vec!();
let mut current: Option<(f64, f64)> = None;
for candidate in raw {
    match current {
        Some(previous) => {
            if (candidate.1 - previous.1).abs() <= threshold {
                result.push(previous);
                current = Some(candidate);
            }
        }

        None => {
            current = Some(candidate)
        }
    }
}
```

The result can be written to a CSV file.

## Further Considerations
The algorithm above depends on a certain threshold. What value should we use?
Try some different values and try to get a feel for what works. Discuss you
choices with somebody else.
