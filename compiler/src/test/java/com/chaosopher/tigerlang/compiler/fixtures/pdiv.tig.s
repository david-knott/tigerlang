.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L1:
movq $8, %rax # move(t, i)
movq $2, %rcx # move(t, i)
xor %rdx, %rdx # div clear bits rdx 
idiv %rcx # div rax * rexp 
movq %rax, %rdi # div rax -> r
call printi
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
