package org.homyspace.mousegrid

data class Size(val width: Int, val height: Int) {

    init {
        require(width > 0) { "'width' should be grater or equal than 0" }
        require(height > 0) { "'height' should be grater or equal than 0" }
    }

}
