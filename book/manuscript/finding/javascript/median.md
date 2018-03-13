# Median
We filtered our brightness graph and got something like this.

![Filtered brightness of Trappist-1](image/filter.png)

We would like to know around what kind of average these points are fluctuating.
For that we are calculating the median.

## Calculation
Let's say we have a sequence of values \\(y_{0}, y_{1}, \ldots, y_{n-1}\\). The
median of these numbers is defined as follows.

1. Sort the numbers into a sequence \\(z_{0}, z_{1}, \dots, z_{n-1}\\).
2. From this sorted sequence, pick the middle number. If there is no middle,
   take the average of the middle two.

Lets work out an example. Take a look at the following example

\\[
31, 41, 59, 26, 53, 58, 97, 93, 23, 84
\\]

If we sort this sequence we get

\\[
23, 26, 31, 41, 53, 58, 59, 84, 93, 97
\\]

Because there are an even number of values, we should take the average of the of
the two middle values. The average of \\(53\\) and \\(58\\) is 
\\(\frac{53 + 58}{2} = \frac{111}{2} \approx 55.5\\).

## Make a library.
Because we are going to use the median several times, we are going to create a
library. Let's start with our `median.js`.

In our `median.js` we are `module.export` will be a function `median_of`. 

```javascript
module.export = median_of;
```
Our `median_of` function will have an array as parameter and return the
median. Once we have a sorted copy of the data called `copy`, getting the
median comes down to determining if the number of elements is even or odd, and
performing the right calculation.

```rust
const n = data.length;
const middle = Math.floor(n / 2);
var median;
if (n % 2 == 1) {
    return data[middle]
} else {
    return (data[middle] + data[middle - 1]) / 2.0;
}
```

But how do we sort the original data? 

### Sorting side-quest

There are a few interesting tidbits about sorting an array of floating point
values that we are going to make a side-quest out of it. While looking into
`Array.prototype`
[documentation](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/prototype),
you can come across the method `sort`. Let's see if we can use it. 

```javascript
const vs = [3.0, 2.0, 1.0, 11.0];

vs.sort();
```

Unfortunately this doesn't work as expected.

```text
[ 1, 11, 2, 3 ]
```

Which could come as a surprise.  The sort function first transforms the values
to string and then sorts them as Unicode source point.

Luckily we can solve that. We just have to pass a function with which we will
sort the values.

```javascript
const vs = [3.0, 2.0, 1.0. 11.0];

vs.sort(function(a, b){ return b > a; });
```

This correctly sorts our array. But it also alters our original data.

### Copying Data
We need a copy of our `data`. Luckily the `slice` provides an easy
method to copy. We use it as

```rust
const data = values.slice();
```

This is the final piece in the median puzzle. We are able to put everything
together and write our `median_of` function.

### Form Groups
We do not want to calculate the median of our entire sequence. Instead we want
to move a [*sliding window*](https://en.wikipedia.org/wiki/Streaming_algorithm)
over our data and calculate the median of that specific window.

For that we need to group our data. Let's create that function.

```rust
fn groups(data: &Vec<f64>, group_size: usize) -> Vec<Vec<f64>> {
    let mut groups: Vec<Vec<f64>> = vec!();

    for end_index in group_size .. data.len() + 1 {
        let mut group: Vec<f64> = vec!();
        for index in (end_index - group_size) .. end_index {
            group.push(data[index])
        }
        groups.push(group)
    }

    groups
}
```

### Median Filter
We are now in the position to create a `median_filter` function. I.e. a function
that calculates the median of a sliding window over our data. With all of our
preparations it writes itself as

```rust
pub fn median_filter(data: &Vec<f64>, window: usize) -> Vec<f64> {
    groups(data, window)
        .iter()
        .map(median_of)
        .collect()
}
```

With our library all done, we can start out processing proper.

## Processing
But wait! Our data arrives as `f64`-pairs, i.e. `(f64, f64)`, and we create
`median_filter` to operate on a single `f64` value. Did I lead you down a wrong
path?

Not entirely. Once again the standard library, in the form of the `Iter` trait,
has a trick up their sleeve. It comes in the pair of methods `zip` and `unzip`.
You can find their signatures below.
With `unzip` you can take a sequences of pairs and return a pair of sequences.
`zip` goes the other way.

Let's see how we can use them. After getting the raw data, we can use `unzip` to
extract the individual components.

```rust
let (times, values): (Vec<f64>, Vec<f64>) = raw
    .iter()
    .cloned()
    .unzip();
```

The `cloned` call is because we need to take ownership of our data. Next we can
use our `median_filter` from our own library. Make sure to reference our own
external crate and import the correct function.

```rust
let median_times = median_filter(&times, window_size);
let median_values = median_filter(&values, window_size);
```

Finally we can zip together these two vectors again to get our result.

```rust
let result = median_times.iter().zip(median_values);
```

Storing this into a CSV file makes it available for the next step.

## Further Considerations
You have created a library that contains some functions. How do you know that
they are implemented correctly? Try to add some
[tests](https://doc.rust-lang.org/book/second-edition/ch11-03-test-organization.html)
that increases your confidence in your code.

The `median_filter` accepts an `window_size` argument. What is a good value?

Why haven't we used same the method we used to detrend the data?
