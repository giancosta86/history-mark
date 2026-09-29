package info.gianlucacosta.historymark.util;

import java.time.LocalDate;

public interface HistoryDateFormat {
    static String formatDate(LocalDate date) {
        return (date.getMonthValue() == 1 &&
                date.getDayOfMonth() == 1) ?

                String.valueOf(date.getYear())
                :
                String.format("%d-%d-%d",
                        date.getYear(),
                        date.getMonthValue(),
                        date.getDayOfMonth()
                );
    }
}
