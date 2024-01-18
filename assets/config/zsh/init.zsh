setopt autocd beep extendedglob nomatch notify

for f (${(f)"$(print -l ${ZSH_CONFIG_HOME}/source/*)"}) {
    if [[ -f $f && -r $f ]] {
        source "$f"
    }
}

source ${ZSH_CONFIG_HOME}/zinit.zsh
zsh ${ZSH_CONFIG_HOME}/motd/motd.zsh
