
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

pub fn groups(slice: Vec<f64>, group_size: usize) -> Vec<Vec<f64>> {
    let mut groups: Vec<Vec<f64>> = vec!();

    for end_index in group_size .. slice.len() + 1 {
        let mut group: Vec<f64> = vec!();
        for index in (end_index - group_size) .. end_index {
            group.push(slice[index])
        }
        groups.push(group)
    }

    groups
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

    #[test]
    fn groups_of_2_for_a_vector() {
        let data: Vec<f64> = vec!(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);

        let gs = groups(data, 2);

        assert_eq!(vec![
            vec!(1.0, 2.0),
            vec!(2.0, 3.0),
            vec!(3.0, 4.0),
            vec!(4.0, 5.0),
            vec!(5.0, 6.0)], gs);
    }

    #[test]
    fn groups_of_3_for_a_vector() {
        let data: Vec<f64> = vec!(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);

        let gs = groups(data, 3);

        assert_eq!(vec![
            vec!(1.0, 2.0, 3.0),
            vec!(2.0, 3.0, 4.0),
            vec!(3.0, 4.0, 5.0),
            vec!(4.0, 5.0, 6.0)], gs);
    }
}
