package src.util;

import javafx.util.StringConverter;
import src.data.Measure;

import java.util.Collection;

public class Converters {

    public static class MeasureStringConverter extends StringConverter<Measure> {

        private final Collection<Measure> measures;

        public MeasureStringConverter(Collection<Measure> measures) {
            this.measures = measures;
        }

        @Override
        public String toString(Measure object) {
            if (object == null) return null;
            return object.abbreviation();
        }

        @Override
        public Measure fromString(String string) {
            for (Measure measure : measures) {
                if (measure.abbreviation().equals(string))
                    return measure;
            }
            return null;
        }
    }

}
