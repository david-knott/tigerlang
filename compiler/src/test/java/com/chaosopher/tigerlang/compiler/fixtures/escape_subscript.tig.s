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
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rcx # mem(boe)
movq $3, %rax # bin(i,i)
movq $8, %rdx # bin(i,i)
imul %rdx # imul rax * r 
add %rax, %rcx
movq (%rcx), %rdi # mem(temp)
call printi
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
subq $16, %rsp
# start main
# start 
L4:
movq %r15, -16(%rbp) # spill store
movq $-8, %rax # bin(t, i)
movq %rbp, %r15 # add lexp -> r
add %rax, %r15
movq $12, %rdi # integerExpression
movq $3, %rsi # integerExpression
call initArray
movq %rax, (%r15) # store
movq %rbp, %rdi # move reg arg 0 to temp
call L0
movq -16(%rbp), %r15 # spill load
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
