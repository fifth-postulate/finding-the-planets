.PHONY: clean

DATA-FILES := $(patsubst %,k2-trappist1-unofficial-tpf-%-cadence.fits,short long)
COMPRESSED-FILES := $(patsubst %,%.gz,DATA-FILES)

workshop.tar.gz: workshop
	tar cvfz workshop.tar.gz workshop/

workshop:
	mkdir -p workshop
	mkdir -p workshop/rust
	cp *.fits workshop
	cp -r docs workshop/book
	cp -r rust-starter workshop/rust/starter
	cp -r rust-showcase workshop/rust/showcase
	cp *.csv workshop

data: $(DATA-FILES)

%.fits: %.fits.gz
	cp $< copy.$<
	gunzip $<
	mv copy.$< $<

%.fits.gz:
	wget https://zenodo.org/record/375796/files/$@

clean:
	rm -f *.fits.gz
	rm -rf workshop*
