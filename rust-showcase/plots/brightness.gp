set datafile separator ','
set key top left
set terminal pngcairo size 750,560

bg_color = "dark-violet"
nobg_color = "forest-green"

set output "../assets/brightness-both.png"
plot [2905:2985] "../assets/brightness.csv" using 1:2 title "Background" linecolor bg_color, "../assets/brightness.csv" using 1:3 title "No background" linecolor nobg_color 

set output "../assets/brightness-background.png"
plot [2905:2985] "../assets/brightness.csv" using 1:2 title "Background" linecolor bg_color 

set output "../assets/brightness-nobackground.png"
plot [2905:2985] "../assets/brightness.csv" using 1:3 title "No background" linecolor nobg_color 


