/*
 * Copyright 2017-2021 Cloudopt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.cloudopt.next.health

import net.cloudopt.next.core.ConfigManager
import net.cloudopt.next.core.Plugin
import net.cloudopt.next.web.NextServer

/**
 * This plugin provides a simple way to expose health checks. Health checks are used to express the current state of the
 * application in very simple terms: UP or DOWN.
 *
 * If also enabled in the settings, the /health interface is automatically opened and you can get the status of all used
 * plugins via json.
 */
class HealthChecksPlugin : Plugin {

    override fun start(): Boolean {
        /**
         * Loading configuration files.
         */
        HealthChecksManager.config =
            ConfigManager.initObject("healthChecks", HealthChecksConfig::class) as HealthChecksConfig

        HealthChecksManager.report()["applicationName"] = HealthChecksManager.config.applicationName

        NextServer.registerResourceTable(url = HealthChecksManager.config.accessPath, HealthChecksController::class)

        /**
         *
         */
        HealthChecksManager.creatTimer()

        return true
    }

    override fun stop(): Boolean {
        if (HealthChecksManager.timerId > 0) {
            HealthChecksManager.stopTimer()
        }
        return true
    }

}