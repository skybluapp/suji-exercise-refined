package com.suji.domain.connectedSujiDevices

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.suji.domain.model.Athlete
import com.suji.domain.model.ConnectionStatus
import com.suji.domain.model.Limb
import com.suji.domain.model.SujiDevice
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class ConnectedSujiDevicesTest{

    val csd = ConnectedSujiDevicesBimap()

    //These will collect the flow from connected suji devices (basically acting as the viewmodel)

    var unassignedAthletes : StateFlow<List<Athlete>> = csd.unassignedAthletes
    var unassignedSujiDevice : StateFlow<List<SujiDevice>> = csd.unassignedSujiDevices


    @Test
    @ExperimentalCoroutinesApi
    fun easyTest(){
        assertTrue(true)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun checkStartingSujisAreAdded(){
        assert(unassignedSujiDevice.value.isNotEmpty())
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testAddAthletes(){
        val x = 20
        //Add x athletes
        for(i in 1..20){
            val athlete = Athlete(uid = "uid$i", name = "name$i", lopArm = 100 + i, lopLeg = 200 + i, institutionLogoUrl = "logo$i")
            csd.addAthlete(athlete)
            val retrievedAthlete = csd.getAthlete("uid$i")
            assertNotNull(retrievedAthlete)
        }
        assertEquals(csd.getNumberOfAthletes(), x)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun testAddSujiDevices(){
        val x = 5
        //Add x suji devices
        for(i in 1..x){
            val sujiDevice = SujiDevice(name = "Suji-$i")
            csd.addSujiDevice(sujiDevice)
            assertNotNull(csd.getSujiDevice("Suji-$i"))
        }

        assertEquals(csd.getNumberOfSujis(), 8)
    }

    @ExperimentalCoroutinesApi
    @Test
     fun connectedSujiDeviceTest() = runTest{

        testAddAthletes()
        testAddSujiDevices()

        System.out.println(csd.getAthlete(athleteUID = "123"))
        System.out.println(csd.getSujiDevice(deviceName = "abc"))

        var badConnection = csd.connectSujiToAthlete(deviceName = "abc", athleteUID = "123")
        assertFalse(badConnection)

        var connectionStatus = csd.getSujiDevice("Suji-1")?.connectionStatus
        assertEquals(connectionStatus, ConnectionStatus.DISCONNECTED)

        val connection = csd.connectSujiToAthlete(deviceName = "Suji-1", athleteUID = "uid1")
        assertTrue(connection)
//
        val testSujiDevice =  csd.getSujiDevice("Suji-1")
        assertEquals(testSujiDevice!!.connectionStatus, ConnectionStatus.CONNECTED)
        assertEquals(csd.athleteDeviceMap.value[csd.getAthlete("uid1")]?.name , "Suji-1" )

        val testAthlete =  csd.getAthlete("uid1")
        assertEquals(csd.athleteDeviceMap.value.inverse()[csd.getSujiDevice("Suji-1")]?.uid , "uid1" )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun testReplaceSujiDevice() = runTest{

        testAddSujiDevices()
        var testSujiDevice = csd.getSujiDevice("Suji-1")!!
        assertNull(testSujiDevice.assignedLimb)
        csd.replaceSujiDevice(testSujiDevice.name, testSujiDevice.copy(assignedLimb = Limb.ARM, connectionStatus = ConnectionStatus.CONNECTING))
        testSujiDevice =  csd.getSujiDevice("Suji-1")!!
        assertEquals(testSujiDevice.assignedLimb,Limb.ARM)
        assertEquals(testSujiDevice.connectionStatus,ConnectionStatus.CONNECTING)



    }


    @ExperimentalCoroutinesApi
    @Test
    fun bimapTest() = runTest{

        val biMap: BiMap<Athlete, Int?> = HashBiMap.create()
        biMap[Athlete("12d3", name = "hello", institutionLogoUrl = "jkd", lopLeg = 22, lopArm = 22)] =
            null
        biMap.forcePut(Athlete("123", name = "hello", institutionLogoUrl = "jkd", lopLeg = 22, lopArm = 22), null)

        System.out.println(biMap)


    }





}