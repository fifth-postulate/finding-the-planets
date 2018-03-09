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
We are defining a threshold beyond which we will discard our data.

```javascript
const threshold = 200.00;
```

Next we will use that threshold to in our data to discard our actual data.
Discarding can be achieved by return `null` instead of an array of data.

```javascript
const time = parseFloat(data[0]);
const brightness = parseFloat(data[1]);
const trend = parseFloat(data[2]);
const difference = parseFloat(data[3]);

if (Math.abs(difference) <= threshold) {
    return data;
} else {
    return null;
}
```

## Further Considerations
The algorithm above depends on a certain threshold. What value should we use?
Try some different values and try to get a feel for what works. Discuss your
choices with somebody else.
