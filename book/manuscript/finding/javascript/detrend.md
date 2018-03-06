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

Our data has two values, one for time and one for the brightness. We our
responsible for turning the raw columns of our CSV into floating point values, 
perform our calculation and selecting the correct ones.

Up until now we never returned more than two or three values. For our current
plan we are going to return more. In order to keep track of our data, we are
going to create a datastructure.

```javascript
var detrend_data {
    time: 0.0,
    brightness: 0.0,
    trend: 0.0,
    difference: 0.0,
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
 
Let's implement this algorithm. With our `detrend_data` structure, we have an
opportunity to directly implement the different branches of our algorithm. Since
our data gets delivered to us in as a pair of floating point values, we better accept it as
an argument in our transformer and pick the pair apart.

```javascript
const time = parseFloat(data[0]);
const brightness = parseFloat(data[1]);
```

It is little more than giving things in the name. Next we will use the
previous `detrend_data` that we have, and use it to determine what the next
`detrend_data` should be. Because this depends on the new data and the parameter
\\(\alpha\\), we better use them both.

```javascript
const trend = alpha * brightness + (1 - alpha) * previous_detrend_data.trend;
const difference = brightness - trend;
detrend_data = {
    'time': time,
    'brightness': brightness,
    'trend': trend,
    'difference': difference
};
```

We calculate the `trend` as described in the algorithm, and calculate the
difference from the brightness and the freshly calculated trend.

But where does the `previous_detrend_data` come from? We initialize it outside
the transformer to `undefined`, and check if we processed a
`previous_detrend_data` or that we need to initialize it

```javascript
var detrend_data;
if (previous_detrend_data) {
    /* calculate the detrend_data  */
} else {
    /* initialize detrend_data */
}
previous_detrend_data = detrend_data;
```

We initialize the `detrend_data` by assign sane values to it.

```javascript
detrend_data = {
    'time': time,
    'brightness': brightness,
    'trend': brightness,
    'difference': 0
};
```

In either way, we set the `previous_detrend_data` to our current `detrend_data`
so that it is ready for the next row.

Now that we have our data, we can let our csv pipeline process it. We only need
to return an array of the data we are interested in.

```javascript
return [
    detrend_data.time, 
    detrend_data.brightness, 
    detrend_data.trend, 
    detrend_data.difference
];
```

## Further Considerations
How does the weighted detrend behave for known functions? Try to plot an step
function, i.e. a series that starts out 0 and than is 1 through out, and detrend
it.

What other kind of detrend strategies can you come up with?
