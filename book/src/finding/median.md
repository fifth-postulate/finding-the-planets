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

Which could come as a surprise. Certainly we can determine whether `0.0 < 1.0``?

```rust
assert!(0.0f64 < 1.0f64);
```

Luckily we can. So what is going on? Rust has two related traits for comparison: 
[`PartialOrd`](https://doc.rust-lang.org/std/cmp/trait.PartialOrd.html) and
[`Ord`](https://doc.rust-lang.org/std/cmp/trait.Ord.html). The main difference
is that `Ord` is supposed to be _total_. I.e. any type that implements the `Ord`
trait should be able to compare any pair of values in that have the type.

In other words, if you implement the `Ord` trait you should be able to answer
the one and only one of the following questions with *yes* for values `a` and
`b` in the type:

1. Is `a < b`?
2. Is `a == b`?
3. Is `a > b`?

The problem with `f64` is that is implements IEEE-754, the standard for
arithmetic with floating point arithmetic. This standard defines values `NaN`,
not a number, for which is not comparable with any other value.

So `f64` can not be complete and follow the standard at the same time.
`PartialOrd` is implemented for `f64`. So as long as we do not compare with
`NaN`s, which we don't intend to do, we should be save.

Back to sorting, the `sort` method expects that the `Ord` is implemented, so we
can not use it. It also has a `sort_by`, that allows to pass a `compare`
function. We can use this to our advantage by relying on the `PartialOrd` trait.

```rust
let mut vs: Vec<f64> = vec!(3.0, 2.0, 1.0);

vs.sort_by(|a, b| a.partial_cmp(b).unwrap());

println!("{:?}", vs);
```
