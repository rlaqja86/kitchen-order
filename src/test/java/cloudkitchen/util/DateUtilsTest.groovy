package cloudkitchen.util

import spock.lang.Specification

class DateUtilsTest extends Specification {

    def "DateUtils.addMilliseconds will add 1000ms to currentDate" () {
        given:
        int diff = 1000
        String current = 'October 29, 2022 03:24:00';

        Date after = new Date(current)
        Date before = new Date(current)
        when:
        after = DateUtils.addMilliseconds(after, diff)
        then:
        after.getTime() - before.getTime() == diff

    }

    def "DateUtils.getDiffence will return difference between two date" () {
        given:
        String current = 'October 29, 2022 03:24:00';
        String afterOneSeconds = 'October 29, 2022 03:24:01';

        Date after = new Date(afterOneSeconds)
        Date before = new Date(current)
        when:
        int diff = DateUtils.getDifference(before, after)
        then:
        diff == 1

    }
}
