alias 'cd..'='cd ..'

alias 'ls'='ls --color=auto '
alias 'll'='ls --color=auto -l '
alias 'la'='ls --color=auto -A '
alias 'all'='ls --color=auto -Al '
#有种在大声嚷嚷着要全部展示出来的感觉
alias 'All'='ls --color=auto -al '

[[ -x '/bin/nvim' ]] && alias 'vi'='/bin/nvim '

[[ -x '/bin/sudo']] && alias 'sudo'='sudo -E '

[[ -x '/bin/sl' ]] && alias 'sl'='sl -ce3'