#[derive(Debug, Clone)]
pub struct Transit {
    period: f64,
    base: f64,
    depth: f64,
    duration: f64,
    decay: f64,
    phase: f64,
}

impl Transit {
    pub fn new((period, base, depth, duration, decay, phase): (f64,f64,f64,f64,f64,f64)) -> Transit {
        Transit { period, base, depth, duration, decay, phase }
    }

    pub fn value(&self, time: &f64) -> f64 {
        let k = (time/self.period).floor();
        let t = time - k * self.period;

        if t < self.phase {
            self.base
        } else if t < (self.phase + self.decay) {
            let start = self.phase;
            let f = (t - start) / self.decay;
            self.base - f * self.depth
        } else if t < (self.phase + self.decay + self.duration) {
            self.base - self.depth
        } else if t < (self.phase + self.decay + self.duration + self.decay) {
            let start = self.phase + self.decay + self.duration;
            let f = (t - start) / self.decay;
            self.base - (1f64 - f) * self.depth
        } else {
            self.base
        }
    }
}
