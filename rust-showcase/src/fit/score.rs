pub fn least_squares(xs: &Vec<f64>, ys: &Vec<f64>) -> f64 {
    xs.iter().zip(ys)
        .map(|(a,b)| (a-b).powi(2))
        .sum()
}

#[cfg(test)]
mod tests {
    use super::*;

    const EPSILON: f64 = 0.0000001;

    #[test]
    fn should_determine_score() {
        let xs: Vec<f64> = vec!(0.0, 1.0, 2.0);
        let ys: Vec<f64> = vec!(0.0, 0.0, 0.0);

        let result = least_squares(&xs, &ys);

        assert!(are_near(result, 5.0));
    }

    fn are_near(x: f64, y: f64) -> bool {
        (x - y) <= EPSILON
    }
}
