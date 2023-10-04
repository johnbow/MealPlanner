package src.util;

import javafx.util.StringConverter;
import src.data.Database;
import src.data.Measure;

import java.util.Collection;

public class Converters {

    public static class MeasureAbbrevStringConverter extends StringConverter<Measure> {

        @Override
        public String toString(Measure object) {
            if (object == null) return null;
            return object.abbreviation();
        }

        @Override
        public Measure fromString(String string) {
            for (Measure measure : Database.getMeasures()) {
                if (measure.abbreviation().equals(string))
                    return measure;
            }
            return null;
        }
    }


    public static class MeasureStringConverter extends StringConverter<Measure> {

        private Measure.Number number;

        public MeasureStringConverter(Measure.Number initial) {
            this.number = initial;
        }

        @Override
        public String toString(Measure object) {
            if (object == null) return null;
            return object.getName(number);
        }

        @Override
        public Measure fromString(String string) {
            for (Measure measure : Database.getMeasures()) {
                if (measure.getName(number).equals(string))
                    return measure;
            }
            return null;
        }

        public void setNumber(Measure.Number num) {
            this.number = num;
        }
    }

}
