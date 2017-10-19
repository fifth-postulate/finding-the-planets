set datafile separator ','
set key top left
set terminal pngcairo size 750,560

brightness_color = "#228b22"
trend_color = "#9400d3"
difference_color = brightness_color

set output "../assets/trend.png"
plot [2905:2985] "../assets/detrend.csv" using 1:2 title "brightness" linecolor rgb brightness_color,"../assets/detrend.csv" using 1:3 title "trend" linecolor rgb trend_color

set output "../assets/detrend.png"
plot [2905:2985] "../assets/detrend.csv" using 1:4 title "detrended brightness" linecolor rgb difference_color