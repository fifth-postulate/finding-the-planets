function median_of(values){
    const data = values.slice();
    data.sort(function(a,b){ return b < a; });

    const n = data.length;
    const middle = Math.floor(n / 2);
    if (n % 2 == 1) {
        return data[middle];
    } else {
        return (data[middle] + data[middle - 1]) / 2.0;
    }
};

module.exports = {
    median_of: median_of
};
