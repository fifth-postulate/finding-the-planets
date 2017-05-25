.PHONY: clean

DATA-FILES := $(patsubst %,k2-trappist1-unofficial-tpf-%-cadence.fits,short long)
COMPRESSED-FILES := $(patsubst %,%.gz,DATA-FILES)

data: $(DATA-FILES)

%.fits: %.fits.gz
	cp $< copy.$<
	gunzip $<
	mv copy.$< $<

%.fits.gz:
	wget https://zenodo.org/record/375796/files/$@

clean:
	rm *.fits.gz
