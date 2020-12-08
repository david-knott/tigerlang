.global tigermain
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L2:
movq %rdi, -8(%rbp) # store to offset 1
movq %rsi, %rax # add lexp -> r
add %rdx, %rax
jmp L1
L1:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L4:
movq $5, %rsi # integerExpression
movq $9, %rdx # integerExpression
movq %rbp, %rdi # move reg arg 0 to temp
call L0
movq %rax, %rdi # rax to temp 
call printi
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
