.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L3:
movq $24, %rdi # integerExpression
call initRecord
movq $10, 0(%rax) # store to offset 2
movq $20, 8(%rax) # store to offset 2
movq $0, 16(%rax) # store to offset 2
movq $0, %rax # move(t, i)
movq $1, %rdi # move(t, i)
movq $0, %rcx # integerExpression
cmp %rcx, %rax
je L0
L1:
movq $0, %rdi # move(t, i)
L0:
call printi
jmp L2
L2:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
