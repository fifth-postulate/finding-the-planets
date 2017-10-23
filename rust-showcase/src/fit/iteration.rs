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

pub struct FloatRange {
    start: f64,
    finish: f64,
    step: f64,
}

impl FloatRange {
    pub fn new(start: f64, finish: f64, step: f64) -> FloatRange {
        FloatRange { start, finish, step }
    }

    fn index(&self, index: u64) -> Option<f64> {
        let value = self.start + self.step * (index as f64);

        if value <= self.finish {
            Some(value)
        } else {
            None
        }
    }
}

pub struct TupleIterator {
    first: FloatRange,
    second: FloatRange,
    current: (u64, u64),
}

impl TupleIterator {
    pub fn new(first : FloatRange, second: FloatRange) -> Self {
        Self { current: (0,0), first, second }
    }
}

impl Iterator for TupleIterator {
    type Item = (f64, f64);

    fn next(&mut self) -> Option<Self::Item> {
        let (first_index, second_index) = self.current;

        match self.first.index(first_index) {
            Some(first_value) => {
                match self.second.index(second_index) {
                    Some(second_value) => {
                        self.current = (first_index, second_index + 1);

                        Some((first_value, second_value))
                    },

                    None => {
                        self.current = (first_index + 1, 0);

                        self.next()
                    },
                }
            },

            None => None,
        }
    }
}

pub struct TransitParametersIterator {
    first: FloatRange,
    second: FloatRange,
    third: FloatRange,
    fourth: FloatRange,
    fifth: FloatRange,
    sixth: FloatRange,
    current: (u64, u64, u64, u64, u64, u64),
}

impl TransitParametersIterator {
    pub fn new(first: FloatRange, second: FloatRange, third: FloatRange, fourth: FloatRange, fifth: FloatRange, sixth: FloatRange) -> Self {
        Self { current: (0,0,0,0,0,0), first, second, third, fourth, fifth, sixth }
    }
}

impl Iterator for TransitParametersIterator {
    type Item = (f64, f64, f64, f64, f64, f64);

    fn next(&mut self) -> Option<Self::Item> {
        let (index0, index1, index2, index3, index4, index5) = self.current;

        match self.first.index(index0) {
            Some(value0) => {
                match self.second.index(index1) {
                    Some(value1) => {
                        match self.third.index(index2) {
                            Some(value2) => {
                                match self.fourth.index(index3) {
                                    Some(value3) => {
                                        match self.fifth.index(index4) {
                                            Some(value4) => {
                                                match self.sixth.index(index5) {
                                                    Some(value5) => {
                                                        self.current = (index0, index1, index2, index3, index4, index5 + 1);

                                                        Some((value0, value1, value2, value3, value4, value5))
                                                    },

                                                    None => {
                                                        self.current = (index0, index1, index2, index3 + 1, 0, 0);

                                                        self.next()
                                                    },
                                                }
                                            },

                                            None => {
                                                self.current = (index0, index1, index2, index3 + 1, 0, 0);

                                                self.next()
                                            },
                                        }
                                    },

                                    None => {
                                        self.current = (index0, index1, index2 + 1, 0, 0, 0);

                                        self.next()
                                    },
                                }
                            },

                            None => {
                                self.current = (index0, index1 + 1, 0, 0, 0, 0);

                                self.next()
                            },
                        }
                    },

                    None => {
                        self.current = (index0 + 1, 0, 0, 0, 0, 0);

                        self.next()
                    },
                }
            },

            None => None,
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


    #[test]
    fn tuple_iterator_should_collect_floating_point_tuples() {
        let first = FloatRange::new(0.0, 0.2, 0.2);
        let second = FloatRange::new(0.0, 0.2, 0.1);
        let mut iter = TupleIterator::new(first, second);

        let (f, s) = iter.next().unwrap();
        assert!(are_near(f, 0.0f64));
        assert!(are_near(s, 0.0f64));
        let (f, s) = iter.next().unwrap();
        assert!(are_near(f, 0.0f64));
        assert!(are_near(s, 0.1f64));
        let (f, s) = iter.next().unwrap();
        assert!(are_near(f, 0.0f64));
        assert!(are_near(s, 0.2f64));
        let (f, s) = iter.next().unwrap();
        assert!(are_near(f, 0.2f64));
        assert!(are_near(s, 0.0f64));
        let (f, s) = iter.next().unwrap();
        assert!(are_near(f, 0.2f64));
        assert!(are_near(s, 0.1f64));
        let (f, s) = iter.next().unwrap();
        assert!(are_near(f, 0.2f64));
        assert!(are_near(s, 0.2f64));
        assert_eq!(iter.next(), None);
    }

    fn are_near(a: f64, b: f64) -> bool {
        (a - b).abs() <= EPSILON
    }
}
