package com.suji.domain.mappers

import com.suji.domain.model.Athlete
import com.suji.domain.model.SujiDevice

/**
 * Maps a Suji Device to an Athlete and Vice Versa
 */

fun Array<SujiDevice>.toMap() : Map<SujiDevice, String?>{
    val map = mutableMapOf<SujiDevice, String?>()
    this.forEach { sujiDevice ->
        map[sujiDevice] = null
    }
    return map
}

fun Array<Athlete>.toMap() : Map<Athlete, String?>{
    val map = mutableMapOf<Athlete, String?>()
    this.forEach { athlete ->
        map[athlete] = null
    }
    return map
}

