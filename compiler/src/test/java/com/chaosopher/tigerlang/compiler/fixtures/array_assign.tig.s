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
movq $10, %rdi # integerExpression
movq $0, %rsi # integerExpression
call initArray
movq %rax, %r15 # move(t, t)
movq $0, %rax # integerExpression
movq $10, %rcx # integerExpression
movq %rcx, (%r15, %rax, 8) # store array
movq $9, %rax # integerExpression
movq $9, %rcx # integerExpression
movq %rcx, (%r15, %rax, 8) # store array
movq $5, %rax # integerExpression
movq $5, %rcx # integerExpression
movq %rcx, (%r15, %rax, 8) # store array
movq $5, %rax # bin(i,i)
movq $8, %rcx # bin(i,i)
imul %rcx # imul rax * r 
movq %r15, %rcx # add lexp -> r
add %rax, %rcx
movq (%rcx), %rdi # mem(temp)
call printi
movq $0, %rax # bin(i,i)
movq $8, %rcx # bin(i,i)
imul %rcx # imul rax * r 
movq %r15, %rcx # add lexp -> r
add %rax, %rcx
movq (%rcx), %rdi # mem(temp)
call printi
movq $9, %rax # bin(i,i)
movq $8, %rcx # bin(i,i)
imul %rcx # imul rax * r 
add %rax, %r15
movq (%r15), %rdi # mem(temp)
call printi
movq -8(%rbp), %r15 # spill load
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
