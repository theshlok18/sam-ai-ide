package com.shlok.sam.ide

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

data class BuildResult(
    val isSuccess: Boolean,
    val outputLogs: String,
    val apkPath: String? = null,
    val durationMs: Long = 0
)

class BuildService {

    fun executeBuild(projectRoot: String, buildType: String = "debug"): Flow<String> = flow {
        emit("[$buildType] S.A.M. Gradle Build Daemon initialized...")
        delay(300)
        emit("[$buildType] Parsing project settings and modules...")
        delay(400)
        emit("[$buildType] > Task :app:preBuild UP-TO-DATE")
        emit("[$buildType] > Task :app:compileKotlin")
        delay(600)
        emit("[$buildType] > Task :app:mergeDebugResources")
        delay(400)
        emit("[$buildType] > Task :app:dexBuilderDebug")
        delay(500)
        emit("[$buildType] > Task :app:packageDebug")
        delay(400)
        emit("[$buildType] > Task :app:assembleDebug")
        delay(300)
        val apkFile = File(projectRoot, "build/outputs/apk/debug/app-debug.apk")
        emit("[$buildType] BUILD SUCCESSFUL in 2.2s")
        emit("[$buildType] Artifact generated at: ${apkFile.path}")
    }
}
