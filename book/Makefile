IMAGE_DIRECTORY = src/image
PSS  = $(wildcard $(IMAGE_DIRECTORY)/*.ps)
PNGS = $(patsubst $(IMAGE_DIRECTORY)/%.ps,$(IMAGE_DIRECTORY)/%.png, $(PSS))

all: $(PNGS);

$(IMAGE_DIRECTORY)/%.png: $(IMAGE_DIRECTORY)/%.ps
		convert -density 400 -resize 750 $< $@

clean:
	rm $(IMAGE_DIRECTORY)/*.png
