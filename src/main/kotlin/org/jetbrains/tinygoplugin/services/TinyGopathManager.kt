package org.jetbrains.tinygoplugin.services

import com.goide.project.GoRootsProvider
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.tinygoplugin.TinyGoConfiguration
import java.nio.file.Paths

class TinyGopathManager : GoRootsProvider {

    private fun getTinyGoRoot(project: Project, subfolder: String? = null): VirtualFile? {
        val settings = TinyGoConfiguration.getInstance(project)
        val tinyGoSDKPath = settings.tinyGoSDKPath
        val result = if (subfolder == null) Paths.get(tinyGoSDKPath) else Paths.get(tinyGoSDKPath, subfolder)

        return VfsUtil.findFileByIoFile(result.toFile(), true)
    }

    override fun getGoPathRoots(project: Project?, p1: Module?): MutableCollection<VirtualFile> {
        if (project == null) {
            return mutableSetOf()
        }
        val tinyGoRoot = getTinyGoRoot(project, null)
        return if (tinyGoRoot == null) {
            mutableSetOf()
        } else {
            mutableSetOf(tinyGoRoot)
        }
    }

    override fun getGoPathSourcesRoots(project: Project?, p1: Module?): MutableCollection<VirtualFile> {
        if (project == null) {
            return mutableSetOf()
        }
        val tinyGoRoot = getTinyGoRoot(project, "src")
        return if (tinyGoRoot == null) {
            mutableSetOf()
        } else {
            mutableSetOf(tinyGoRoot)
        }
    }

    override fun getGoPathBinRoots(p0: Project?, p1: Module?): MutableCollection<VirtualFile> {
        return mutableSetOf()
    }

    override fun isExternal(): Boolean = true
}
