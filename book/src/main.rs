extern crate mdbook;

use mdbook::MDBook;

fn main() {
    let root_dir = ".";

    let md = MDBook::load(root_dir)
        .expect("unable to load book");

    md.build().expect("build failed");
}
