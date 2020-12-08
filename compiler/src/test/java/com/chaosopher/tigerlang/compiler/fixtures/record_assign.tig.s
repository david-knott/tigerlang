.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L1:
movq %r15, -8(%rbp) # spill store
movq $24, %rdi # integerExpression
call initRecord
movq $10, 0(%rax) # store to offset 2
movq $20, 8(%rax) # store to offset 2
movq $0, 16(%rax) # store to offset 2
movq %rax, %r15 # move(t, t)
movq $20, 0(%r15) # store to offset 2
movq $21, 8(%r15) # store to offset 2
movq 0(%r15), %rdi # mem(boe)
call printi
movq 8(%r15), %rdi # mem(boe)
call printi
movq -8(%rbp), %r15 # spill load
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
