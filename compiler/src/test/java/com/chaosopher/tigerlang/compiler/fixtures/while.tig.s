.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L4:
movq $0, %rdi # move(t, i)
L1:
movq $11, %rax # integerExpression
cmp %rax, %rdi
jl L2
L0:
call printi
jmp L3
L2:
movq $1, %rax # bin(t, i)
add %rax, %rdi
jmp L1
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
