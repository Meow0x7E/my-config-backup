import java.io.File

val userHome = File(System.getenv("HOME") ?: throw GradleException("不是哥们你 HOME 目录跑哪去了？"))

tasks.register("install") {
    dependsOn(tasks.getByName("installConfig"))
}

tasks.register("installConfig") {
    val copyToConfig = fun(directoryName: String, lambda: ((File, File) -> Unit)?): Boolean {
        val source = File("${projectDir.absoluteFile}/assets/config", directoryName).apply {
            if (!exists()) throw GradleException("好好好，source 飞了是吧")
        }

        val target = File("${userHome.absoluteFile}/.config", directoryName).apply {
            if (exists()) {
                //renameTo(File("${absolutePath}.bak"))
                println("\u001B[38;2;255;255;0mRename: \u001B[38;2;255;255;255m${absolutePath} \u001B[0m-> \u001B[38;2;255;255;255m${absolutePath}.bak\u001B[0m")
            }
        }
        try {
            //source.copyRecursively(target)
            println("\u001B[38;2;0;255;0mCopy: \u001B[38;2;255;255;255m${source.absolutePath} \u001B[0m-> \u001B[38;2;255;255;255m${target.absolutePath}\u001B[0m")
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        lambda?.let { it(source, target) }
        return true
    }

    arrayOf("fastfetch", "nvim").forEach { copyToConfig(it, null) }

    copyToConfig("zsh") { source, _ ->
        File(source, "zshrc").copyTo(File(userHome, ".zshrc").apply {
            if (exists()) {
                //renameTo(File("${absolutePath}.bak"))
                println("\u001B[38;2;255;255;0mRename: \u001B[38;2;255;255;255m${absolutePath} \u001B[0m-> \u001B[38;2;255;255;255m${absolutePath}.bak\u001B[0m")
            }
        })
    }
}