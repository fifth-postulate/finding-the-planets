.PHONY: clean

DATA-FILES := $(patsubst %,k2-trappist1-unofficial-tpf-%-cadence.fits.gz,short long)

data: $(DATA-FILES)

%.fits.gz:
	wget https://zenodo.org/record/375796/files/$@

clean:
	rm *.fits.gz
