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
library. Let's start with our `lib.rs`.

In our `lib.rs` we are announcing a module called `median`. 

```rust
pub mod median;
```

There are different ways to create this module. Either creating a `median.rs`
file inside the `src` directory. Or creating a `median` directory inside the
`src` directory, which contains a `mod.rs` file. Which every you choose, let's
implement a `median_of` function.

Our `median_of` function will have a `&Vec<f64>` as parameter and return the
median `f64`. Once we have a sorted copy of the data called `copy`, getting the
median comes down to determining if the number of elements is even or odd, and
performing the right calculation.

```rust
let n = data.len();
let middle: usize = n / 2;
let median = if n % 2 == 1 {
    copy[middle]
} else {
    (copy[middle] + copy[middle - 1]) / 2.0;
}
```

But how do we sort the original data? 

### Sorting side-quest
There are a few interesting tidbits when sorting a `Vec<f64>` that we are going
to make a side-quest out of it. While looking into `Vec<T>` 
[documentation](https://doc.rust-lang.org/std/vec/struct.Vec.html#method.sort),
you can come across the method `sort`. Let's see if we can use it.

```rust
let mut vs: Vec<f64> = vec!(3.0, 2.0, 1.0);

vs.sort();
```

Unfortunately this doesn't compile.

```text
   Compiling playground v0.0.1 (file:///playground)
error[E0277]: the trait bound `f64: std::cmp::Ord` is not satisfied
 --> src/main.rs:6:4
  |
6 | vs.sort();
  |    ^^^^ the trait `std::cmp::Ord` is not implemented for `f64`

error: aborting due to previous error

error: Could not compile `playground`.

To learn more, run the command again with --verbose.
```

Which could come as a surprise. The `Ord` trait determines an ordering of
elements. Certainly we can determine whether `0.0 < 1.0``? 

```rust
assert!(0.0f64 < 1.0f64);
```

Luckily we can. So what is going on? Rust has two related traits for comparison: 
[`PartialOrd`](https://doc.rust-lang.org/std/cmp/trait.PartialOrd.html) and
[`Ord`](https://doc.rust-lang.org/std/cmp/trait.Ord.html). The main difference
is that `Ord` is supposed to be _total_. I.e. any type that implements the `Ord`
trait should be able to compare any pair of values that have the type.

In other words, if you implement the `Ord` trait you should be able to answer
**yes** to one and only one of the following questions with for values `a` and
`b` in the type:

1. Is `a < b`?
2. Is `a == b`?
3. Is `a > b`?

The problem with `f64` is that is implements IEEE-754, the standard for
arithmetic with floating point numbers. This standard defines a value `NaN`,
not a number, which is not comparable with any other value.

So `f64` can not be complete and follow the standard at the same time.
Fortunately `PartialOrd` is implemented for `f64`. So as long as we do not
compare with `NaN`s, which we don't intend to do, we should be safe.

Back to sorting, the `sort` method expects that the `Ord` is implemented, so we
can not use it. `Vec<T>` also has a `sort_by` method, that allows to pass a `compare`
function. We can use this to our advantage by relying on the `PartialOrd` trait.

```rust
let mut vs: Vec<f64> = vec!(3.0, 2.0, 1.0);

vs.sort_by(|a, b| a.partial_cmp(b).unwrap());

println!("{:?}", vs);
```

This correctly sorts our vector. But notice that the `vs` variable is declared
mutable. Our signature doesn't expect to have a mutable reference, so we need to
copy our `data` first.

### Copying Data
We need a mutable copy of our `data`. Luckily the `Vec<T>` API provides an other
method; `copy_from_slice`. We use it as

```rust
let n = data.len();
let mut copy = vec!(0f64; n);
copy.copy_from_slice(&data);
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
