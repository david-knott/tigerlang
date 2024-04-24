.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L1:
movq $16, %rdi # integerExpression
call initRecord
movq $10, 0(%rax) # store to offset 2
movq $0, 8(%rax) # store to offset 2
movq 0(%rax), %rdi # mem(boe)
call printi
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
