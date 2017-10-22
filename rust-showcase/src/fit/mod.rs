use std::iter::Iterator;

pub struct FloatIterator {
    start: f64,
    finish: f64,
    step: f64,
    current: u64,
}

impl FloatIterator {
    pub fn new(start: f64, finish: f64, step: f64) -> Self {
        Self { current: 0, start, finish, step }
    }
}

impl Iterator for FloatIterator {
    type Item = f64;

    fn next(&mut self) -> Option<Self::Item> {
        let value = self.start + self.step * (self.current as f64);

        if value <= self.finish {
            self.current += 1;

            Some(value)
        } else {
            None
        }
    }
}


#[cfg(test)]
mod tests {
    use super::*;

    const EPSILON: f64 = 0.000001;

    #[test]
    fn float_iterator_should_collect_floating_point_numbers() {
        let mut iter = FloatIterator::new(0.0, 1.0, 0.2);

        assert!(are_near(iter.next().unwrap(), 0.0f64));
        assert!(are_near(iter.next().unwrap(), 0.2f64));
        assert!(are_near(iter.next().unwrap(), 0.4f64));
        assert!(are_near(iter.next().unwrap(), 0.6f64));
        assert!(are_near(iter.next().unwrap(), 0.8f64));
        assert!(are_near(iter.next().unwrap(), 1.0f64));
        assert_eq!(iter.next(), None);
    }

    fn are_near(a: f64, b: f64) -> bool {
        (a - b).abs() <= EPSILON
    }
}


