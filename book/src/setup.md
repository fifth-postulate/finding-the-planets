# Setup
In this chapter we will setup everything so that you can start working on the
workshop.

## Prerequisites 
The only real prerequisite is that you have a working
[Rust](https://www.rust-lang.org) tool chain. Everything else should be be
provided in the workshop resources. 

If you want to graph some of the intermediate results, a graphing tool will be
useful. We would recommend [gnuplot](http://www.gnuplot.info/).

## $CARGO_HOME
The project relies on some crates. In order to go easy on the conference
network we have provided the dependencies we envisioned. In order to make use
of these downloaded crates it is necessary to set the `$CARGO_HOME`
environment variable to `<PROJECT_RESOURCES>/cargo_home`.
