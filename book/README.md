# Finding the Planets
This directory contains the sources of the book accompanying the Finding the
Planets workshop. 

## Development
### Makefile
There is a `Makefile` that transforms images to the right format.

```
make
```

Will do the appropriate transformations. If for some reason you want to remove
the images run `make clean`.

### mdBook
The book is written with the [mdBook][mkbook] tool. It is configured with the
`book.toml` inside of this directory and will output to the `docs` directory in
parent directory. This will automatically get [served by GitHub][book].

To build the book use the following command

```
mdbook build
```

[mdbook]: https://github.com/rust-lang-nursery/mdBook
[book]: http://fifth-postulate.nl/finding-the-planets/
