# Filter
Take a look the detrended brightness graph you made in the preceding chapter.

![Detrended brightness of Trappist-1](image/detrend.png)

There is a clear band of data. I.e. regions where most of the data-points lie.
But what also stands out enormous are
[outliers](https://en.wikipedia.org/wiki/Outlier). For example, most points are
below 50, but some shoot out all the way to 600. They are clearly
erroneous.

There are various reasons how these outliers can occur. Some are the results of
satellite maneuvers. What ever there origin, in this chapter we will filter
those outliers. 

## Processing
We are going to rely on our `data` function again. Remeber the `data` function
is responsible for:

> turning the raw columns of our CSV into `f64` values, and selecting the
> correct ones. 

Now that we have our data, we can filter it in one swoop. `Iter` still has a
trick up it's sleeve. It sports a `filter` method that fits our needs. Study the
code below.

```rust
let result: Vec<(f64, f64)> = reader
    .map (|r| r.unwrap())
    .map(data)
    .filter(|&(_,difference)| difference.abs() <= threshold)
    .collect();
```

The code above is depending on a threshold. Once chosen, the result can be written to a CSV file.

## Further Considerations
The algorithm above depends on a certain threshold. What value should we use?
Try some different values and try to get a feel for what works. Discuss your
choices with somebody else.
