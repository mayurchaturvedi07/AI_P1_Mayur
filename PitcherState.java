
package ai_p1;
import java.util.List;
import java.util.Objects;

public class PitcherState {
    private final List<Integer> newPitchers;
    private final int newSteps;

    public PitcherState(List<Integer> pitchers, int steps) {
        this.newPitchers = pitchers;
        this.newSteps = steps;
    }

    public List<Integer> getPitchers() {
        return newPitchers;
    }

    public int getSteps() {
        return newSteps;
    }

    public int getTotal() {
        return newPitchers.stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PitcherState that = (PitcherState) o;
        return newPitchers.equals(that.newPitchers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newPitchers);
    }
}


