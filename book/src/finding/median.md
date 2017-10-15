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
Because we are going to use the median several times, we are going to create a library.
