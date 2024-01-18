-- Options are automatically loaded before lazy.nvim startup
-- Default options that are always set: https://github.com/LazyVim/LazyVim/blob/main/lua/lazyvim/config/options.lua
-- Add any additional options here

local opt = vim.opt

for k, v in ipairs(opt.fileencodings:get()) do
  opt.fileencodings:remove(v)
end

opt.fileencodings:append("ucs-bom")
opt.fileencodings:append("utf-8")
opt.fileencodings:append("gbk")
opt.fileencodings:append("default")
opt.fileencodings:append("latin1")

--for k, v in ipairs(opt.fileencodings:get()) do
--  print(k .. ":" .. v)
--end

-- 设定 <Tab> 长度
opt.tabstop = 4
-- 在编辑模式的时候按退格键删除的长度，配合 expandtab 用
opt.softtabstop = opt.tabstop:get()
-- 每一级缩进的长度
opt.shiftwidth = opt.tabstop:get()
-- 缩进是否用空格表示
opt.expandtab = true
