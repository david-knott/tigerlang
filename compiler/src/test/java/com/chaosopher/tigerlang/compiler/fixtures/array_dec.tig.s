.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L1:
movq $10, %rdi # integerExpression
movq $6, %rsi # integerExpression
call initArray
movq %rax, %rcx # rax to temp 
movq $9, %rax # bin(i,i)
movq $8, %rdx # bin(i,i)
imul %rdx # imul rax * r 
add %rax, %rcx
movq (%rcx), %rdi # mem(temp)
call printi
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
