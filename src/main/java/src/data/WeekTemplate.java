package src.data;

import java.util.List;

public record WeekTemplate(
        String date,
        List<List<RecipeInfo>> days
) {

    public boolean isEmpty() {
        return days.stream().allMatch(List::isEmpty);
    }

}
