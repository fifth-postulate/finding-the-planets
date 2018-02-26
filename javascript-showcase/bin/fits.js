fits = require('fits');

// fits.readFile('../k2-trappist1-unofficial-tpf-long-cadence.fits', function(error, FITS){
//     if (error) return console.error(error);
//     console.log(FITS.HDU.primary);
// });

// fits.readFile('../k2-trappist1-unofficial-tpf-long-cadence.fits', function(error, FITS){
//     if (error) return console.error(error);
//     console.log(FITS.HDU.extensions[0]);
// });

fits.readFile('../k2-trappist1-unofficial-tpf-long-cadence.fits', function(error, FITS){
    if (error) return console.error(error);
    console.log(FITS.images[1]);
});
