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

const SlidingWindow = function(size){
    this.size = size;
    this.window = [];
};
SlidingWindow.prototype.push = function(value){
    this.window.push(value);
    if (this.window.length > this.size) {
        this.window = this.window.slice(1);
    }
};
SlidingWindow.prototype.result = function(){
    if (this.window.length === this.size) {
        return this.window.slice();
    } else {
        return null;
    }
};

module.exports = {
    median_of: median_of,
    sliding_window: SlidingWindow
};
