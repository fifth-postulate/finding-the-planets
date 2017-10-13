
pub fn median_of(slice: &[f64]) -> f64 {
    let n = slice.len();
    let mut copy = vec!(0f64; n);
    copy.copy_from_slice(&slice);
    copy.sort_by(|a,b| a.partial_cmp(b).unwrap());
    let middle: usize = n / 2;

    let result = if n % 2 == 1 {
        slice[middle]
    } else {
        (slice[middle] + slice[middle - 1])/2.0
    };

    result.clone()
}

#[cfg(test)]
pub mod tests {
    const EPSILON: f64 = 0.00001;
    use super::*;

    #[test]
    fn median_of_odd_length_vector() {
        let data: Vec<f64> = vec!(1.0, 2.0, 3.0);

        let median = median_of(&data);

        assert!((median - 2.0).abs() < EPSILON);
    }

    #[test]
    fn median_of_even_length_vector() {
        let data: Vec<f64> = vec!(1.0, 2.0, 3.0, 4.0);

        let median = median_of(&data);

        assert_eq!(median, 2.5);
        assert!((median - 2.5).abs() < EPSILON);
    }
}
