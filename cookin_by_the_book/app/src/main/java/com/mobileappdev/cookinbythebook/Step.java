package com.mobileappdev.cookinbythebook;

public class Step {
    private String step;

    public Step(String step) {
        this.step = step;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "Step{" +
                "step='" + step + '\'' +
                '}';
    }
}
