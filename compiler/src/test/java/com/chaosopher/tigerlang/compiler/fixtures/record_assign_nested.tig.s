.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# start 
L1:
movq %r14, -16(%rbp) # spill store
movq %r15, -8(%rbp) # spill store
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %r14 # rax to temp 
movq $3, 0(%r14) # store to offset 2
movq $8, %rax # bin(t, i)
movq %r14, %r15 # add lexp -> r
add %rax, %r15
movq $16, %rdi # integerExpression
call initRecord
movq $5, 0(%rax) # store to offset 2
movq $0, 8(%rax) # store to offset 2
movq %rax, (%r15) # store
movq 8(%r14), %rax # mem(boe)
movq 0(%rax), %rdi # mem(boe)
call printi
movq -16(%rbp), %r14 # spill load
movq -8(%rbp), %r15 # spill load
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
