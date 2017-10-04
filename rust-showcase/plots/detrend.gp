set datafile separator ','
set key top left
set terminal pngcairo size 750,560

brightness_color = "forest-green"
trend_color = "red"
difference_color = brightness_color

set output "../assets/trend.png"
plot [2905:2985] "../assets/detrend.csv" using 1:2 title "brightness" linecolor brightness_color,"../assets/detrend.csv" using 1:3 title "trend" linecolor trend_color

set output "../assets/detrend.png"
plot [2905:2985] "../assets/detrend.csv" using 1:4 title "detrended brightness" linecolor difference_color