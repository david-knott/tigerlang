.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L5:
movq $0, %rcx # move(t, i)
movq $2, %rax # move(t, i)
movq $12, %rdx # move(t, i)
L0:
cmp %rdx, %rax
jle L1
L3:
movq $10, %rdi # integerExpression
call printi
jmp L4
L1:
movq $1, %rsi # bin(t, i)
add %rsi, %rcx
cmp %rdx, %rax
je L3
L2:
movq $1, %rsi # bin(t, i)
add %rsi, %rax
jmp L0
L4:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
