package sos.util;

import java.util.ArrayList;
import java.util.List;

public class X {

    public static void main(String[] args) {
        int LIMIT_IN_CLAUSE = 1000;
        List<Long> uncompletedTaskHistoryIds = new ArrayList<Long>();
        for (int i = 0; i < 1008; i++) {
            uncompletedTaskHistoryIds.add(new Long(i));
        }

        int size = uncompletedTaskHistoryIds.size();
        if (size > 0) {
            if (size > LIMIT_IN_CLAUSE) {

                for (int i = 0; i < size; i += LIMIT_IN_CLAUSE) {
                    size = uncompletedTaskHistoryIds.size();
                    if (i < size) {
                        List<Long> subList;
                        if (size > i + LIMIT_IN_CLAUSE) {
                            System.out.println(i + " --" + (i + LIMIT_IN_CLAUSE));
                            subList = uncompletedTaskHistoryIds.subList(i, (i + LIMIT_IN_CLAUSE));
                        } else {
                            System.out.println(i + "__" + size);
                            subList = uncompletedTaskHistoryIds.subList(i, size);
                        }
                        uncompletedTaskHistoryIds.remove(1L);
                        uncompletedTaskHistoryIds.remove(2L);
                        uncompletedTaskHistoryIds.remove(3L);
                        uncompletedTaskHistoryIds.remove(4L);
                        uncompletedTaskHistoryIds.remove(5L);
                        uncompletedTaskHistoryIds.remove(6L);

                    }

                }
            }

        }
    }
}
