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
movq 16(%rbp), %rdi # mem(boe)
movq %rsi, %rax # add lexp -> r
add %rdx, %rax
add %rcx, %rax
add %r8, %rax
add %r9, %rax
add %rdi, %rax
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
movq $1, %rsi # integerExpression
movq $2, %rdx # integerExpression
movq $3, %rcx # integerExpression
movq $4, %r8 # integerExpression
movq $5, %r9 # integerExpression
movq $6, %rax # integerExpression
movq %rbp, %rdi # move reg arg 0 to temp
pushq %rax # move reg arg 6 to stack
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
