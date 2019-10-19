package org.homyspace.mousegrid

import spock.lang.Specification

class SizeSpec extends Specification {

    def 'can be created width valid'() {
        when:
        Size size = new Size(5, 10)

        then:
        size.getWidth() == 5
        size.getHeight() == 10
    }

    def 'complains with no valid dimensions'() {
        when:
        new Size(-5, 10)

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "'width' should be grater or equal than 0"
    }

}
