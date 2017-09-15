.PHONY: clean

DATA-FILES := $(patsubst %,k2-trappist1-unofficial-tpf-%-cadence.fits,short long)
COMPRESSED-FILES := $(patsubst %,%.gz,DATA-FILES)

workshop:
	mkdir -p workshop
	cp *.fits workshop
	cp -r docs workshop/book

data: $(DATA-FILES)

%.fits: %.fits.gz
	cp $< copy.$<
	gunzip $<
	mv copy.$< $<

%.fits.gz:
	wget https://zenodo.org/record/375796/files/$@

clean:
	rm -f *.fits.gz
	rm -rf workshop
