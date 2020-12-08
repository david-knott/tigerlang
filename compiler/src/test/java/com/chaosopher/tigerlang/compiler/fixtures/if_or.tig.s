.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L7:
movq $4, %rax # move(t, i)
movq $3, %rcx # move(t, i)
movq $5, %rdx # move(t, i)
movq $6, %rsi # move(t, i)
cmp %rcx, %rax
jg L0
L1:
cmp %rsi, %rdx
jg L3
L4:
movq $0, %rdi # move(t, i)
L5:
call printi
jmp L6
L0:
L3:
movq $1, %rdi # move(t, i)
jmp L5
L6:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
