# Median
We filtered our brightness graph and got something like this.

, pick the middle number. If there is no middle,
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
library. Let's start with our `lib.js`.

In our `median.js` we are `module.exports` will be a function object. 

```javascript
module.exports = {
    median_of: median_of
};
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

vs.sort(function(a, b){ return b < a; });
```

This correctly sorts our array. But it also alters our original data.

### Copying Data
We need a copy of our `data`. Luckily the `slice` provides an easy
method to copy. We use it as

```javascript
const data = values.slice();
```

This is the final piece in the median puzzle. We are able to put everything
together and write our `median_of` function.

### Form Groups
We do not want to calculate the median of our entire sequence. Instead we want
to move a [*sliding window*](https://en.wikipedia.org/wiki/Streaming_algorithm)
over our data and calculate the median of that specific window.

For that we need to group our data. Let's create that an object.

```javascript
const SlidingWindow = function(size){
    this.size = size;
    this.window = [];
};
```

We are creating a `SlidingWindow` constructor. This object will keep track of
two things. The size of the sliding window, and the values that it will have
seen. Together with two prototype functions this will provide our intended
functionality.

```javascript
SlidingWindow.prototype.push = function(value){
    this.window.push(value);
    if (this.window.length > this.size) {
        this.window = this.window.slice(1);
    }
};
```

The `push` method will be used to add a new value to the window, maintaining the
invariant of maximum of `size` values. Whenever the window grows to large, we
slice off a value.

```javascript
SlidingWindow.prototype.result = function(){
    if (this.window.length === this.size) {
        return this.window.slice();
    } else {
        return null;
    }
};
```

The `result` method will return the current window, if it has grown enough to
have the correct `size`. Otherwise it will return `null`, which will signal the
`csv` module that there is no data.

Make sure to add the `SlidingWindow` to the `module.exports`.

## Processing
We are now ready to create a tranformation for our data. In our data we should
keep track of two sliding windows. One for the time measurement and one for the
brightness measurements.

We collect the median of both and combine them in an array to produce the
output of our pipeline. Maybe something like this.

```javascript
var size = 10;
var times = new SlidingWindow(size);
var values = new SlidingWindow(size);
var transformer = transform(function(data){
    const time = parseFloat(data[0]);
    const value = parseFloat(data[3]);

    times.push(time);
    values.push(value);

    var tw = times.result();
    var vw = value.result();
    if (tw != null && vw != null) {
        var tm = median_of(tw);
        var vm = median_of(vw);

        return [tm, vm];
    } else {
        return null;
    }
});
```

## Further Considerations
You have created a library that contains some functions. How do you know that
they are implemented correctly? Try to add some
[tests](https://mochajs.org/)
that increases your confidence in your code.

The `SlidingWindow` accepts an `window_size` argument. What is a good value?

Why haven't we used same the method we used to detrend the data?
