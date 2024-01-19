import org.fusesource.jansi.Ansi
import org.fusesource.jansi.Ansi.Color.*
import java.io.File
import java.nio.file.Files

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
    dependencies {
        classpath("org.fusesource.jansi:jansi:1.18")
    }
}

val userHome = File(System.getenv("HOME") ?: throw GradleException("不是哥们你 HOME 目录跑哪去了？"))
var cleanBackup = false
//清理过后肯定就不能再备份了
var doNotBackup: Boolean = false
    get() = if (cleanBackup) {
        true
    } else {
        field
    }
//为了防止灾难性的配置破坏它必须和 cleanBackup 互斥
var backupAndDeleteIt: Boolean = true
    get() = if (cleanBackup && field) {
        Ansi.ansi().run {
            a("警告:", YELLOW)
            a("由于与 ")
            a("cleanBackup", BLUE)
            a(" 互斥所以 ")
            a("backupAndDeleteIt", BLUE)
            a(" 已失效")
            toString()
        }.let(::println)
        false
    } else {
        field
    }

tasks.register("clean") {
    cleanBackup = true
    dependsOn("install")
}

tasks.register("install") {
    doNotBackup = false
    backupAndDeleteIt = true
    dependsOn(tasks.getByName("installConfig"))
}

tasks.register("installConfig") {
    val copyToConfig = fun(directoryName: String, lambda: ((File, File) -> Unit)?) {
        val source = File("${projectDir.absoluteFile}/assets/config", directoryName).apply {
            if (!exists()) throw GradleException("好好好，source 飞了是吧")
        }

        val target = File("${userHome.absoluteFile}/.config", directoryName).apply {
            if (exists()) printAndRenameToBak()
        }

        source.printAndCopyRecursively(target)

        lambda?.let { it(source, target) }
    }

    arrayOf(
        "fastfetch", "nvim"
    ).forEach { copyToConfig(it, null) }

    copyToConfig("zsh") { source, _ ->
        File(source, "zshrc.zsh").printAndCreateSymlink(File(userHome, ".zshrc"))
    }
}

fun Ansi.a(message: String, c: Ansi.Color) {
    fg(c).a(message).reset()
}

fun File.printAndDelete() {
    if (deleteRecursively() || delete()) {
        Ansi.ansi().run {
            a("删除: ")
            a(absolutePath, BLUE)
            toString()
        }.let(::println)
    } else {
        Ansi.ansi().run {
            a("错误: ", RED)
            a("删除 ")
            a(absolutePath, BLUE)
            a(" 的操作失败了")
        }
    }
}

fun File.printAndCopyRecursively(target: File) {
    if (cleanBackup) return

    copyRecursively(target, true)

    Ansi.ansi().run {
        a("复制: ")
        a(absolutePath, BLUE)
        a(" -> ")
        a(target.absolutePath, BLUE)
        toString()
    }.let(::println)
}

fun File.printAndRenameToBak() {
    File("${absolutePath}.bak").let {
        if (cleanBackup && it.exists()) it.printAndDelete()
        if (doNotBackup) return

        renameTo(it)

        Ansi.ansi().run {
            a("重命名: ")
            a(absolutePath, BLUE)
            a(" -> ")
            a(it.absolutePath, BLUE)
            toString()
        }.let(::println)

        if (backupAndDeleteIt) printAndDelete()
    }
}

fun File.printAndCreateSymlink(target: File) {
    if (cleanBackup) return
    target.run {
        if (Files.isSymbolicLink(toPath())) {
            printAndDelete()
        } else {
            printAndRenameToBak()
        }
    }

    Files.createSymbolicLink(target.toPath(), toPath())

    Ansi.ansi().run {
        a("链接: ")
        a(absolutePath, BLUE)
        a(" -> ")
        a(target.absolutePath, BLUE)
        toString()
    }.let(::println)
}