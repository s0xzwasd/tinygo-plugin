package org.jetbrains.tinygoplugin.configuration

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

interface TinyGoConfiguration : UserConfiguration, ProjectConfiguration {
    fun deepCopy(): TinyGoConfiguration
    fun saveState(p: Project)
    fun modified(p: Project): Boolean

    companion object {
        fun getInstance(p: Project): TinyGoConfiguration = TinyGoConfigurationImpl(
            projectConfig = p.service<ProjectConfigurationImpl>().state,
            userConfig = p.service<UserConfigurationImpl>().state,
        )
        fun getInstance(): TinyGoConfiguration = TinyGoConfigurationImpl()
        fun getInstance(
            userConfig: UserConfigurationState = UserConfigurationState(),
            projectConfig: ProjectConfigurationState = ProjectConfigurationState()
        ): TinyGoConfiguration {
            return TinyGoConfigurationImpl(userConfig, projectConfig)
        }
    }
}

data class TinyGoConfigurationImpl(
    private val userConfig: UserConfigurationState = UserConfigurationState(),
    private val projectConfig: ProjectConfigurationState = ProjectConfigurationState(),
) : TinyGoConfiguration, UserConfiguration by userConfig, ProjectConfiguration by projectConfig {

    override fun saveState(p: Project) {
        p.service<ProjectConfigurationImpl>().projectState = projectConfig.copy()
        p.service<UserConfigurationImpl>().userConfigurationState = userConfig.copy()
    }

    override fun modified(p: Project): Boolean {
        val currentSettings = getInstance(p)
        return currentSettings.projectConfig != projectConfig ||
            currentSettings.userConfig != userConfig
    }

    override fun deepCopy(): TinyGoConfigurationImpl {
        val projectConfigurationCopy = projectConfig.copy()
        val userConfigurationCopy = userConfig.copy()
        return TinyGoConfigurationImpl(
            projectConfig = projectConfigurationCopy,
            userConfig = userConfigurationCopy,
        )
    }

    companion object {
        fun getInstance(p: Project): TinyGoConfigurationImpl = TinyGoConfigurationImpl(
            projectConfig = p.service<ProjectConfigurationImpl>().state,
            userConfig = p.service<UserConfigurationImpl>().state,
        )
    }
}