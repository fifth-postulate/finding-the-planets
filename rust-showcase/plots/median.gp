set datafile separator ','
set key top left
set terminal pngcairo size 750,560

color = "#228b22"

set output "../assets/median.png"
plot [2905:2985] "../assets/median.csv" using 1:2 title "median" with lines linecolor rgb color 