.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L3:
movq $5, %rax # move(t, i)
movq $3, %rcx # move(t, i)
movq $1, %rdi # move(t, i)
cmp %rcx, %rax
jle L0
L1:
movq $0, %rdi # move(t, i)
L0:
call printi
jmp L2
L2:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
