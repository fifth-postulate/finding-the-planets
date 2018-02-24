fits = require('fits');

fits.readFile('../k2-trappist1-unofficial-tpf-long-cadence.fits', function(error, FITS){
    if (error) return console.error(error);
    console.log(FITS.HDU.primary);
});
