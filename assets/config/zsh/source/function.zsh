function it-is-mine() {
    local user="$(whoami)"
    local group=":$(whoami)"
    local directoryPermissions="755"
    local filePermissions="644"
    local opt=""

    while {getopts u:g:d:f:vh arg} {
        case $arg {
            (u)
                user="$OPTARG"
            ;;
            (g)
                group=":$OPTARG"
            ;;
            (d)
                directoryPermissions="$OPTARG"
            ;;
            (f)
                filePermissions="$OPTARG"
            ;;
            (v)
                opt="-v"
            ;;
            (h)
                print -u 2 -X 2 \
"\e[6m用法: it-is-mine [选项...] [文件...]
遍历并根据参数分别设置目录或文件的权限

\t-h\t获取帮助信息
\t-v\t输出详细信息
\t-u\t设置所有者;默认为当前用户
\t-g\t设置所有组;默认为当前用户组
\t-d\t设置目录权限;默认为755
\t-f\t设置文件权限;默认为644\e[0m"
                return
            ;;
            (?)
                return 1
            ;;
        }
    }

    local function loopDirectory() {
        for f (${(f)"$(print -l ${1}/*)"}) { l $f }
    }

    local function l() {
        f=${1:A}
        chown $opt "${user}${group}" $f
        if [[ -d $f ]] {
            chmod $opt $directoryPermissions $f
            loopDirectory $f
        } elif [[ -f $f ]] {
            chmod $opt $filePermissions $f
        }
    }

    for f ($*[$OPTIND,-1]) {
        l $f
    }

    unfunction loopDirectory
    unfunction l
}
