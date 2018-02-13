.PHONY: clean

DATA-FILES := $(patsubst %,k2-trappist1-unofficial-tpf-%-cadence.fits,short long)
COMPRESSED-FILES := $(patsubst %,%.gz,DATA-FILES)
TARGET = workshop

$(TARGET).tar.gz: $(TARGET)
	tar cvfz $(TARGET).tar.gz $(TARGET)/

$(TARGET):
	mkdir -p $(TARGET)
	mkdir -p $(TARGET)/rust
	cp *.fits $(TARGET)
	cp -r docs $(TARGET)/book
	cp -r rust-starter $(TARGET)/rust/starter
	cp -r rust-showcase $(TARGET)/rust/showcase
	cp *.csv $(TARGET)

data: $(DATA-FILES)

%.fits: %.fits.gz
	cp $< copy.$<
	gunzip $<
	mv copy.$< $<

%.fits.gz:
	wget https://zenodo.org/record/375796/files/$@

clean:
	rm -f *.fits.gz
	rm -rf $(TARGET)*
