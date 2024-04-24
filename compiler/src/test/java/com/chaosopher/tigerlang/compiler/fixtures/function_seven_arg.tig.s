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
movq 24(%rbp), %r10 # mem(boe)
add %rdx, %rsi
add %rcx, %rsi
add %r8, %rsi
add %r9, %rsi
movq %rsi, %rax # add lexp -> r
add %rdi, %rax
add %r10, %rax
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
movq $7, %r10 # integerExpression
movq %rbp, %rdi # move reg arg 0 to temp
pushq %r10 # move reg arg 7 to stack
pushq %rax # move reg arg 6 to stack
call L0
movq %rax, %rdi # move reg arg 0 to temp
call printi
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
