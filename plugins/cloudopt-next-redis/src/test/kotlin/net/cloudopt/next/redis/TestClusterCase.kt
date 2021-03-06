package net.cloudopt.next.redis

import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import net.cloudopt.next.core.ConfigManager
import net.cloudopt.next.core.Resourcer
import net.cloudopt.next.core.Worker
import net.cloudopt.next.web.NextServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class TestClusterCase {

    @ExperimentalCoroutinesApi
    @Before
    fun init() {
        NextServer.webConfig.env = "cluster"
        val newConfigFileName = "application-${NextServer.webConfig.env}.json"
        ConfigManager.configMap.putAll(Resourcer.read(newConfigFileName))
        RedisPlugin().start()
        Dispatchers.setMain(Worker.dispatcher())
    }

    @ExperimentalCoroutinesApi
    @After
    fun clear() {
        Dispatchers.resetMain()
    }

    @ExperimentalLettuceCoroutinesApi
    @Test
    fun getAndSet() = runBlocking {
        var value = RedisManager.clusterCoroutines().getset("testAloneGetSet", "success")
        if (value.isNullOrBlank()) {
            value = RedisManager.clusterCoroutines().getset("testAloneGetSet", "success")
        }
        assert(value == "success")
    }


}