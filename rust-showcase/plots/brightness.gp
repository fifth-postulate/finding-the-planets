set datafile separator ','
set key top left
set terminal pngcairo size 750,560
set output "../assets/brightness.png"

plot [2905:2985] "../assets/brightness.csv" using 1:2 title "Background", "../assets/brightness.csv" using 1:3 title "No background"