.global tigermain
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L8:
movq %rdi, -8(%rbp) # store to offset 1
movq $0, %rax # integerExpression
cmp %rax, %rsi
je L4
L5:
movq 8(%rsi), %rax # mem(boe)
movq $0, %rcx # integerExpression
cmp %rcx, %rax
je L1
L2:
movq -8(%rbp), %rdi # mem(boe)
movq 8(%rsi), %rsi # mem(boe)
call L0
movq $1, %rcx # bin(i, t)
add %rax, %rcx
movq %rcx, %rax # move(t, t)
L3:
L6:
jmp L7
L4:
movq $0, %rax # move(t, i)
jmp L6
L1:
movq $1, %rax # move(t, i)
jmp L3
L7:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
# start main
# start 
L10:
movq %r12, -32(%rbp) # spill store
movq %r13, -24(%rbp) # spill store
movq %r14, -16(%rbp) # spill store
movq %r15, -8(%rbp) # spill store
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %r12 # rax to temp 
movq $2, 0(%r12) # store to offset 2
movq $8, %rax # bin(t, i)
movq %r12, %r14 # add lexp -> r
add %rax, %r14
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %r13 # rax to temp 
movq $3, 0(%r13) # store to offset 2
movq $8, %rax # bin(t, i)
movq %r13, %r15 # add lexp -> r
add %rax, %r15
movq $16, %rdi # integerExpression
call initRecord
movq $4, 0(%rax) # store to offset 2
movq $0, 8(%rax) # store to offset 2
movq %rax, (%r15) # store
movq %r13, (%r14) # store
movq %rbp, %rdi # move reg arg 0 to temp
movq %r12, %rsi # move reg arg 1 to temp
call L0
movq %rax, %rdi # rax to temp 
call printi
movq -32(%rbp), %r12 # spill load
movq -24(%rbp), %r13 # spill load
movq -16(%rbp), %r14 # spill load
movq -8(%rbp), %r15 # spill load
jmp L9
L9:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
